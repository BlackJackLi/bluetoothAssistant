package com.github.black.bluetooth_assistant.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Black on 2018/2/28 0028.
 */

public final class PermissionUtil {

	/**
	 * 检查是否拥有指定的所有权限
	 */
	public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
				// 只要有一个权限没有被授予, 则直接返回 false
				return false;
			}
		}
		return true;
	}
}
