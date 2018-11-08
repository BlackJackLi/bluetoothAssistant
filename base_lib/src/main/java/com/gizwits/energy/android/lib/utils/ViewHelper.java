package com.gizwits.energy.android.lib.utils;

import android.view.View;
import android.view.ViewGroup;

import com.gizwits.energy.android.lib.base.AbstractConstantClass;

/**
 * Created by Black on 2016/10/23 0023.
 */

public class ViewHelper extends AbstractConstantClass {
	public static void setViewGroupSelected(ViewGroup viewGroup, boolean selected) {
		viewGroup.setSelected(selected);
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = viewGroup.getChildAt(i);
			view.setSelected(selected);
		}
	}
}
