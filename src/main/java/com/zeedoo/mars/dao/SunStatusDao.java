package com.zeedoo.mars.dao;

import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.SunStatus;

@Component
public class SunStatusDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunStatusDao.class);
	
	public SunStatus getStatusBySunId(String sunMacAddress) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(CoreApiPath.FIND_BY_SUN_MAC_ADDRESS.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("sunMacAddress", sunMacAddress);
		SunStatus result = coreApiClient.getWithQueryParams(uri.toASCIIString(), SunStatus.class, params);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with sunMacAddress={}, returning null", sunMacAddress);
		}
		return result;
	}
	
	public SunStatus getStatusBySocketAddress(String ipAddress, Integer port) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(ipAddress + ":" + port).build();
		SunStatus result = coreApiClient.get(uri.toASCIIString(), SunStatus.class);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with ipAddress={} and port={}, returning null", ipAddress, port);
		}
		return result;
	}
	
	public SunStatus insert(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), SunStatus.class, sunStatus);
	}
	
	public SunStatus update(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		Preconditions.checkNotNull("Port should not be null", sunStatus.getSunPort());
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(sunStatus.getSunIpAddress() + ":" + sunStatus.getSunPort()).build();
		SunStatus result = coreApiClient.put(uri.toASCIIString(), SunStatus.class, sunStatus);
		if (result == null) {
			LOGGER.warn("Updating SunStatus has returned a NULL entity with ipAddress={} and port={}", sunStatus.getSunIpAddress(), sunStatus.getSunPort());
		}
		return result;
	}
}
