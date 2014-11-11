package com.zeedoo.mars.message;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.zeedoo.commons.domain.SensorDataRecord;

public class MessageDeserializerTest {

	@Test
	public void testDeserializeInstantSensorDataSyncPayload() throws Exception {
		String payload = "[{\"sensorId\":123456,\"sensorTimestamp\":1388769271.0,\"sensorType\":\"ST01\",\"sensorValue\":\" 22.80\"}]";
		List<SensorDataRecord> result = MessageDeserializer.deserializeSensorDataSyncPayload(payload);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1388769271000L, (long)result.get(0).getTimestampLong());
	}

}
