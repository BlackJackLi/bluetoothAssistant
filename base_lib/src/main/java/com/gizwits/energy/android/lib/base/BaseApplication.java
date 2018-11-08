package com.gizwits.energy.android.lib.base;

import android.app.Application;
import android.content.res.Configuration;

import com.gizwits.energy.android.lib.utils.LogUtils;


/**
 * Created by black-Gizwits on 2016/06/27.
 */
public abstract class BaseApplication extends Application {

	protected final String TAG = getClass().getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i(TAG, "onCreate");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		LogUtils.i(TAG, "onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtils.i(TAG, "onConfigurationChanged");
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		LogUtils.i(TAG, "onTrimMemory");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtils.i(TAG, "onLowMemory");
	}
}
