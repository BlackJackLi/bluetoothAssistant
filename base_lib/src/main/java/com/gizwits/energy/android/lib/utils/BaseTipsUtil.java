package com.gizwits.energy.android.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.gizwits.energy.android.lib.R;
import com.gizwits.energy.android.lib.base.AbstractConstantClass;
import com.gizwits.energy.android.lib.web.base.BaseWebAPI;
import com.gizwits.energy.android.lib.web.base.WebResponse;

/**
 * Created by Black on 2016/3/17 0017.
 */
public final class BaseTipsUtil extends AbstractConstantClass {
	private BaseTipsUtil() {
		super();
	}

	public static void showTips(@NonNull Context context, @NonNull WebResponse response) {
		if (context != null && context instanceof Activity && ((Activity) context).isFinishing()) {
			return;
		}
		switch (response.getCode()) {
			case BaseWebAPI.BaseErrorCode.CODE_RESULT_INVALID: {
				Toast.makeText(context, R.string.tips_result_invalid, Toast.LENGTH_SHORT).show();
				break;
			}
			default: {
				Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	private static void showTips(Context context, WebResponse response, String message, int resId) {
	}
}
