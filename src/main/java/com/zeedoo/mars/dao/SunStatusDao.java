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
	
	public SunStatus getStatusBySunId(String sunId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(CoreApiPath.FIND_BY_SUN_ID.getPath()).build();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("sunId", sunId);
		SunStatus result = coreApiClient.getWithQueryParams(uri.toASCIIString(), SunStatus.class, params);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with sunId={}, returning null", sunId);
		}
		return result;
	}
	
	public SunStatus getStatusByIpAddress(String ipAddress) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(ipAddress).build();
		SunStatus result = coreApiClient.get(uri.toASCIIString(), SunStatus.class);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with ipAddress={}, returning null", ipAddress);
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
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(sunStatus.getSunIpAddress()).build();
		SunStatus result = coreApiClient.put(uri.toASCIIString(), SunStatus.class, sunStatus);
		if (result == null) {
			LOGGER.warn("Updating SunStatus has returned a NULL entity with ipAddress={}", sunStatus.getSunIpAddress());
		}
		return result;
	}
}
