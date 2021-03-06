package com.github.black.bluetooth_assistant.common;

import android.content.Context;

import com.gizwits.energy.android.lib.utils.BaseSPUtil;

public class ConfigSp extends BaseSPUtil {
	public ConfigSp(Context context) {
		super(context, "config");
	}

	public void putDeviceMusicVol(String mac, int vol) {
		putInt(mac, vol);
	}

	public int getDeviceMusicVol(String mac) {
		return getInt(mac, -1);
	}

}
