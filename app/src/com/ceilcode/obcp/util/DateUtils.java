package com.ceilcode.obcp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
	public static final String DD_MMM_YYY = "dd-MMM-yyyy";
	public static final String MM_dd_yyyy = "MM/dd/yyyy";

	public static String formatDate(int day, int month, int year,
			String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		return format.format(calendar.getTime());
	}

	public static String formatDate(Calendar calendar, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
		return format.format(calendar.getTime());
	}
}
