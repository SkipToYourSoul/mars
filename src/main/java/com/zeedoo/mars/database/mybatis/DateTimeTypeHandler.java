package com.zeedoo.mars.database.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;

/**
 * MyBatis {@link DateTime} handler to convert MySQL UNIX timestamp <-> Joda DateTime objects
 * 
 * @author nzhu
 *
 */
public class DateTimeTypeHandler implements TypeHandler<DateTime> {
	
	@Override
	public void setParameter(PreparedStatement ps, int i, DateTime parameter,
			JdbcType jdbcType) throws SQLException {
		if (parameter != null) {
			// Divide by 1000 to get to Unix epoch time
			ps.setLong(i, parameter.getMillis() / 1000L);
		} else {
			ps.setTimestamp(i, null);
		}
	}

	@Override
	public DateTime getResult(ResultSet rs, String columnName)
			throws SQLException {
		long timestamp = rs.getLong(columnName);
		return new DateTime(timestamp * 1000L);
	}

	@Override
	public DateTime getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		long timestamp = rs.getLong(columnIndex);
		// this returns a UTC time automatically
		return new DateTime(timestamp * 1000L);
	}

	@Override
	public DateTime getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		long timestamp = cs.getLong(columnIndex);
		return new DateTime(timestamp * 1000L);
	}

}
