package com.zeedoo.mars.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.Sun;

/**
 * MyBatis mapper to interact with dbo.Sun
 * @author nzhu
 *
 */
public interface SunMapper extends Mapper {
	
	Sun get(@Param(value = "sunId") String sunId);
	
	int insert(@Param(value = "sun") Sun sun);
	
	int update(@Param(value = "sun") Sun sun);
}
