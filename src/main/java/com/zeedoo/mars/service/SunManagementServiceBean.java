package com.zeedoo.mars.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Objects;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.commons.domain.Sun;
import com.zeedoo.mars.dao.SensorDao;
import com.zeedoo.mars.dao.SunDao;

@Component
public class SunManagementServiceBean implements SunManagementService {
	
	@Autowired
	private SensorDao sensorDao;
	
	@Autowired
	private SunDao sunDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunManagementServiceBean.class);
		
	@Override
	public void onSunConnectionEstablished(String ipAddress, Integer port) {
		/*Sun newSun = new Sun(ipAddress, port, null, DeviceStatus.ONLINE);
		 LOGGER.info("OnSunConnectionEstablisehd: Setting Sun={} for ipAddress={}", newSun, ipAddress);
		Sun existing = sunDao.getSunBySocketAddress(ipAddress, port);
		if (existing == null) {
			LOGGER.info("Could not find Sun with ipAddress={} and port={}, inserting a new Sun", ipAddress, port);
			sunDao.insert(newSun);
		} else {
			sunDao.update(newSun);
		} */
	}
	
	@Override
	public void onSunConnectionInterrupted(String ipAddress, Integer port) {
		// Try to get the existing sun first
		Sun sun = sunDao.getSunBySocketAddress(ipAddress, port);
		Sun updatedSun;
		if (sun == null) {
			LOGGER.warn("Could not find existing Sun with ipAddress={} and port={}", ipAddress, port);
		} else {
			if (StringUtils.isEmpty(sun.getMacAddress())) {
				LOGGER.warn("Existing Sun ={} has EMPTY MAC Address, skipping update", sun);
				return;
			}
			//simply update the device sun
			sun.setStatus(DeviceStatus.OFFLINE);
			LOGGER.info("Updating Sun={}", sun);
			updatedSun = sunDao.update(sun);
			// Mark sensors as offline
			markSensorsOffline(updatedSun);
		}
	}
	
	@Override
	public void onSunMessageReceived(String sunMacAddress, String ipAddress, Integer port) {
		Sun sun = sunDao.getSunByMacAddress(sunMacAddress);
		if (sun == null) {
			Sun newSun = new Sun(ipAddress, port, sunMacAddress, DeviceStatus.ONLINE);
			LOGGER.warn("Could not find existing Sun with macAddress={}, inserting new entry={}", sunMacAddress, newSun);
			sunDao.insert(newSun);
		} else {
			if (isStateDifferent(ipAddress, port, sun)) {
				// We have received a message so set status to online
				sun.setStatus(DeviceStatus.ONLINE);
				sun.setCurrentIpAddress(ipAddress);
				sun.setCurrentPort(port);
				LOGGER.info("Updating Sun={}", sun);
				sunDao.update(sun);
			}
		}
	}

	/**
	 * Checks if MAC address or socket address or online status is different
	 * @param ipAddress
	 * @param port
	 * @param sun
	 * @return boolean
	 */
	private boolean isStateDifferent(String ipAddress, Integer port, Sun sun) {
		return !Objects.equal(ipAddress, sun.getCurrentIpAddress())
				|| !Objects.equal(port, sun.getCurrentPort())
				|| !Objects.equal(DeviceStatus.ONLINE, sun.getStatus());
	}
	
	/**
	 * When the connection with a Sun device gets interrupted, we need to mark all sensors that share the same MAC address 
	 * with this Sun device as OFFLINE
	 * @param sun
	 */
	private void markSensorsOffline(Sun sun) {
		if (sun == null) {
			return;
		}
		// Find all sensor objects that share the same MAC address with this Sun device
		String macAddress = sun.getMacAddress();
		if (!StringUtils.isEmpty(macAddress)) {
			List<Sensor> sensorList = sensorDao.findSensors(macAddress, null);
			for (Sensor sensor : sensorList) {
				// Mark each sensor as offline
				sensor.setSensorStatus(DeviceStatus.OFFLINE);
				sensorDao.update(sensor);
				LOGGER.info("Marked Sensor with SensorId={} as OFFLINE", sensor.getSensorId());
			}
		} else {
			LOGGER.warn("Mac Address is NOT available for Sun={}. Unabled to mark sensors associated with this Sun device as offline", sun);
		}
	}
}
