package com.zeedoo.mars.domain;

import com.google.common.base.Objects;

/**
 * Represents a Sun system
 * @author nzhu
 *
 */
public class Sun {
	
	// Internal DB id
	String id;
	
	// External Sun Id
	String sunId;
	
	// Sun SSID
	String sunSsid;
	
	// Sun geocode (lat,lng)
	String geoCode;
	
	// Human-readable Sun location
	String location;
	
	// Sun wireless access code
	String wirelessCode;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSunId() {
		return sunId;
	}
	
	public String getSunSsid() {
		return sunSsid;
	}

	public void setSunSsid(String sunSsid) {
		this.sunSsid = sunSsid;
	}
	
	public void setSunId(String sunId) {
		this.sunId = sunId;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWirelessCode() {
		return wirelessCode;
	}

	public void setWirelessCode(String wirelessCode) {
		this.wirelessCode = wirelessCode;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("sunId", sunId).add("sunSsid", sunSsid).add("geoCode", geoCode).add("location", location).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, sunId, sunSsid, geoCode, location, wirelessCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sun other = (Sun) obj;
		return Objects.equal(id, other.id) && Objects.equal(sunId, other.sunId) && Objects.equal(geoCode, other.geoCode)
				&& Objects.equal(location, other.location) && Objects.equal(sunSsid, other.sunSsid) && Objects.equal(wirelessCode, other.wirelessCode);
	}
}
