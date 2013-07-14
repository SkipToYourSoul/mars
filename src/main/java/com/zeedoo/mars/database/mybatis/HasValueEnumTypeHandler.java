package com.zeedoo.mars.database.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.zeedoo.mars.domain.HasValueEnum;

public class HasValueEnumTypeHandler<E extends Enum<E> & HasValueEnum> extends BaseTypeHandler<E> {

	private Class<E> type;
	private final E[] enums;

	public HasValueEnumTypeHandler(Class<E> type) {
		if (type == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null)
			throw new IllegalArgumentException(type.getSimpleName()
					+ " does not represent an enum type.");
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
			JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		int value = rs.getInt(columnName);
	    if (rs.wasNull()) {
	      return null;
	    }
	    for (E enm : enums) {
	      if (value == enm.getValue()) {
	        return enm;
	      }
	    }
	    throw new IllegalArgumentException("Cannot convert "
	      + value + " to " + type.getSimpleName());
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		int value = rs.getInt(columnIndex);
	    if (rs.wasNull()) {
	      return null;
	    }
	    for (E enm : enums) {
	      if (value == enm.getValue()) {
	        return enm;
	      }
	    }
	    throw new IllegalArgumentException("Cannot convert "
	      + value + " to " + type.getSimpleName());
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		int value = cs.getInt(columnIndex);
	    if (cs.wasNull()) {
	      return null;
	    }
	    for (E enm : enums) {
	      if (value == enm.getValue()) {
	        return enm;
	      }
	    }
	    throw new IllegalArgumentException("Cannot convert "
	      + value + " to " + type.getSimpleName());
	}

}
