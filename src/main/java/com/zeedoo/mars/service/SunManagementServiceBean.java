package com.zeedoo.mars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.database.dao.SunStatusDao;
import com.zeedoo.mars.domain.DeviceStatus;
import com.zeedoo.mars.domain.SunStatus;

@Component
public class SunManagementServiceBean implements SunManagementService {
	
	@Autowired
	private SunStatusDao sunStatusDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorManagementServiceBean.class);
				
	@Override
	public SunStatus getSunStatusByIpAddress(String ipAddress) {
		return sunStatusDao.getStatusByIpAddress(ipAddress);
	}
	
	@Override
	public SunStatus getSunStatusBySunId(String sunId) {
		return sunStatusDao.getStatusBySunId(sunId);
	}
	
	@Override
	public boolean onSunConnectionEstablished(String ipAddress) {
		SunStatus newStatus = new SunStatus(ipAddress, null, DeviceStatus.ONLINE);
		if (mergeSunStatus(newStatus) == false) {
			LOGGER.error("Failed to set new SunStatus={} for ipAddress = {}", newStatus, ipAddress);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onSunConnectionInterrupted(String ipAddress) {
		SunStatus newStatus = new SunStatus(ipAddress, null, DeviceStatus.OFFLINE);
		if (mergeSunStatus(newStatus) == false) {
			LOGGER.error("Failed to set new SunStatus={} for ipAddress = {}", newStatus, ipAddress);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onSunMessageReceived(String sunId, String ipAddress) {
		// Set sunId for this ip address regardless if we have done it previously or not
		SunStatus newStatus = new SunStatus(ipAddress, sunId, DeviceStatus.ONLINE);
		if (mergeSunStatus(newStatus) == false) {
			LOGGER.error("Failed to set new SunStatus={} for ipAddress = {}", newStatus, ipAddress);
			return false;
		}
		return true;
	}
		
	// Merge a Sun status
	// Update first, if update doesn't return a result, do an insert
	private boolean mergeSunStatus(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus, "sunStatus should not be null");
		Preconditions.checkNotNull(sunStatus.getSunIpAddress(), "sunIpAddress should not be null");
		Preconditions.checkNotNull(sunStatus.getSunStatus(), "sunStatus should not have a null device status");
		int result = sunStatusDao.update(sunStatus);
		if (result == 0) {
			LOGGER.debug("SunStatus={} does not exist in database, inserting new entry", sunStatus);
			result = sunStatusDao.insert(sunStatus);
		}
		return (result != 0);
	}

}
