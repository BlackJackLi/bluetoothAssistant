/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gizwits.energy.android.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import org.xutils.x;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log。
 * tag自动产生，格式: customTag:className.methodName(L:lineNumber),
 * customTag为空时只输出：className.methodName(L:lineNumber)。
 * Author: wyouflf
 * Date: 13-7-24
 * Time: 下午12:23
 * modify: bj
 * Date 17-06-07
 * Time: 下午15:17
 */

public class LogUtils {

	private static long datedTime = 1000L * 60 * 60 * 24 * 60;//60天

	private LogUtils() {
	}

	private static String generateTag(String customTag) {
		StackTraceElement caller = new Throwable().getStackTrace()[2];
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(Locale.getDefault(),tag, callerClazzName, caller.getMethodName(), caller.getLineNumber(), Locale.getDefault());
		tag = TextUtils.isEmpty(customTag) ? tag : customTag + ":" + tag;
		return tag;
	}

	public static void d(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.d(tag, content);
	}

	public static void d(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.d(tag, content, tr);
	}

	public static void e(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.e(tag, content);
	}

	public static void e(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.e(tag, content, tr);
	}

	public static void i(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.i(tag, content);
	}

	public static void i(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.i(tag, content, tr);
	}

	public static void v(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.v(tag, content);
	}

	public static void v(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.v(tag, content, tr);
	}

	public static void w(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.w(tag, content);
	}

	public static void w(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.w(tag, content, tr);
	}

	public static void w(String customTag, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.w(tag, tr);
	}


	public static void wtf(String customTag, String content) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.wtf(tag, content);
	}

	public static void wtf(String customTag, String content, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.wtf(tag, content, tr);
	}

	public static void wtf(String customTag, Throwable tr) {
		if (!x.isDebug()) return;
		String tag = generateTag(customTag);

		Log.wtf(tag, tr);
	}

	public static boolean clearDatedLog(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			return false;
		} else {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			final long now = System.currentTimeMillis();
			File[] datedLogs = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					try {
						Date e = sdf.parse(filename.replaceAll(".log", ""));
						if (now - e.getTime() > datedTime) {
							return true;
						}
					} catch (ParseException var4) {
						var4.printStackTrace();
					}

					return false;
				}
			});
			File[] arr$ = datedLogs;
			int len$ = datedLogs.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				File datedLog = arr$[i$];
				FileUtils.deleteFile(datedLog);
			}

			return true;
		}
	}

	public static boolean log2File(String dirPath, final String message) {
		String dateStr = TimeUtil.format(new Date(), "yyyyMMdd");
		final String timeStr = TimeUtil.format(new Date(), "HH:mm:ss");
		String fileName = dateStr + ".log";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File logFile = new File(dir, fileName);
		try {
			BufferedWriter bow = new BufferedWriter(new FileWriter(logFile));
			String boundary = "========== " + timeStr + " ==========" + "\n";
			bow.write(boundary);
			bow.write(message);
			bow.newLine();
			bow.newLine();
			bow.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
