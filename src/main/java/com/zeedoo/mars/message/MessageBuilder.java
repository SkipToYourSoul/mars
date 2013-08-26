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
	
	private MessageBuilder () {
		//hidden on purpose
	}
	
	public static Message buildMessage(MessageType type, Optional<JsonNode> payload) {
		Message message = buildBasicMessage();
		message.setMessageType(type);
		if (payload.isPresent()) {
			message.setPayload(payload.get());
		}
		LOGGER.debug("Built message={}", message);
		return message;
	}
	
	public static Message buildResponseMessage(MessageType type, Optional<JsonNode> payload, Integer responseCode, String errorMessage) {
		Preconditions.checkArgument(responseCode != null, "responseCode should not be null");
		Message message = buildBasicMessage();
		message.setMessageType(type);
		if (payload.isPresent()) {
			message.setPayload(payload.get());
		}
		message.setErrorMessage(errorMessage);
		message.setResponseCode(String.valueOf(responseCode));
		LOGGER.debug("Built message={}", message);
		return message;
	}

	private static Message buildBasicMessage() {
		Message message = new Message();
		message.setId(UUID.randomUUID().toString());
		message.setSource(SOURCE);
		message.setSourceId(SOURCE_ID);
		message.setTimestamp(DateTime.now(DateTimeZone.UTC).getMillis() / 1000L);
		return message;
	}
}
