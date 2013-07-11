package com.zeedoo.mars.database.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.zeedoo.mars.domain.DeviceStatus;

/**
 * MyBatis type handler to convert data types between {@link DeviceStatusTypeHandler} and integer
 * @author nzhu
 *
 */
public class DeviceStatusTypeHandler implements TypeHandler<DeviceStatus> {
	
	@Override
	public void setParameter(PreparedStatement ps, int i,
			DeviceStatus parameter, JdbcType type) throws SQLException {
		ps.setInt(i, parameter.getStatusCode());
	}

	@Override
	public DeviceStatus getResult(ResultSet rs, String columnName)
			throws SQLException {
		return DeviceStatus.fromStatusCode(rs.getInt(columnName));
	}

	@Override
	public DeviceStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
		return DeviceStatus.fromStatusCode(rs.getInt(columnIndex));
	}

	@Override
	public DeviceStatus getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return DeviceStatus.fromStatusCode(cs.getInt(columnIndex));
	}
}
