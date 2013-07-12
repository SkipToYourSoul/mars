package com.zeedoo.mars.message;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Deserailizes JSON String to Message object
 * @author nzhu
 *
 */
public class MessageDeserializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDeserializer.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private MessageDeserializer() {
		//Hidden on purpose
	}
	
	public static Message fromJSON(String json) throws Exception {
		try {
			return MAPPER.readValue(json, Message.class);
		} catch (JsonParseException e) {
			LOGGER.error("Unable to parse Incoming JSON=" + json + ". Check JSON data integrity", e);
            throw e;
		} catch (JsonMappingException e) {
			LOGGER.error("Unable to map Incoming JSON=" + json + " to Message object", e);
            throw e;
		} catch (IOException e) {
			LOGGER.error("An IOException occured", e);
            throw e;
		}
	}

}
