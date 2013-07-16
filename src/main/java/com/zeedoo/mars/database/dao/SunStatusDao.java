package com.zeedoo.mars.database.dao;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SunStatusMapper;
import com.zeedoo.mars.domain.SunStatus;

@Component
public class SunStatusDao extends EntityDao<SunStatusMapper> {
	
	@Transactional
	public SunStatus getStatusBySunId(String sunId) {
		SunStatusMapper mapper = getMapper();
		Preconditions.checkNotNull("SunId should not be null", sunId);
		return mapper.getStatusBySunId(sunId);
	}
	
	@Transactional
	public SunStatus getStatusByIpAddress(String ipAddress) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		SunStatusMapper mapper = getMapper();
		return mapper.getStatusByIpAddress(ipAddress);
	}
	
	@Transactional
	public int insert(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		SunStatusMapper mapper = getMapper();
		return mapper.insert(sunStatus);
	}
	
	@Transactional
	public int update(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		SunStatusMapper mapper = getMapper();
		return mapper.update(sunStatus);
	}
	
	@Override
	protected SunStatusMapper getMapper() {
		return databaseService.getMapper(SunStatusMapper.class);
	}

}
