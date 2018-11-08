package com.gizwits.energy.android.lib.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
	private KeyboardUtil() {
	}

	public static void toggle(Context context) {
		InputMethodManager imm = SystemServiceUtil.getInputMethodManager(context);
		imm.toggleSoftInput(1, 2);
	}

	public static void show(Context context, View view) {
		InputMethodManager imm = SystemServiceUtil.getInputMethodManager(context);
		imm.showSoftInput(view, 1);
	}

	public static void hide(Context context, View view) {
		InputMethodManager imm = SystemServiceUtil.getInputMethodManager(context);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
	}

	public static void showDelayed(final Context context, final View view, long delayMillis) {
		view.postDelayed(new Runnable() {
			public void run() {
				KeyboardUtil.show(context, view);
			}
		}, delayMillis);
	}
}
