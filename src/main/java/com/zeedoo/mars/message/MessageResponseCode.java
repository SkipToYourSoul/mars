package com.zeedoo.mars.message;

public enum MessageResponseCode {
		
	OK(0),
	SUN_INTERNAL_ERROR(-1),
	CORE_API_INTERNAL_ERROR(-2),
	MARS_INTERNAL_ERROR(-3),
	SENSOR_NOT_FOUND(-4);
	
	private Integer code;
	
	private MessageResponseCode(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return this.code;
	}
}
