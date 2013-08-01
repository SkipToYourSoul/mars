package com.zeedoo.mars.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.SensorStatus;

/**
 * myBatis mapper for Sensor Staus
 * @author nzhu
 *
 */
public interface  SensorStatusMapper extends Mapper {
	
	SensorStatus get(@Param(value = "sensorId")String sensorId);
	
	int update(@Param(value = "sensorStatus") SensorStatus sensorStatus);
	
	int insert(@Param(value = "sensorStatus") SensorStatus sensorStatus);
	
	int delete(@Param(value = "sensorId") String sensorId);
}
