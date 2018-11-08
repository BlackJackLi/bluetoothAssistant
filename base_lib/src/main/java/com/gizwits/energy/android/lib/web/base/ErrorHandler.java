package com.gizwits.energy.android.lib.web.base;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Black on 2016/3/6 0006.
 */
public interface ErrorHandler {
	/**
	 *
	 * @param context
	 * @param response
	 * @return boolean 如果返回true 则把错误信息传递给其他错误处理器处理,否则则截断错误的传播
	 */
	boolean handleErrorCode(@NonNull Context context, @NonNull WebResponse response);
}
