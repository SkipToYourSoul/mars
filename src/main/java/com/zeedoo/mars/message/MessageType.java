package com.zeedoo.mars.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the type of the Message as well as the code string
 * @author nzhu
 *
 */
public enum MessageType {
	
	RESPONSE_TIMED_SENSOR_DATA_SYNC("response_timed_sensor_data_sync"),
	TIMED_SENSOR_DATA_SYNC("timed_sensor_data_sync"),
	
	INSTANT_SENSOR_DATA_SYNC("insant_sensor_data_sync"),
	SENSOR_ALIVE_STATUS("sensor_alive_status"),
	TIMESTAMP_SYNC("timestamp_sync"),
	REPONSE_TIMESTAMP_SYNC("response_timestamp_sync"),
	HANDSHAKE("handshake");
	
	private String type;
	
	private MessageType(String type) {
		this.type = type;
	}
	
	@JsonValue
	public String getType() {
		return this.type;
	}
	
	@JsonCreator
	public static MessageType fromType(String type) {
		for (MessageType t : MessageType.values()) {
			if (t.type.equals(type)) {
				return t;
			}
		}
		throw new IllegalArgumentException("Unknown MessageType String: " + type);
	}
}
