package com.zeedoo.mars.message;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zeedoo.mars.domain.SensorDataRecord;

/**
 * This class contains utility methods to convert a JSON node to the correct domain model
 * @author nzhu
 *
 */
public class MessagePayloadConverter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagePayloadConverter.class);
	
	private MessagePayloadConverter() {
		// hidden on purpose
	}
	
	/**
	 * Converts a JSONArray to a list of sensor data records
	 * @param payload
	 * @return
	 */
	public static List<SensorDataRecord> sensorDataRecordsFromJson(JsonNode payload) {
		Preconditions.checkArgument(payload != null, "Payload should NOT be null");
		// Verify that the payload is a JSONArray
		if (payload.getNodeType() != JsonNodeType.ARRAY) {
			LOGGER.error("Message payload should be an JSON Array, but instead got={}", payload.toString());
			throw new IllegalStateException("Message payload is not a JSON Array");
		}
		ArrayList<SensorDataRecord> result = Lists.newArrayListWithCapacity(payload.size());
		// iterate through each entry in the payload, and make database interaction
	    for (int i = 0; i < payload.size(); i++) {
	    	JsonNode node = payload.get(i);
			try {
				SensorDataRecord record = sensorDataRecordFromJson(node);
				result.add(record);
			} catch (Exception e) {
				LOGGER.error("An error occured trying to parse node={}" + node.toString() + ". Skipping this entry", e);	
			} 	
	    }
	    return result;
	}
	
	/**
	 * Helper method to convert a JSON entry to an individual sensor data record
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private static SensorDataRecord sensorDataRecordFromJson(JsonNode node) throws Exception {
		if (node.getNodeType() == JsonNodeType.OBJECT) {
    		ObjectNode objNode = (ObjectNode)node;
    		String sensorId = objNode.get("sensorId").textValue();
    		long sensorTimestamp = objNode.get("sensorTimestamp").asLong();
    		String sensorType = objNode.get("sensorType").textValue();
    		String sensorValue = objNode.get("sensorValue").textValue();
    		if (StringUtils.isEmpty(sensorId) || StringUtils.isEmpty(sensorType) || StringUtils.isEmpty(sensorValue)) {
    		   throw new IllegalStateException("Encountered an invalid entry node=" + node.toString());
    		}
    		return new SensorDataRecord(sensorId, new DateTime(sensorTimestamp), sensorValue);
    	}
		throw new IllegalStateException("Node=" + node.toString() + "is not an Object Node.");
	}

}
