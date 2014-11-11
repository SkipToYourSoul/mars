package com.zeedoo.mars.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.FindByResult;
import com.zeedoo.commons.domain.Link;
import com.zeedoo.commons.domain.Paginator;
import com.zeedoo.commons.domain.Sensor;

@Component
public class SensorDao extends EntityDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDao.class);
	
	public Sensor get(String sensorId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).path(sensorId).build();
		Sensor result = coreApiClient.get(uri.toASCIIString(), Sensor.class);
		if (result == null) {
			LOGGER.warn("Could not find Sensor with sensorId={}. Returning NULL", sensorId);
		}
		return result;
	}
	
	@Deprecated
	public List<Sensor> findSensorsLegacy(String macAddress, String username) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (macAddress != null) {
			params.add("sunMacAddress", macAddress);
		}
		if (username != null) {
			params.add("username", username);
		}
		// Get a list of links by mac address
		FindByResult result = coreApiClient.getWithQueryParams(uri.toASCIIString(), FindByResult.class, params);
		ArrayList<Sensor> sensorList = Lists.newArrayListWithCapacity(result.getLinks().size());
		// Resolve links
		for (String url : result.getLinks()) {
			Sensor sensor = coreApiClient.get(url, Sensor.class);
			sensorList.add(sensor);
		}
		return sensorList;
	}
	
	public List<Sensor> findSensors(String macAddress, String username) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).path(CoreApiPath.FIND_BY.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (macAddress != null) {
			params.add("sunMacAddress", macAddress);
		}
		if (username != null) {
			params.add("username", username);
		}
		// Get the paginator
		Paginator paginator = coreApiClient.getWithQueryParams(uri.toASCIIString(), Paginator.class, params);
		List<Link> links = resolveObjectsFromPaginator(paginator, Link.class);
		return resolveLinks(links, Sensor.class);
	}

	public Sensor update(Sensor sensor) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).path(sensor.getSensorId()).build();
		Sensor result = coreApiClient.put(uri.toASCIIString(), Sensor.class, sensor);
		if (result == null) {
			LOGGER.warn("Updating Sensor has returned a NULL entity with sensorId={}", sensor.getSensorId());
		}
		return result;
	}
	
	public Sensor insert(Sensor sensor) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), Sensor.class, sensor);
	}
	
	public boolean delete(String sensorId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).path(sensorId).build();
		if (coreApiClient.delete(uri.toASCIIString()))
		{
			LOGGER.debug("Sensor sensorId={} successfully deleted");
			return true;
		}
		return false;
	}
}
