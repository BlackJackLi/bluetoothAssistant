package com.github.black.bluetooth_assistant.common;

import android.os.Environment;

public class MyConstant {

	public static final String PATH;
	public static final String WEB_LOG_DIR = "/log_web";
	public static final String CRASH_LOG_DIR = "/log_crash";

	static {
		PATH = Environment.getExternalStorageDirectory().getPath() + "/bluetoothAssistant";
	}
}
