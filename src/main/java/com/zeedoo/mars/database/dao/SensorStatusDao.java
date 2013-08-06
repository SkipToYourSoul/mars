package com.zeedoo.mars.database.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.mars.api.CoreApiClient;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SensorStatusMapper;

@Component
public class SensorStatusDao extends EntityDao<SensorStatusMapper> {
	
	@Transactional
	public SensorStatus get(String sensorId) {
		//SensorStatusMapper mapper = getMapper();
		//return mapper.get(sensorId);
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorId).build();
		return (SensorStatus) coreApiClient.get(uri.toASCIIString(), SensorStatus.class);
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
