package com.zeedoo.mars.domain;

public enum DeviceStatus implements HasValueEnum {

	ONLINE(1), 
	OFFLINE(0);

	private int statusCode;

	DeviceStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public static DeviceStatus fromStatusCode(int statusCode) {
		switch (statusCode) {
		case 0:
			return DeviceStatus.OFFLINE;
		case 1:
			return DeviceStatus.ONLINE;
		default:
			throw new IllegalArgumentException(
					"Unknown DeviceStatus status code:" + statusCode);
		}
	}

	@Override
	public int getValue() {
		return this.statusCode;
	}

}
