package com.zeedoo.mars.message;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.zeedoo.mars.message.json.JsonSensorDataRecord;

public class MessageEncoderTest {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Test
	public void testMessageToBytes() throws Exception {
		Message message = new Message();	
		ArrayNode payload = MAPPER.createArrayNode();
		payload.addPOJO(new JsonSensorDataRecord("S1",12345L,"100","ST01"));
		payload.addPOJO(new JsonSensorDataRecord("S2",16666L,"200","ST02"));
		message.setMessageType(MessageType.INSTANT_SENSOR_DATA_SYNC);
		message.setTimestamp(12345L);
		message.setSource("Sun");
		message.setSourceId("1A2B3C");
		message.setPayload(payload);
		System.out.println(MAPPER.writeValueAsString(message));
	}

}
