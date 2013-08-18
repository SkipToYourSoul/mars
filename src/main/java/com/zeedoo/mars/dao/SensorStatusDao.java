package com.zeedoo.mars.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.SensorStatus;

@Component
public class SensorStatusDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorStatusDao.class);
	
	public SensorStatus get(String sensorId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorId).build();
		SensorStatus result = coreApiClient.get(uri.toASCIIString(), SensorStatus.class);
		if (result == null) {
			LOGGER.warn("Could not find SensorStatus with sensorId={}. Returning NULL", sensorId);
		}
		return result;
	}

	public SensorStatus update(SensorStatus sensorStatus) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorStatus.getSensorId()).build();
		SensorStatus result = coreApiClient.put(uri.toASCIIString(), SensorStatus.class, sensorStatus);
		if (result == null) {
			LOGGER.warn("Updating SensorStatus has returned a NULL entity with sensorId={}", sensorStatus.getSensorId());
		}
		return result;
	}
	
	public SensorStatus insert(SensorStatus sensorStatus) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), SensorStatus.class, sensorStatus);
	}
	
	public boolean delete(String sensorId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR_STATUS.getPath()).path(sensorId).build();
		ClientResponse response = coreApiClient.delete(uri.toASCIIString());
		if (Status.OK.equals(response.getClientResponseStatus()))
		{
			LOGGER.debug("Sensor sensorId={} successfully deleted");
			return true;
		}
		return false;
	}
}
