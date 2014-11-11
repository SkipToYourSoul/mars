package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;
import com.zeedoo.commons.domain.payload.SensorFilePacket;
import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.event.SensorFileMergeEvent;
import com.zeedoo.mars.file.transfer.CompleteFile;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageResponseCode;
import com.zeedoo.mars.message.MessageType;
import com.zeedoo.mars.message.SensorFileMergeGateway;
import com.zeedoo.mars.service.FileTransferServiceBean;

@Component
public class SensorFilePacketMessageHandler extends AbstractMessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorFilePacketMessageHandler.class);
		
	@Autowired
	private FileTransferServiceBean fileTransferService;
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;
	
	@Autowired
	private SensorFileMergeGateway sensorFileMergeGateway;

	@Override
	public MessageType getHandledType() {
		return MessageType.SENSOR_FILE_PACKET;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx) throws Exception {
		validateMessage(message);
		Message response = null;
		try {
			final String sunMacAddress = message.getSourceId();
			final SensorFilePacket sensorFilePacket = MessageDeserializer.deserializeSensorFilePacket(message.getPayloadAsRawJson());
			validatePayload(sensorFilePacket);
			// Check our task pool for file transfer info
			String sensorId = sensorFilePacket.getSensorId();
			FileTransferTask fileTransfer = fileTransferService.getFileTransferTask(message.getSourceId(), FileType.SENSOR_DATA_READINGS, sensorId);
			if (fileTransfer == null) {
				LOGGER.error("Could not find Sun MAC address={} in file transfer task pool, requesting file re-send", message.getSourceId());
				return Optional.fromNullable(createResponseMessage(MessageResponseCode.FILE_TRANSFER_RESEND_COMPLETE_FILE));
			}
			int sensorFilePacketNumber = sensorFilePacket.getPacketNumber();
			int currentPacketInMars = fileTransfer.getCurrentPacket();
			// Check if the previous finished packet in task pool reflects the current packet number
			if (currentPacketInMars != sensorFilePacketNumber) {
				LOGGER.error("Sun has sent Mars packet number={}, but does not match Mars's current packet={}, requesting file re-send", 
						sensorFilePacketNumber, currentPacketInMars);
				fileTransferService.removeFileTransferTask(message.getSourceId(), FileType.SENSOR_DATA_READINGS, sensorId);
				response = createResponseMessage(MessageResponseCode.FILE_TRANSFER_RESEND_COMPLETE_FILE);
				return Optional.fromNullable(response);
			}
			// Check CRC 32 of the packet
			if (!checkCRC32(sensorFilePacket)) {
				LOGGER.error("Invalid CRC checksum, returning error response");
				// Update packet error count
				fileTransfer.setPacketErrorCount(fileTransfer.getPacketErrorCount()+1);
				fileTransferService.updateFileTransferTask(sunMacAddress, fileTransfer);
				response = createResponseMessage(MessageResponseCode.FILE_TRANSFER_CRC32_INCORRECT);
				return Optional.fromNullable(response);	
			}
			// CRC 32, process the packet
			response = processFilePacket(sensorFilePacket, fileTransfer);
			return Optional.fromNullable(response);
		} catch (Exception e) {
			marsErrorHandler.handleError(ctx, e);
			if (ctx.channel().isOpen()) {
				response = createResponseMessage(MessageResponseCode.MARS_INTERNAL_ERROR);
				return Optional.fromNullable(response);
			}
			return Optional.absent();
		}
	}
	
    /**
     * Updates the data of the in-memory task pool with given sensor file packet
     * @param sunMacAddress
     * @param sensorFilePacket
     * @param fileTransferTask
     * @return a Message response
     * @throws IOException 
     * @throws NoSuchAlgorithmException 
     */
	private Message processFilePacket(SensorFilePacket sensorFilePacket, FileTransferTask fileTransferTask) throws IOException, NoSuchAlgorithmException {
		final String sensorId = fileTransferTask.getFileId();
		final String sunMacAddress = fileTransferTask.getSunMacAddress();
		Optional<CompleteFile> completeFile = fileTransferService.processFilePacket(fileTransferTask, sensorFilePacket);
		if (completeFile.isPresent()) {
			// We have reached the last packet, process the decoded file
			if (verifyMD5(fileTransferTask.getMd5(), completeFile.get().getMd5Hash())) {
				String decodedFilePath = fileTransferService.decodeFile(completeFile.get().getData(), fileTransferTask);
				fileTransferTask.setFilePath(decodedFilePath);
				fileTransferTask.setCompleted(true);
				fileTransferService.updateFileTransferTask(sunMacAddress, fileTransferTask);
				// Send message to merge sensor data file to DB
    			sensorFileMergeGateway.sendSensorFileMergeEvent(new SensorFileMergeEvent(sensorId, decodedFilePath));
    			//TODO: Merge two filetransferTask calls into one
    			fileTransferService.removeFileTransferTask(sunMacAddress, FileType.SENSOR_DATA_READINGS, sensorId);
    			return createResponseMessage(MessageResponseCode.FILE_TRANSFER_SUCCESS);
			} else {
				LOGGER.error("Computed MD5={} is different from expected MD5={}", completeFile.get().getMd5Hash(), fileTransferTask.getMd5());
				fileTransferService.removeFileTransferTask(sunMacAddress, FileType.SENSOR_DATA_READINGS, sensorId);
				return createResponseMessage(MessageResponseCode.FILE_TRANSFER_MD5_INCORRECT);
			}
		} else {
			// update packet number
			fileTransferTask.setCurrentPacket(sensorFilePacket.getPacketNumber() + 1);
			fileTransferService.updateFileTransferTask(sunMacAddress, fileTransferTask);
			// just send an OK response to acknowledge the packet
			return createResponseMessage(MessageResponseCode.OK);
		}
	}

	private void validatePayload(
			final SensorFilePacket sensorFilePacket) {
		// Validate state, return an error response if necessary
		Preconditions.checkState(sensorFilePacket.getSensorId() != null, "Sensor Id should not be null");
		Preconditions.checkState(sensorFilePacket.getData() != null, "Data should not be null");
		Preconditions.checkState(sensorFilePacket.getCRC32() != null, "CRC 32 should not be null");
	}

	private void validateMessage(Message message) {
		Preconditions.checkArgument(message != null, "Message should not be null");
		Preconditions.checkArgument(message.getSourceId() != null, "Source Id should not be null");
		Preconditions.checkArgument(message.getPayload() != null, "Payload should not be null");
	}
	
	private Message createResponseMessage(MessageResponseCode code) {
		return MessageBuilder.buildEmptyPayloadResponseMessage(MessageType.RESPONSE_FILE_PACKET, code);
	}
	
	/**
	 * Checks if the packet valid
	 * @param sensorFilePacket
	 * @return
	 */
	private boolean checkCRC32(SensorFilePacket sensorFilePacket) {
		if (fileTransferService.getBypassCRC32Check()) {
			LOGGER.info("CRC32 check is DISABLED! Returning TRUE for CRC32 check");
			return true;
		} else {
			long receivedCRC32 = Long.parseLong(sensorFilePacket.getCRC32());
			CRC32 crc = new CRC32();
			crc.update(sensorFilePacket.getData().getBytes());
			long computedCRC32 = crc.getValue();
			LOGGER.debug("Computed crc: " + computedCRC32);
			return receivedCRC32 == computedCRC32;
		}
	}
	
	/**
	 * Computes the MD5 with the actual data and verify it with the one in the original file header
	 * @param md5
	 * @param actualData
	 * @return
	 */
	private boolean verifyMD5(String expectedMd5, String actualMd5) {
		if (fileTransferService.getBypassMD5Check()) {
			LOGGER.info("MD5 check is DISABLED! Returning TRUE for MD5 check");
			return true;
		}
		return Objects.equals(expectedMd5, actualMd5);
	}
}
