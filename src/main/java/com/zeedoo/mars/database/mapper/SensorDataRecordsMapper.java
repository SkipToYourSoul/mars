package com.zeedoo.mars.database.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.mars.domain.SensorDataRecord;

public interface SensorDataRecordsMapper extends Mapper {
	
	List<SensorDataRecord> get(@Param(value = "sensorId") String sensorId, 
			@Param(value = "start") Date start, @Param(value = "end") Date end);
	
	// Insert(update) the sensor data record with the given payload
	int insert(@Param(value = "record") SensorDataRecord record);
	
	// Delete data in the record
	int delete(@Param(value = "sensorId") String sensorId, 
			@Param(value = "start") Date start, @Param(value = "end") Date end);	
}
