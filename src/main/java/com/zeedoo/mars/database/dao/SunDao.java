package com.zeedoo.mars.database.dao;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.mars.database.aop.Transactional;
import com.zeedoo.mars.database.mapper.SunMapper;
import com.zeedoo.mars.domain.Sun;

/**
 * DAO to interact with dbo.Sun
 */
@Component
public class SunDao extends EntityDao<SunMapper> {
			
	@Transactional
	public Sun get(String sunId) {
		Preconditions.checkArgument(sunId != null, "Sun ID should not be null");
		SunMapper mapper = getMapper();
		return mapper.get(sunId);
	}
	
	@Transactional
	public int insert(Sun sun) {
		Preconditions.checkArgument(sun != null, "Sun entity should not be null");
		SunMapper mapper = getMapper();
		return mapper.insert(sun);
	}
	
	@Transactional
	public int update(Sun sun) {
		Preconditions.checkArgument(sun != null, "Sun entity should not be null");
		SunMapper mapper = getMapper();
		return mapper.update(sun);
	}

	@Override
	protected SunMapper getMapper() {
		return databaseService.getMapper(SunMapper.class);
	}


}
