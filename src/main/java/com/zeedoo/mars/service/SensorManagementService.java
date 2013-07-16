package com.zeedoo.mars.service;

import com.zeedoo.mars.domain.SensorStatus;

/**
 * 
 * Service that maintains Sensor-related data/states
 * @author nzhu
 *
 */
public interface SensorManagementService {
	
	/**
	 * Updates the device status of a Sensor in cache as well as database
	 * If the given sensorId does not exist, insert it in the DB
	 * @param sensorId
	 * @param currentStatus
	 * @return
	 */
	boolean updateSensorStatus(String sensorId, SensorStatus currentSensorStatus);
	
	/**
	 * Gets the status of the Sensor from the in-memory cache.
	 * If not found in cache, retrieve it from the database
	 * @param sensorId
	 * @return
	 */
	SensorStatus getSensorStatus(String sensorId);

}
