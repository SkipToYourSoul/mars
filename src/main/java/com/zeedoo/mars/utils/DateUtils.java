package com.zeedoo.mars.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class DateUtils {
	
	private DateUtils() {
	}
	
	public static Date fromDateTime(DateTime dateTime) {
		return dateTime.toDate();
	}

	public static DateTime nowDateUTC() throws Exception {
		return DateTime.now(DateTimeZone.UTC);
	}

}
