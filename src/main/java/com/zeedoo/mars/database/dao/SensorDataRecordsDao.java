package com.zeedoo.mars.database.dao;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SensorDataRecordsMapper;
import com.zeedoo.mars.domain.SensorDataRecord;

@Component
public class SensorDataRecordsDao extends EntityDao<SensorDataRecordsMapper> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataRecordsDao.class);
	
	// Utility method to insert a list of records
	public void insertDataRecords(List<SensorDataRecord> records) {
		Preconditions.checkArgument(records != null, "Records should not be null");
		//Insert record one by one, if there is an exception, we suppress it and disregard it, since it's unlikely that a 
		//single data record would affect the overall data look. However, we do need to be alerted if there's high number
		//of failures
		for (SensorDataRecord record : records) {
			try {
                insert(record);
			} catch (Exception e) {
				LOGGER.error("Error occured while inserting a record: {}, Skipping SensorDataRecord={}", e, record);
			}
		}
	}
	
	@Transactional
	public List<SensorDataRecord> get(String sensorId, DateTime start, DateTime end) {
		Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
		SensorDataRecordsMapper mapper = getMapper();
		Date startDate = start != null? start.toDate() : null;
		Date endDate = end != null? end.toDate() : null;
		return mapper.get(sensorId, startDate, endDate);
	}
	
	@Transactional
	public int insert(SensorDataRecord record) {
		Preconditions.checkArgument(record != null, "Record should not be null");
		SensorDataRecordsMapper mapper = getMapper();
		return mapper.insert(record);
	}
	
	// For testing purposes
	@Transactional
	public int delete(String sensorId, DateTime start, DateTime end) {
    	Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
    	SensorDataRecordsMapper mapper = getMapper();
    	Date startDate = start != null? start.toDate() : null;
		Date endDate = end != null? end.toDate() : null;
		return mapper.delete(sensorId, startDate, endDate);
	}

	@Override
	protected SensorDataRecordsMapper getMapper() {
		return databaseService.getMapper(SensorDataRecordsMapper.class);
	}

}
