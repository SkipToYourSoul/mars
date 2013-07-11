package com.zeedoo.mars.message;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;


import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.zeedoo.mars.message.json.JsonSensorDataRecord;


public class MessageTest {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Test
	public void serializes_InstantSensorDataSyncMessage() throws Exception {
		Message message = new Message();	
		ArrayNode test = MAPPER.createArrayNode();
		test.addPOJO(new JsonSensorDataRecord("S1",12345L,"100","ST01"));
		test.addPOJO(new JsonSensorDataRecord("S2",16666L,"200","ST02"));
		message.setMessageType(MessageType.INSTANT_SENSOR_DATA_SYNC);
		message.setTimestamp(12345L);
		message.setSource("Sun");
		message.setSourceId("1A2B3C");
		message.setPayload(test);
		System.out.println(asJson(message));
		assertThat(asJson(message), is(fixture("fixtures/instantSensorDataSync.json")));
	}
	
	@Test
	public void deserialize_InstantSensorDataSyncMessage() throws Exception {
		// Instant Sensor Data Sync 
		Message genericMessage = fromJson(fixture("fixtures/instantSensorDataSync.json"), Message.class);
		// Test that we can cast this Message to InstantSensorDataSyncMessage
		System.out.println(genericMessage.toString());
		assertThat(genericMessage.getMessageType(), is(MessageType.INSTANT_SENSOR_DATA_SYNC));
		assertThat(genericMessage instanceof InstantSensorDataSyncMessage, is(true));
	}
	
	/** 
	 * JSON Utility Methods
	 */
	
	private static String fixture(String filename) throws IOException {
		return fixture(filename, Charsets.UTF_8);
	}
	
	private static String fixture(String filename, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(filename), charset).trim();
    }
	
    public static <T> T fromJson(String json, Class<T> klass) throws IOException {
        return MAPPER.readValue(json, klass);
    }
    
    public static String asJson(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }
}
