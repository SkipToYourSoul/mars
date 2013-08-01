package com.zeedoo.mars.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.SunStatus;

/**
 * myBatis mapper to interact with dbo.sun_alive_state
 * @author nzhu
 *
 */
public interface SunStatusMapper extends Mapper {
	
	SunStatus getStatusByIpAddress(@Param(value = "ipAddress") String ipAddress);
	
	SunStatus getStatusBySunId(@Param(value = "sunId") String sunId);
	
	int insert(@Param(value = "sunStatus") SunStatus sunStatus);
	
	int update(@Param(value = "sunStatus") SunStatus sunStatus);
}
