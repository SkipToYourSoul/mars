package com.zeedoo.mars.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageSerializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSerializer.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private MessageSerializer() {
		//Hidden on purpose
	}
	
	public static String toJSON(Message message) throws Exception {
	 	String json;
		try {
			json = MAPPER.writeValueAsString(message);
		 	return json;
		} catch (JsonProcessingException e) {
			LOGGER.error("Unable to serialize message=" + message.toString(), e);
			throw e;
		}
	}

}
