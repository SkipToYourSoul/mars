package com.zeedoo.mars.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the type of the Message as well as the code string
 * @author nzhu
 *
 */
public enum MessageType {
		
	INSTANT_SENSOR_DATA_SYNC("insant_sensor_data_sync"),
	SENSOR_ALIVE_STATUS("sensor_alive_status"),
	TIMESTAMP_SYNC("timestamp_sync"),
	REPONSE_TIMESTAMP_SYNC("response_timestamp_sync"),
	HANDSHAKE("handshake"),
	
	/**
	 *  File transfer related
	 */
	GET_SENSOR_DATA_SIZE("get_sensor_data_size"),
	RESPONSE_GET_SENSOR_DATA_SIZE("response_get_sensor_data_size"),
	
	SENSOR_DATA_TRANSFER("sensor_data_transfer"),
	RESPONSE_SENSOR_DATA_TRANSFER("response_sensor_data_transfer"),
	
	SENSOR_FILE_INFO("sensor_file_info"),
	RESPONSE_SENSOR_FILE("response_sensor_file_info"),
	
	SENSOR_FILE_PACKET("sensor_file_packet"),
	RESPONSE_FILE_PACKET("response_sensor_file_packet");
	
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
