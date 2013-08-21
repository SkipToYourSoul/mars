package com.zeedoo.mars.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.mars.dao.SensorStatusDao;
import com.zeedoo.mars.dao.SunStatusDao;

@ManagedResource()
@Component
public class SunManagementServiceBean implements SunManagementService {
	
	@Autowired
	private SensorStatusDao sensorStatusDao;
	
	@Autowired
	private SunStatusDao sunStatusDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunManagementServiceBean.class);
		
	@Override
	public void onSunConnectionEstablished(String ipAddress, Integer port) {
		SunStatus newStatus = new SunStatus(ipAddress, port, null, DeviceStatus.ONLINE);
		LOGGER.info("OnSunConnectionEstablisehd: Setting SunStatus={} for ipAddress={}", newStatus, ipAddress);
		SunStatus existingStatus = sunStatusDao.getStatusBySocketAddress(ipAddress, port);
		if (existingStatus == null) {
			LOGGER.info("Could not find SunStatus with ipAddress={}, inserting a new SunStatus", ipAddress);
			sunStatusDao.insert(newStatus);
		} else {
			sunStatusDao.update(newStatus);
		}
	}
	
	@Override
	public void onSunConnectionInterrupted(String ipAddress, Integer port) {
		// Try to get the existing status first
		SunStatus status = sunStatusDao.getStatusBySocketAddress(ipAddress, port);
		SunStatus updatedStatus;
		if (status == null) {
			SunStatus newStatus = new SunStatus(ipAddress, port, null, DeviceStatus.OFFLINE);
			LOGGER.warn("Could not find existing SunStatus with ipAddress={}, inserting new entry={}", ipAddress, newStatus);
			updatedStatus = sunStatusDao.insert(newStatus);
		} else {
			//simply update the device status
			status.setSunStatus(DeviceStatus.OFFLINE);
			LOGGER.info("Updating SunStatus={}", status);
			updatedStatus = sunStatusDao.update(status);
		}
		// Mark sensors as offline
		markSensorsOffline(updatedStatus);
	}
	
	@Override
	public void onSunMessageReceived(String sunMacAddress, String ipAddress, Integer port) {
		// Set sunId for this ip address regardless if we have done it previously or not
		SunStatus status = sunStatusDao.getStatusBySocketAddress(ipAddress, port);
		if (status == null) {
			SunStatus newStatus = new SunStatus(ipAddress, port, sunMacAddress, DeviceStatus.ONLINE);
			LOGGER.warn("Could not find existing SunStatus with ipAddress={}, inserting new entry={}", ipAddress, newStatus);
			sunStatusDao.insert(newStatus);
		} else {
			// simply update the device status
			status.setSunMacAddress(sunMacAddress);
			LOGGER.info("Updating SunStatus={}", status);
			sunStatusDao.update(status);
		}
	}
	
	/**
	 * When the connection with a Sun device gets interrupted, we need to mark all sensors that share the same MAC address 
	 * with this Sun device as OFFLINE
	 * @param status
	 */
	private void markSensorsOffline(SunStatus status) {
		if (status == null) {
			return;
		}
		// Find all sensorStatus objects that share the same MAC address with this Sun device
		String macAddress = status.getSunMacAddress();
		if (!StringUtils.isEmpty(macAddress)) {
			List<SensorStatus> sensorStatusList = sensorStatusDao.findByMacAddress(macAddress);
			for (SensorStatus sensorStatus : sensorStatusList) {
				// Mark each sensor as offline
				sensorStatus.setSensorStatus(DeviceStatus.OFFLINE);
				sensorStatusDao.update(sensorStatus);
				LOGGER.info("Marked Sensor with SensorId={} as OFFLINE", sensorStatus.getSensorId());
			}
		} else {
			LOGGER.warn("Mac Address is NOT available for SunStatus={}. Unabled to mark sensors associated with this Sun device as offline", status);
		}
	}
}
