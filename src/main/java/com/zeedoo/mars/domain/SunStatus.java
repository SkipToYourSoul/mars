package com.zeedoo.mars.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.base.Objects;

/**
 * Represents a Sun's connection status
 * @author nzhu
 *
 */
public class SunStatus {
	
	// Internal DB id
	String id;
	
	String sunId;
	
	String sunIpAddress;
	
	// Sun's online/offline status
	DeviceStatus sunStatus;
	
	// Last updated timestamp
	DateTime lastUpdated;
	
	public SunStatus() {
		// myBatis needs this
	}
	
	public SunStatus(String sunIpAddress, String sunId, DeviceStatus status) {
		this.sunId = sunId;
		this.sunIpAddress = sunIpAddress;
		this.sunStatus = status;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSunId() {
		return sunId;
	}

	public void setSunId(String sunId) {
		this.sunId = sunId;
	}

	public String getSunIpAddress() {
		return sunIpAddress;
	}

	public void setSunIpAddress(String sunIpAddress) {
		this.sunIpAddress = sunIpAddress;
	}

	public DeviceStatus getSunStatus() {
		return sunStatus;
	}

	public void setSunStatus(DeviceStatus sunStatus) {
		this.sunStatus = sunStatus;
	}

	public DateTime getLastUpdated() {
		return lastUpdated;
	}
	
	public DateTime getLastUpdatedUTC() {
		return lastUpdated.withZone(DateTimeZone.UTC);
	}

	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("sunId", sunId).add("sunIpAddress", sunIpAddress).add("sunStatus", sunStatus)
				.add("lastUpdated", lastUpdated).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, sunId, sunIpAddress, sunStatus, lastUpdated);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SunStatus other = (SunStatus) obj;
		return Objects.equal(this.id, other.id)
				&& Objects.equal(this.sunId, other.sunId)
				&& Objects.equal(this.sunIpAddress, other.sunIpAddress)
				&& Objects.equal(this.sunStatus, other.sunStatus)
				&& Objects.equal(this.lastUpdated, other.lastUpdated);
	}
}
