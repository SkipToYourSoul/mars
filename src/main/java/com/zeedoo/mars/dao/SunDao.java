package com.zeedoo.mars.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.FindByResult;
import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.commons.domain.Sun;

@Component
public class SunDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunDao.class);
	
	public Sun getSunBySunId(String sunId) {
		Preconditions.checkNotNull(sunId != null, "Sun Id should not be null");
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).path("id").path(sunId).build();
		Sun result = coreApiClient.get(uri.toASCIIString(), Sun.class);
		if (result == null) {
			LOGGER.warn("Could not find Sun with sunId={}. Returning NULL", sunId);
		}
		return result;
	}
	
	public Sun getSunByMacAddress(String macAddress) {
		Preconditions.checkNotNull(macAddress != null, "MAC Address should not be null");
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).path("macAddress").path(macAddress).build();
		Sun result = coreApiClient.get(uri.toASCIIString(), Sun.class);
		if (result == null) {
			LOGGER.warn("Could not find Sun with MAC Address={}. Returning NULL", macAddress);
		}
		return result;
	}
	
	public Sun getSunBySocketAddress(String ipAddress, Integer port) {
		Preconditions.checkNotNull(ipAddress != null, "IP Address should not be null");
		Preconditions.checkNotNull(port != null, "Port should not be null");
		String socketAddress = ipAddress + ":" + port;
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).path("socketAddress").path(socketAddress).build();
		Sun result = coreApiClient.get(uri.toASCIIString(), Sun.class);
		if (result == null) {
			LOGGER.warn("Could not find Sun with ipAddress={} and port={}. Returning NULL", ipAddress, port);
		}
		return result;
	}
	
	//FIXME: Use new API endpoint
	public List<Sun> getAllSuns() {
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).build();
		FindByResult result = coreApiClient.get(uri.toASCIIString(), FindByResult.class);
		ArrayList<Sun> suns  = Lists.newArrayListWithCapacity(result.getLinks().size());
		// Resolve links
		for (String url : result.getLinks()) {
			Sun sun = coreApiClient.get(url, Sun.class);
			suns.add(sun);
		}
		return suns;
	}
	
	public Sun insert(Sun sun) {
		Preconditions.checkNotNull(sun != null, "Sun should not be null");
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), Sun.class, sun);
	}
	
	public Sun update(Sun sun) {
		Preconditions.checkNotNull(sun != null, "Sun should not be null");
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN.getPath()).build();
		Sun result = coreApiClient.put(uri.toASCIIString(), Sun.class, sun);
		if (result == null) {
			LOGGER.warn("Updating Sun has returned a NULL entity with sun entity={}", sun);
		}
		return result;
	}	
}
