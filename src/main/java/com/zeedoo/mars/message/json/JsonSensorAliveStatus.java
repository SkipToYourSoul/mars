package com.zeedoo.mars.message.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSensorAliveStatus {
	
	@JsonProperty
	private String sensorId;
	
	@JsonProperty
	private String sensorSunIpPort;
	
	@JsonProperty
	private String sensorSunMac;
	
	@JsonProperty
	private Integer sensorState;

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorSunIpPort() {
		return sensorSunIpPort;
	}

	public void setSensorSunIpPort(String sensorSunIpPort) {
		this.sensorSunIpPort = sensorSunIpPort;
	}

	public String getSensorSunMac() {
		return sensorSunMac;
	}

	public void setSensorSunMac(String sensorSunMac) {
		this.sensorSunMac = sensorSunMac;
	}

	public Integer getSensorState() {
		return sensorState;
	}

	public void setSensorState(Integer sensorState) {
		this.sensorState = sensorState;
	}
}
