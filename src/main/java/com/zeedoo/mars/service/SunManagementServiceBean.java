package com.zeedoo.mars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.database.dao.SunStatusDao;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SunStatus;

@ManagedResource()
@Component
public class SunManagementServiceBean implements SunManagementService {
	
	@Autowired
	private SunStatusDao sunStatusDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorManagementServiceBean.class);
	
	@ManagedOperation
	public String querySunStatusByIpAddress(String ipAddress) {
		SunStatus status = sunStatusDao.getStatusByIpAddress(ipAddress);
		if (status == null) {
			String result = String.format("Could not find SunStatus by ipAddress=%s", ipAddress);
			return result;
		}
		return status.toString();
	}
	
	@ManagedOperation
	public String querySunStatusBySunId(String sunId) {
		SunStatus status = sunStatusDao.getStatusBySunId(sunId);
		if (status == null) {
			String result = String.format("Could not find SunStatus by SunId=%s", sunId);
			return result;
		}
		return status.toString();
	}
	
	@Override
	public SunStatus getSunStatusByIpAddress(String ipAddress) {
		return sunStatusDao.getStatusByIpAddress(ipAddress);
	}
	
	@Override
	public SunStatus getSunStatusBySunId(String sunId) {
		return sunStatusDao.getStatusBySunId(sunId);
	}
	
	@Override
	public void onSunConnectionEstablished(String ipAddress) {
		SunStatus newStatus = new SunStatus(ipAddress, null, DeviceStatus.ONLINE);
		LOGGER.info("OnSunConnectionEstablisehd: Setting SunStatus={} for ipAddress={}", newStatus, ipAddress);
		mergeSunStatus(newStatus);
	}
	
	@Override
	public void onSunConnectionInterrupted(String ipAddress) {
		// Try to get the existing status first
		SunStatus status = sunStatusDao.getStatusByIpAddress(ipAddress);
		if (status == null) {
			SunStatus newStatus = new SunStatus(ipAddress, null, DeviceStatus.OFFLINE);
			LOGGER.warn("Could not find existing SunStatus with ipAddress={}, inserting new entry={}", ipAddress, newStatus);
			sunStatusDao.insert(newStatus);
		} else {
			//simply update the device status
			status.setSunStatus(DeviceStatus.OFFLINE);
			LOGGER.info("Updating SunStatus={}", status);
			sunStatusDao.update(status);
		}
	}
	
	@Override
	public void onSunMessageReceived(String sunId, String ipAddress) {
		// Set sunId for this ip address regardless if we have done it previously or not
		SunStatus status = sunStatusDao.getStatusByIpAddress(ipAddress);
		if (status == null) {
			SunStatus newStatus = new SunStatus(ipAddress, sunId, DeviceStatus.ONLINE);
			LOGGER.warn("Could not find existing SunStatus with ipAddress={}, inserting new entry={}", ipAddress, newStatus);
			sunStatusDao.insert(newStatus);
		} else {
			// simply update the device status
			status.setSunId(sunId);
			LOGGER.info("Updating SunStatus={}", status);
			sunStatusDao.update(status);
		}
	}
		
	// Merge a Sun status
	// Update first, if update doesn't return a result, do an insert
	private void mergeSunStatus(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus, "sunStatus should not be null");
		Preconditions.checkNotNull(sunStatus.getSunIpAddress(), "sunIpAddress should not be null");
		Preconditions.checkNotNull(sunStatus.getSunStatus(), "sunStatus should not have a null device status");
		int result = sunStatusDao.update(sunStatus);
		if (result == 0) {
			LOGGER.debug("SunStatus={} does not exist in database, inserting new entry", sunStatus);
			result = sunStatusDao.insert(sunStatus);
		}
	}
}
