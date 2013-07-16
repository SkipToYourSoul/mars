package com.zeedoo.mars.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zeedoo.mars.database.dao.SensorStatusDao;
import com.zeedoo.mars.domain.SensorStatus;

@ManagedResource()
@Component
public class SensorManagementServiceBean implements SensorManagementService {
	
	@Autowired
	private SensorStatusDao sensorStatusDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorManagementServiceBean.class);
	
	/**
	 * Creates a in-memory cache for sensor status
	 * Maps a SensorId String to a SensorStatus object
	 */
	private final Map<String, SensorStatus> cache = Maps.newConcurrentMap();
	
	/**
	 * Whether we read data from cache
	 * If set to false, we will directly read from DB and skip cache 
	 */
	private boolean readFromCacheFirst = true;
	
	@ManagedAttribute(description = "Whether we read data from cache first")
	public boolean isReadFromCacheFirst() {
		return readFromCacheFirst;
	}

	@ManagedAttribute(description = "Set whether we read data from cache first")
	public void setReadFromCacheFirst(boolean readFromCacheFirst) {
		this.readFromCacheFirst = readFromCacheFirst;
	}

	@Override
	public boolean updateSensorStatus(String sensorId,
			SensorStatus currentSensorStatus) {
		Preconditions.checkNotNull(sensorId, "current sensorId should not be null");
		Preconditions.checkNotNull(currentSensorStatus, "current sensorStatus should not be null");
		// Update database
		int result = sensorStatusDao.update(currentSensorStatus);
		if (result == 0) {
			LOGGER.info("Given sensorId={} currently does not exist in database, inserting a new entry with payload={}", sensorId, currentSensorStatus);
		}
		result = sensorStatusDao.insert(currentSensorStatus);
		// Update cache
		cache.put(sensorId, currentSensorStatus);
		return (result != 0);
	}

	@Override
	public SensorStatus getSensorStatus(String sensorId) {
		// Check cache first
		SensorStatus status = null;
		boolean foundInCache = true;
		if (readFromCacheFirst) {
		    status = cache.get(sensorId);
		    if (status != null) {
		    	return status;
		    } else {
		    	foundInCache = false;
		    	LOGGER.info("Could not find sensorId={} in cache, checking database...", sensorId);
		    }
		}
		// If we do not want to read from cache, or sensor is not found in cache, check DB
		if (!readFromCacheFirst || !foundInCache) {
			status = sensorStatusDao.get(sensorId);
		    if (status != null) {
		    	return status;
		    } else {
		    	LOGGER.warn("Could not find SensorId={} in either cache or database!");
		    }
		}
		return status;
	}
	
	@ManagedOperation(description = "Returns the current status with the given Sensor Id")
    public String getSensorStatusInfo(String sensorId) {
    	SensorStatus status = getSensorStatus(sensorId);
    	if (status == null) {
    		return String.format("Could not find Sensor with given sensorId=%s", sensorId);
    	} else {
    		return status.toString();
    	}
    }

}
