package com.zeedoo.mars.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.mars.message.json.JsonSensorAliveStatus;
import com.zeedoo.mars.message.json.JsonSensorDataRecord;

/**
 * Deserializes JSON String to Message object
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
	
	public static ObjectMapper getMapper() {
		return MAPPER;
	}
	
	/**
	 * PAYLOAD SPECIFIC methods
	 */
	
	/**
	 * Deserializes payload that contains sensor data records
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public static List<SensorDataRecord> deserializeSensorDataSyncPayload(String payload) throws Exception {
		Preconditions.checkArgument(payload != null, "Payload should NOT be null");
		try {
		   JsonSensorDataRecord[] records = MAPPER.readValue(payload, JsonSensorDataRecord[].class);
		   ArrayList<SensorDataRecord> result = Lists.newArrayListWithCapacity(records.length);
		   for (int i = 0; i < records.length; i++) {
		       JsonSensorDataRecord jsonRecord = records[i];
		       String sensorId = jsonRecord.getSensorId();
		       Long sensorTimestamp = jsonRecord.getSensorTimestamp();
		       String sensorValue = jsonRecord.getSensorValue();
		       // Validate fields
		       Preconditions.checkArgument(!StringUtils.isEmpty(sensorId), "sensorId is required");
		       Preconditions.checkArgument(!StringUtils.isEmpty(sensorValue), "sensorValue is required");
		       Preconditions.checkArgument(sensorTimestamp != null, "sensorTimestmap is required");
			   SensorDataRecord record = new SensorDataRecord(sensorId, new DateTime(sensorTimestamp), sensorValue);
			   result.add(record);
		   }
		   return result;
		   
		} catch (Exception e) {
			LOGGER.error("An exception occured. Payload=" + payload, e);
			throw e;
		}
	}
	
	public static List<SensorStatus> deserializeSensorAliveStatusPayload(String payload) throws Exception {
		Preconditions.checkArgument(payload != null, "Payload should NOT be null");
		try {
			JsonSensorAliveStatus[] statusList = MAPPER.readValue(payload, JsonSensorAliveStatus[].class);
			ArrayList<SensorStatus> result = Lists.newArrayListWithCapacity(statusList.length);
			for (int i = 0; i < statusList.length; i++) {
				JsonSensorAliveStatus jsonSensorStatus = statusList[i];
				String sensorId = jsonSensorStatus.getSensorId();
				String sunIpPort = jsonSensorStatus.getSensorSunIpPort();
				String sunMacAddress = jsonSensorStatus.getSensorSunMac();
				Integer sensorStatus = jsonSensorStatus.getSensorState();
				// Validate fields
				Preconditions.checkArgument(!StringUtils.isEmpty(sensorId), "sensorId is required");
				Preconditions.checkArgument(!StringUtils.isEmpty(sunMacAddress), "sensor MAC address is required");
				Preconditions.checkArgument(!StringUtils.isEmpty(sunIpPort), "sensor Sun Ip/port is required");
				Preconditions.checkArgument(sensorStatus != null, "sensor status is required");
				// Validate ip address port pair
				String[] ipPortPair = sunIpPort.split(":");
				Preconditions.checkState(ipPortPair.length == 2, "Sun Ip/port should contain a valid ip address and port pair");
				String ipAddress = ipPortPair[0];
				Integer port = Integer.parseInt(ipPortPair[1]);
				SensorStatus status = new SensorStatus(sensorId, ipAddress, port, sunMacAddress, DeviceStatus.fromStatusCode(sensorStatus));
				result.add(status);
			}
			return result;
			
		} catch (Exception e) {
			LOGGER.error("An exception occured. Payload=" + payload, e);
			throw e;
		}
		
	}
}
