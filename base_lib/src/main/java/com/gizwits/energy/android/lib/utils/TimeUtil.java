package com.gizwits.energy.android.lib.utils;

/**
 * Created by Black on 2017/5/25 0025.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
	private TimeUtil() {
	}

	public static Date parse(String time, String pattern) {
		try {
			SimpleDateFormat e = new SimpleDateFormat(pattern, Locale.getDefault());
			return e.parse(time);
		} catch (Exception var3) {
			return null;
		}
	}

	public static String format(long time, String pattern) {
		return format(new Date(time), pattern);
	}

	public static String format(Date date, String pattern) {
		try {
			SimpleDateFormat e = new SimpleDateFormat(pattern, Locale.getDefault());
			return e.format(date);
		} catch (Exception var3) {
			return "";
		}
	}
}
