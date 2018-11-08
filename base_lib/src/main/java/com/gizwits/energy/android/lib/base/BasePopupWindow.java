package com.gizwits.energy.android.lib.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import org.xutils.x;

/**
 * Created by black-Gizwits on 2016/04/21.
 */
public abstract class BasePopupWindow extends PopupWindow {
	protected Activity mActivity;

	public BasePopupWindow(Activity activity, int resourceId) {
		mActivity = activity;
		View v = LayoutInflater.from(mActivity).inflate(resourceId, null);
		setContentView(v);
		x.view().inject(this, v);
	}
}
