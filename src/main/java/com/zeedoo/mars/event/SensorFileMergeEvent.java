package com.zeedoo.mars.event;

import java.io.File;

import com.google.common.base.Objects;

/**
 * Represents a sensor data file transferred from Sun for a specific sensor (represented by sensorId)
 * @author nzhu
 *
 */
public class SensorFileMergeEvent {
	
	private String sensorId;
	
	private String filePath;
	
	public SensorFileMergeEvent(String sensorId, String filePath) {
		this.sensorId = sensorId;
		this.filePath = filePath;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sensorId, filePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorFileMergeEvent other = (SensorFileMergeEvent) obj;
		return Objects.equal(this.sensorId, other.sensorId) && Objects.equal(this.filePath, other.filePath);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("sensorId", sensorId).add("filePath", filePath).toString();
	}
}
