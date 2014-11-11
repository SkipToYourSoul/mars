package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.payload.SensorDataSyncInfo;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageBuilder;
import com.zeedoo.mars.message.MessageDeserializer;
import com.zeedoo.mars.message.MessageType;
import com.zeedoo.mars.service.FileTransferServiceBean;

@Component
public class GetSensorDataSyncInfoResponseMessageHandler extends AbstractMessageHandler {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(GetSensorDataSyncInfoResponseMessageHandler.class);
	
	@Autowired
	private FileTransferServiceBean fileTransferService;

	@Override
	public MessageType getHandledType() {
		return MessageType.RESPONSE_GET_SENSOR_DATA_SYNC_INFO;
	}
	
	@Override
	protected Optional<Message> doHandleMessage(Message message,
			ChannelHandlerContext ctx) throws Exception {
		Preconditions.checkArgument(message.getPayload() != null, "Payload should not be null");
		// Payload should be an integer representing the total size (KB) of all sensor data in this Sun device
		final SensorDataSyncInfo sensorDataSyncInfo = MessageDeserializer.deserializeSensorDataSyncInfo(message.getPayloadAsRawJson());
		final int totalSensorDataSize = sensorDataSyncInfo.getDataSize();
		final int sensorDataSizeThreshold = fileTransferService.getSensorDataSizeThreshold();
		final int maxWaitTimeInSeconds = fileTransferService.getMaxSensorDataSyncWaitTime();
		final Long lastSuccessfulSyncTimestamp = sensorDataSyncInfo.getLastDataSyncTimestamp();
		// We will request a sensor data transfer when
		// Sun has responded us with a sensor data size greater than our threshold
		// OR the elapsed time since the last successful sensor data sync exceeds the threshold
		final boolean exceededDataSizeThreshold = totalSensorDataSize >= sensorDataSizeThreshold;
		final boolean exceededMaxWaitTime = lastSuccessfulSyncTimestamp != null 
				|| DateTime.now(DateTimeZone.UTC).minusSeconds(maxWaitTimeInSeconds).isAfter(sensorDataSyncInfo.getLastDataSyncTimestamp());
		final boolean noPreviousSuccessfulSync = lastSuccessfulSyncTimestamp == null;
		if (exceededDataSizeThreshold) {
			LOGGER.info("Total sensor data size={} in Sun is larger than threshold={}", totalSensorDataSize, sensorDataSizeThreshold); 
		} else if (exceededMaxWaitTime) {
			LOGGER.info("Last successful sensor data sync={} has exceeded the max wait time = {} seconds", 
					new DateTime(sensorDataSyncInfo.getLastDataSyncTimestamp()), maxWaitTimeInSeconds);
		} else if (noPreviousSuccessfulSync) {
			LOGGER.info("Sun has returned NULL for last successful data sync timestamp");
		}
		if (exceededDataSizeThreshold || exceededMaxWaitTime || noPreviousSuccessfulSync) {
			// Build response message to request sensor data transfer
			LOGGER.info("Requesting sensor data transfer from Sun");
			Message response = MessageBuilder.buildMessage(MessageType.SENSOR_FILEDATA_TRANSFER);
			return Optional.fromNullable(response);
		} else {
			return Optional.<Message>absent();
		}
	}
}
