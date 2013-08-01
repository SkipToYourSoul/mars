package com.zeedoo.mars.database.dao;

import org.springframework.stereotype.Component;

import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SensorStatusMapper;

@Component
public class SensorStatusDao extends EntityDao<SensorStatusMapper> {
	
	@Transactional
	public SensorStatus get(String sensorId) {
		SensorStatusMapper mapper = getMapper();
		return mapper.get(sensorId);
	}
	
	@Transactional
	public int update(SensorStatus sensorStatus) {
		SensorStatusMapper mapper = getMapper();
		return mapper.update(sensorStatus);
	}
	
	@Transactional
	public int insert(SensorStatus sensorStatus) {
		SensorStatusMapper mapper = getMapper();
		return mapper.insert(sensorStatus);
	}
	
	@Transactional
	public int delete(String sensorId) {
		SensorStatusMapper mapper = getMapper();
		return mapper.delete(sensorId);
	}

	@Override
	protected SensorStatusMapper getMapper() {
		return databaseService.getMapper(SensorStatusMapper.class);
	}

}
