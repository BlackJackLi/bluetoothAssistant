package com.github.black.bluetooth_assistant;

import android.os.Build;

import com.github.black.bluetooth_assistant.common.MyConstant;
import com.gizwits.energy.android.lib.base.BaseApplication;
import com.gizwits.energy.android.lib.utils.LogUtils;

import org.xutils.common.task.AbsTask;
import org.xutils.x;

public class MyApplication extends BaseApplication implements Thread.UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		x.Ext.setDebug(true);

		mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

		x.task().start(new AbsTask<Object>() {
			@Override
			protected Object doBackground() throws Throwable {
				//清除过期log
				LogUtils.clearDatedLog(MyConstant.PATH + MyConstant.WEB_LOG_DIR);
				LogUtils.clearDatedLog(MyConstant.PATH + MyConstant.CRASH_LOG_DIR);
				return null;
			}

			@Override
			protected void onSuccess(Object result) {

			}

			@Override
			protected void onError(Throwable ex, boolean isCallbackError) {

			}
		});
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (ex != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Device Model: ").append(Build.MODEL).append("\n");
			sb.append("OS: ").append(Build.VERSION.RELEASE).append("\n");
			sb.append("App Code: ").append(BuildConfig.VERSION_CODE).append("\n");
			sb.append("App Name: ").append(BuildConfig.VERSION_NAME).append("\n");

			//打印异常堆栈信息
			sb.append(printExceptionStack(ex));

			LogUtils.log2File(MyConstant.PATH + MyConstant.CRASH_LOG_DIR, sb.toString());
		}
		mUncaughtExceptionHandler.uncaughtException(thread, ex);
	}

	private String printExceptionStack(Throwable ex) {
		Throwable th = ex;
		StringBuilder sb = new StringBuilder();
		while (th != null) {
			sb.append("Caused By: ").append(th.getClass().getName()).append(":").append(th.getMessage()).append("\n");
			sb.append("Caused Detail: ");
			for (StackTraceElement ste : th.getStackTrace()) {
				sb.append("\tat ").append(ste).append("\n");
			}
			th = th.getCause();
		}
		return sb.toString();
	}
}
