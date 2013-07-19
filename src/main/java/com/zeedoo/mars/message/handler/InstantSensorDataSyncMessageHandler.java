package com.zeedoo.mars.message.handler;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.mars.domain.SensorDataRecord;
import com.zeedoo.mars.message.Message;
import com.zeedoo.mars.message.MessagePayloadConverter;
import com.zeedoo.mars.message.MessageType;

@Component
public class InstantSensorDataSyncMessageHandler extends AbstractMessageHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstantSensorDataSyncMessageHandler.class);

	@Override
	public MessageType getHandledType() {
		return MessageType.INSTANT_SENSOR_DATA_SYNC;
	}

	@Override
	protected Optional<Message> doHandleMessage(Message message, ChannelHandlerContext ctx) {
		LOGGER.info("Handling Message={}", message);
		Preconditions.checkArgument(message.getPayload() != null, "Payload should NOT be null");
	    List<SensorDataRecord> records = MessagePayloadConverter.sensorDataRecordsFromJson(message.getPayload());
	    for (int i = 0; i < records.size(); i++) {
	    	LOGGER.info(records.get(i).toString());
	    }
		return Optional.<Message>absent();
	}
}
