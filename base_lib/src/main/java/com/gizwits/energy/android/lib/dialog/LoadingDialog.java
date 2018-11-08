package com.gizwits.energy.android.lib.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.energy.android.lib.R;
import com.gizwits.energy.android.lib.base.BaseDialog;

public class LoadingDialog extends BaseDialog {
	private ImageView iv_loading;

	private TextView tv_msg;

	private AnimationDrawable adAnimationDrawable = null;
	private boolean cancelBackPress;

	public LoadingDialog(Context context, int msgId) {
		super(context);
		initUI(context);
		adAnimationDrawable = (AnimationDrawable) iv_loading.getBackground();
		adAnimationDrawable.start();
		tv_msg.setText(msgId);
		setCanceledOnTouchOutside(false);
	}

	public LoadingDialog(Context context, String msg) {
		super(context);
		initUI(context);
		adAnimationDrawable = (AnimationDrawable) iv_loading.getBackground();
		adAnimationDrawable.start();
		tv_msg.setText(msg);
		setCanceledOnTouchOutside(false);
	}

	public LoadingDialog(Context context, int msgId, boolean cancelBackPress) {
		super(context);
		initUI(context);
		adAnimationDrawable = (AnimationDrawable) iv_loading.getBackground();
		adAnimationDrawable.start();
		tv_msg.setText(msgId);
		setCanceledOnTouchOutside(false);
		this.cancelBackPress = cancelBackPress;
	}

	public LoadingDialog(Context context, String msg, boolean cancelBackPress) {
		super(context);
		initUI(context);
		adAnimationDrawable = (AnimationDrawable) iv_loading.getBackground();
		adAnimationDrawable.start();
		tv_msg.setText(msg);
		setCanceledOnTouchOutside(false);
		this.cancelBackPress = cancelBackPress;
	}

	private void initUI(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		setContentView(view);
		iv_loading = (ImageView) findViewById(R.id.iv_loading);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
	}

	@Override
	public void onBackPressed() {
		if (cancelBackPress) super.onBackPressed();
	}

	public void setMsg(int msgId) {
		((TextView) findViewById(R.id.tv_msg)).setText(msgId);
	}

	public void showDialog() {
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismiss() {
		try {
			super.dismiss();
			if (adAnimationDrawable != null) {
				adAnimationDrawable.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
