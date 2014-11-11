package com.zeedoo.mars.message;

public enum MessageResponseCode {
		
	// Success for an individual file transfer
	FILE_TRANSFER_SUCCESS(2),
	OK(1),
	SUN_INTERNAL_ERROR(-1),
	CORE_API_INTERNAL_ERROR(-2),
	MARS_INTERNAL_ERROR(-3),
	SENSOR_NOT_FOUND(-4),
	// Mars indicates file transfer is not necessary
	FILE_TRANSFER_NOT_NECESSARY(-5),
	// File sensor file info error
	FILE_TRANSFER_SENSOR_FILE_INFO_ERROR(-6),
	// Request Sun to re-send the entire file
	FILE_TRANSFER_RESEND_COMPLETE_FILE(-7),
	// Different MD5
	FILE_TRANSFER_MD5_INCORRECT(-8),
	// Different CRC16
	FILE_TRANSFER_CRC32_INCORRECT(-9),
	// Sun has aborted the transfer
	SENSOR_DATA_TRANFSER_ABORTED(-10),
	// Data validation error
	DATA_VALIDATION_ERROR(-11);
	
	private Integer code;
	
	private MessageResponseCode(Integer code) {
		this.code = code;
	}
	
	public String getCode() {
		return String.valueOf(this.code);
	}
}
