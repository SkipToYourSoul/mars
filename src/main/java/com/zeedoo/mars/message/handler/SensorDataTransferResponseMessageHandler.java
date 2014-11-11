package com.zeedoo.mars.message.handler;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessageResponseCode;
import com.zeedoo.mars.message.MessageType;
import com.zeedoo.mars.service.FileTransferServiceBean;

@Component
public class SensorDataTransferResponseMessageHandler extends AbstractMessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataTransferResponseMessageHandler.class);
		
	@Autowired
	private FileTransferServiceBean fileTransferService;

	@Override
	public MessageType getHandledType() {
		return MessageType.RESPONSE_SENSOR_FILEDATA_TRANSFER;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message,
			ChannelHandlerContext ctx) throws Exception {
		final String responseCode = message.getResponseCode();
		Preconditions.checkArgument(message.getResponseCode() != null, "Message response code should not be null");
		// Check response code
		if (MessageResponseCode.FILE_TRANSFER_SUCCESS.getCode().equals(responseCode)) {
			// Sun has acknowledged that the transfer was done and complete, we just need to remove the entry from our cache and log
			LOGGER.info("Successfully completed sensor data transfer with Sun MacAddress={}", message.getSourceId());
		} else if (MessageResponseCode.SENSOR_DATA_TRANFSER_ABORTED.getCode().equals(responseCode)) {
			// Sun has aborted the file transfer this time, just log 
			LOGGER.info("Sensor data transfer has been ABORTed with Sun MacAddress={}", message.getSourceId());
		}
        // We don't need to send a response
		return Optional.<Message>absent();
	}

}
