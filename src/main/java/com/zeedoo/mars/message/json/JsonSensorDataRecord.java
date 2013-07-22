package com.zeedoo.mars.message.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an individual sensor data record in a JSON message
 * @author nzhu
 *
 */
public class JsonSensorDataRecord {
	
	@JsonProperty
	private String sensorId;
	
	@JsonProperty
	private Long sensorTimestamp;
	
	@JsonProperty
	private String sensorValue;
	
	@JsonProperty
	private String sensorType;
	
	public JsonSensorDataRecord() {
		// for JSON purposes
	}
	
	public JsonSensorDataRecord(String sensorId, Long sensorTimestamp, String sensorValue, String sensorType) {
		this.sensorId = sensorId;
		this.sensorTimestamp = sensorTimestamp;
		this.sensorValue = sensorValue;
		this.sensorType = sensorType;
	}
	
	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public Long getSensorTimestamp() {
		return sensorTimestamp;
	}

	public void setSensorTimestamp(Long sensorTimestamp) {
		this.sensorTimestamp = sensorTimestamp;
	}

	public String getSensorValue() {
		return sensorValue;
	}

	public void setSensorValue(String sensorValue) {
		this.sensorValue = sensorValue;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
}