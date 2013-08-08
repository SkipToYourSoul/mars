package com.zeedoo.mars.database.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SensorStatusMapper;

@Component
public class SensorStatusDao extends EntityDao<SensorStatusMapper> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorStatusDao.class);
	
	@Transactional
	public SensorStatus get(String sensorId) {
		//SensorStatusMapper mapper = getMapper();
		//return mapper.get(sensorId);
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorId).build();
		return coreApiClient.get(uri.toASCIIString(), SensorStatus.class);
	}
	
	@Transactional
	public SensorStatus update(SensorStatus sensorStatus) {
		//SensorStatusMapper mapper = getMapper();
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorStatus.getSensorId()).build();
		//return mapper.update(sensorStatus);
		return coreApiClient.put(uri.toASCIIString(), SensorStatus.class, sensorStatus);
	}
	
	@Transactional
	public SensorStatus insert(SensorStatus sensorStatus) {
		//SensorStatusMapper mapper = getMapper();
		//return mapper.insert(sensorStatus);
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), SensorStatus.class, sensorStatus);
	}
	
	@Transactional
	public boolean delete(String sensorId) {
		//SensorStatusMapper mapper = getMapper();
		//return mapper.delete(sensorId);
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorId).build();
		ClientResponse response = coreApiClient.delete(uri.toASCIIString());
		if (Status.OK.equals(response.getClientResponseStatus()))
		{
			LOGGER.debug("Sensor sensorId={} successfully deleted");
			return true;
		}
		return false;
	}

	@Override
	protected SensorStatusMapper getMapper() {
		return databaseService.getMapper(SensorStatusMapper.class);
	}

}
