package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;
import com.zeedoo.commons.domain.payload.SensorFileInfo;
import com.zeedoo.mars.error.MarsErrorHandler;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageResponseCode;
import com.zeedoo.mars.message.MessageType;
import com.zeedoo.mars.service.FileTransferServiceBean;

@Component
public class SensorFileInfoMessageHandler extends AbstractMessageHandler {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorFileInfoMessageHandler.class);
	
	@Autowired
	private FileTransferServiceBean fileTransferService;
	
	@Autowired
	private MarsErrorHandler marsErrorHandler;

	@Override
	public MessageType getHandledType() {
		return MessageType.SENSOR_FILE_INFO;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message,
			ChannelHandlerContext ctx) throws Exception {
		Preconditions.checkArgument(message != null, "Message should not be null");
		Preconditions.checkArgument(message.getSourceId() != null, "Source Id should not be null");
		Preconditions.checkArgument(message.getPayload() != null, "Payload should not be null");
		// Try-catch is here to ensure we return an error message if there is an exception
		Message response = null;
		try {
		   SensorFileInfo sensorFileInfo = MessageDeserializer.deserializeSensorFileInfoPayload(message.getPayloadAsRawJson());
		   // Validate sensorFileInfo, if info is incomplete, return an error response
		   Preconditions.checkState(!StringUtils.isEmpty(sensorFileInfo.getSensorId()), "Sensor Id should not be null");
		   Preconditions.checkState(!StringUtils.isEmpty(sensorFileInfo.getMd5()), "MD5 should not be null");
		   String sensorId = sensorFileInfo.getSensorId();
		   // Check if we have a file transfer info object for this Sun device 
		   FileTransferTask fileTransferTask = fileTransferService.getFileTransferTask(message.getSourceId(), FileType.SENSOR_DATA_READINGS, sensorId);
		   if (fileTransferTask != null) {
			   if (isFileTransferTaskInErrorState(fileTransferTask)) {
				   fileTransferService.newFileTransferTask(message.getSourceId(), sensorFileInfo);
			   } else if (!isSameFile(sensorFileInfo, fileTransferTask)) {
				   LOGGER.error("Received SensorFileInfo={} does NOT match FileTransferTask={} in Mars, requesting file-resend", sensorFileInfo, fileTransferTask);
				   fileTransferService.removeFileTransferTask(message.getSourceId(), FileType.SENSOR_DATA_READINGS, sensorId);
				   response = createResponseMessage(MessageResponseCode.FILE_TRANSFER_RESEND_COMPLETE_FILE);
				   return Optional.fromNullable(response);
			   }   
		   } else {
			   fileTransferService.newFileTransferTask(message.getSourceId(), sensorFileInfo);
		   }
		   // build an OK response message
		   response = createResponseMessage(MessageResponseCode.OK);
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
	
	private Message createResponseMessage(MessageResponseCode code) {
		return MessageBuilder.buildEmptyPayloadResponseMessage(MessageType.RESPONSE_SENSOR_FILE_INFO, code);
	}
	
	private boolean isFileTransferTaskInErrorState(FileTransferTask fileTransferTask) {
		if (fileTransferTask.getPacketErrorCount() >= fileTransferService.getMaxFilePacketErrorRetries()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if two files are different
	 * @param sensorFileInfo
	 * @param fileTransfer
	 * @return
	 */
	private boolean isSameFile(SensorFileInfo sensorFileInfo, FileTransferTask fileTransfer) {
		Preconditions.checkArgument(sensorFileInfo != null, "sensorFileInfo should not be null");
		Preconditions.checkArgument(fileTransfer != null, "fileTransfer should not be null");
		return sensorFileInfo.getSensorId().equals(fileTransfer.getId()) 
			   && sensorFileInfo.getMd5().equals(fileTransfer.getMd5())
		       && sensorFileInfo.getNumberOfPackets() == fileTransfer.getNumberOfPackets()
		       && sensorFileInfo.getPacketStartNumber() == fileTransfer.getCurrentPacket();
	}
	
	/**
	 * Determines if a file is out-of-sync between Sun and Mars
	 * @param sensorFileInfo
	 * @param fileTransfer
	 * @return
	 */
	/*private boolean isFileOutOfSync(SensorFileInfo sensorFileInfo, FileTransferTask fileTransfer) {
		Preconditions.checkArgument(sensorFileInfo != null, "sensorFileInfo should not be null");
		Preconditions.checkArgument(fileTransfer != null, "fileTransfer should not be null");
		int numberOfPacketsFromMessage = sensorFileInfo.getNumberOfPackets();
		int numberOfPacketsInCache = fileTransfer.getNumberOfPackets();
		int startingPacketNumber = sensorFileInfo.getPacketStartNumber();
		int currentPacketInCache = fileTransfer.getCurrentPacket();
		if (numberOfPacketsFromMessage != numberOfPacketsInCache) {
			LOGGER.debug("Number of packets from Sun message is different in Mars cache, Sun : {}, Mars : {}", numberOfPacketsFromMessage, numberOfPacketsInCache);
			return true;
		}
		if (currentPacketInCache != startingPacketNumber) {
			LOGGER.info("Starting packet number : {} from Sun does not reflect current packet : {} in Mars", startingPacketNumber, currentPacketInCache);
			return true;
		}
     	return false;
	}*/
}
