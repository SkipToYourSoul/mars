package com.zeedoo.mars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.zeedoo.mars.dao.SensorDataRecordsDao;
import com.zeedoo.mars.event.SensorFileMergeEvent;

@Component
public class SensorFileMergeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorFileMergeService.class);
	
	@Autowired
	private SensorDataRecordsDao sensorDataRecordsDao;

	@ServiceActivator
	public void onSensorDataFileMergeEvent(SensorFileMergeEvent event) {
		LOGGER.info("Received SensorFileMergeEvent={}", event);
	}

}
