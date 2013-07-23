package com.zeedoo.mars.message;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;

public class MessageBuilder {
	
	private static final String SOURCE = "Mars";
	private static final String SOURCE_ID = "Mars";
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilder.class);
	
	private MessageBuilder () {
		//hidden on purpose
	}
	
	public static Message buildMessage(MessageType type, Optional<JsonNode> payload, String responseCode, String errorMessage) {
		Message message = buildBasicMessage();
		message.setMessageType(type);
		if (payload.isPresent()) {
			message.setPayload(payload.get());
		}
		message.setErrorMessage(errorMessage);
		message.setResponseCode(responseCode);
		LOGGER.debug("Built message={}", message);
		return message;
	}

	private static Message buildBasicMessage() {
		Message message = new Message();
		message.setSource(SOURCE);
		message.setSourceId(SOURCE_ID);
		message.setTimestamp(new DateTime().getMillis() / 1000L);
		return message;
	}
}
