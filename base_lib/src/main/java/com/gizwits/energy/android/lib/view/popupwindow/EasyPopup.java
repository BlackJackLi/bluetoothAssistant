//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gizwits.energy.android.lib.view.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class EasyPopup extends PopupWindow {
	protected Context context;

	public EasyPopup() {
		this.init();
	}

	public EasyPopup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
		this.context = context;
	}

	public EasyPopup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
		this.context = context;
	}

	public EasyPopup(Context context) {
		super(context);
		this.init();
		this.context = context;
	}

	public EasyPopup(Context context, int resId) {
		this(context, resId, -2, -2);
	}

	public EasyPopup(Context context, int resId, int width, int height) {
		super(context);
		View v = LayoutInflater.from(context).inflate(resId, (ViewGroup)null);
		this.setContentView(v);
		this.setWidth(width);
		this.setHeight(height);
		this.init();
		this.context = context;
	}

	public EasyPopup(int width, int height) {
		super(width, height);
		this.init();
	}

	public EasyPopup(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
		this.setOutsideDismissable(true);
	}

	public EasyPopup(View contentView, int width, int height) {
		super(contentView, width, height);
		this.init();
	}

	public EasyPopup(View contentView) {
		super(contentView);
		this.init();
	}

	private void init() {
		this.setFocusable(true);
		this.setOutsideDismissable(true);
	}

	public void setOutsideDismissable(boolean dismissable) {
		if(dismissable) {
			this.setOutsideTouchable(true);
			this.setBackgroundDrawable(new BitmapDrawable());
		} else {
			this.setBackgroundDrawable((Drawable)null);
		}

	}

	public View findViewById(int id) {
		return this.getContentView().findViewById(id);
	}
}
