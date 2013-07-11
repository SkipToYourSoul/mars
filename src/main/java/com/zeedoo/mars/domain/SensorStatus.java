package com.zeedoo.mars.domain;

import com.google.common.base.Objects;

/**
 * Represents a Sensor's connection status
 * @author nzhu
 *
 */
public class SensorStatus {
	
	/**
	 * Sensor Id
	 */
	String sensorId;
	
	/** 
	 * Human-readable IP / Port of the Sun system that the sensor belongs to
	 */
	String sunIp;
	
	Integer sunPort;
	
	/**
	 * Device status of this Sensor
	 */
	DeviceStatus deviceStatus;

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String getSunIp() {
		return sunIp;
	}

	public void setSunIp(String sunIp) {
		this.sunIp = sunIp;
	}

	public Integer getSunPort() {
		return sunPort;
	}

	public void setSunPort(Integer sunPort) {
		this.sunPort = sunPort;
	}

	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(SensorStatus.class).add("sensorId", sensorId).add("sunIp", sunIp).add("sunPort", sunPort)
				.add("deviceStatus", deviceStatus).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sensorId, sunIp, sunPort, deviceStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorStatus other = (SensorStatus) obj;
		return Objects.equal(sensorId, other.sensorId) 
				&& Objects.equal(sunIp, other.sunIp)
				&& Objects.equal(sunPort, other.sunPort)
				&& Objects.equal(deviceStatus, other.deviceStatus);
	}
}
