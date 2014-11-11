package com.zeedoo.mars.message;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.commons.domain.payload.SensorFilePacket;
import com.zeedoo.mars.message.json.JsonSensorDataRecord;


public class MessageTest {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Test
	@Ignore
	public void serialize_InstantSensorDataSyncMessage() throws Exception {
		Message message = new Message();	
		ArrayNode payload = MAPPER.createArrayNode();
		payload.addPOJO(new JsonSensorDataRecord("S1",12345L,"100","ST01"));
		payload.addPOJO(new JsonSensorDataRecord("S2",16666L,"200","ST02"));
		message.setMessageType(MessageType.INSTANT_SENSOR_DATA_SYNC);
		message.setTimestamp(12345L);
		message.setSource("Sun");
		message.setSourceId("1A2B3C");
		message.setPayload(payload);
		assertThat(asJson(message), is(fixture("fixtures/instantSensorDataSync.json")));
	}
	
	@Test
	public void deserialize_InstantSensorDataSyncMessage() throws Exception {
		// Instant Sensor Data Sync 
		Message message = fromJson(fixture("fixtures/instantSensorDataSync.json"), Message.class);
		// Deserialize payload
		List<SensorDataRecord> records = MessageDeserializer.deserializeSensorDataSyncPayload(message.getPayloadAsRawJson());
		// Test that we can cast this Message to InstantSensorDataSyncMessage
		assertThat(message.getMessageType(), is(MessageType.INSTANT_SENSOR_DATA_SYNC));
		assertThat(records.size(), is(2));
		System.out.println(DateTime.now(DateTimeZone.UTC).getMillis() / 1000L);
	}
		
	@Test
	public void deserailize_SensorAliveStatusMessage() throws Exception {
		Message message = fromJson(fixture("fixtures/sensorAliveStatus.json"), Message.class);
		List<SensorStatus> statusList = MessageDeserializer.deserializeSensorAliveStatusPayload(message.getPayloadAsRawJson());
		assertThat(message.getMessageType(), is(MessageType.SENSOR_ALIVE_STATUS));
		assertThat(statusList.size(), is(2));
	}
	
	@Test
	public void deserialize_SensorFilePacketMessage() throws Exception {
		Message message = fromJson(fixture("fixtures/sensorFilePacket.json"), Message.class);
		assertThat(message.getMessageType(), is(MessageType.SENSOR_FILE_PACKET));
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
