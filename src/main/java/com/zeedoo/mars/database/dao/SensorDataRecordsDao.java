package com.zeedoo.mars.database.dao;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.mars.database.aop.Transactional;

@Component
public class SensorDataRecordsDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataRecordsDao.class);
	
	// Insert a list of records
	// Note: This should be the preferred way to insert multiple records for performance reasons
	@Transactional
	public int insertDataRecords(List<SensorDataRecord> records) {
		//Preconditions.checkArgument(records != null, "Records should not be null");
		//SensorDataRecordsMapper mapper = getMapper();
		//return mapper.insertDataRecords(records);
		return 0;
	}
	
	@Transactional
	public List<SensorDataRecord> get(String sensorId, DateTime start, DateTime end) {
		//Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
		//SensorDataRecordsMapper mapper = getMapper();
		//Date startDate = start != null? start.toDate() : null;
		//Date endDate = end != null? end.toDate() : null;
		//return mapper.get(sensorId, startDate, endDate);
		return Lists.newLinkedList();
	}
	
	@Transactional
	public int insert(SensorDataRecord record) {
		Preconditions.checkArgument(record != null, "Record should not be null");
		//return mapper.insert(record);
		return 1;
	}
	
	// For testing purposes
	@Transactional
	public int delete(String sensorId, DateTime start, DateTime end) {
    	Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
    	Date startDate = start != null? start.toDate() : null;
		Date endDate = end != null? end.toDate() : null;
		//return mapper.delete(sensorId, startDate, endDate);
		return 1;
	}
}
