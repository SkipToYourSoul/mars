package com.zeedoo.mars.database.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.mars.database.aop.Transactional;

@Component
public class SunStatusDao extends EntityDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunStatusDao.class);
	
	@Transactional
	public SunStatus getStatusBySunId(String sunId) {
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(CoreApiPath.FIND_BY_SUN_ID.getPath())
				.queryParam("sunId", sunId).build();
		SunStatus result = coreApiClient.get(uri.toASCIIString(), SunStatus.class);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with sunId={}, returning null", sunId);
		}
		return result;
	}
	
	@Transactional
	public SunStatus getStatusByIpAddress(String ipAddress) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).path(ipAddress).build();
		SunStatus result = coreApiClient.get(uri.toASCIIString(), SunStatus.class);
		if (result == null) {
			LOGGER.warn("Could not find SunStatus with ipAddress={}, returning null", ipAddress);
		}
		return result;
	}
	
	@Transactional
	public SunStatus insert(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		URI uri = UriBuilder.fromPath(CoreApiPath.SUN_STATUS.getPath()).build();
		return coreApiClient.post(uri.toASCIIString(), SunStatus.class, sunStatus);
	}
	
	@Transactional
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
