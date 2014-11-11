package com.zeedoo.mars.message;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class MessageBuilder {
	
	private static final String SOURCE = "Mars";
	private static final String SOURCE_ID = "Mars";
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilder.class);
	
	private MessageBuilder() {
		//Hidden on purpose
	}
	
	public static Message buildMessage(MessageType type) {
		return buildMessage(type, Optional.<JsonNode>absent());
	}
	
	public static Message buildMessage(MessageType type, Optional<JsonNode> payload) {
		Message message = buildBasicMessage(type);
		if (payload.isPresent()) {
			message.setPayload(payload.get());
		}
		LOGGER.debug("Built message={}", message);
		return message;
	}
	
	public static Message buildResponseMessage(MessageType type, String responseCode, String errorMessage) {
		Message message = buildMessage(type);
		message.setResponseCode(responseCode);
		message.setErrorMessage(errorMessage);
		return message;
	}
	
	public static Message buildResponseMessage(MessageType type, Optional<JsonNode> payload, String responseCode, String errorMessage) {
		Preconditions.checkArgument(responseCode != null, "responseCode should not be null");
		Message message = buildResponseMessage(type, responseCode, errorMessage);
		if (payload.isPresent()) {
			message.setPayload(payload.get());
		}
		LOGGER.debug("Built message={}", message);
		return message;
	}
	
    /**
	 * Builds an error response in case of an exception
	 * @return
	 */
	public static Message buildEmptyPayloadResponseMessage(MessageType messageType, MessageResponseCode responseCode) {
		Message responseMessage = MessageBuilder.buildResponseMessage(messageType, Optional.<JsonNode>absent(), 
				responseCode.getCode(), null);
		return responseMessage;
	}
	
	private static Message buildBasicMessage(MessageType type) {
		Message message = new Message();
		message.setMessageType(type);
		message.setId(UUID.randomUUID().toString());
		message.setSource(SOURCE);
		message.setSourceId(SOURCE_ID);
		message.setTimestamp(DateTime.now(DateTimeZone.UTC).getMillis() / 1000L);
		return message;
	}
}
