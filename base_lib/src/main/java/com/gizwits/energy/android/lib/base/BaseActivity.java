package com.gizwits.energy.android.lib.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.gizwits.energy.android.lib.dialog.LoadingDialog;
import com.gizwits.energy.android.lib.utils.KeyboardUtil;
import com.gizwits.energy.android.lib.utils.LogUtils;

import org.xutils.x;

/**
 * Created by black-Gizwits on 2016/02/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

	protected  final String TAG = getClass().getSimpleName();
	private LoadingDialog loadingDialog = null;
	private boolean isAutoClearFocus;

	public BaseActivity() {
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogUtils.i(TAG,"onNewIntent");
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		LogUtils.i(TAG,"onAttachedToWindow");
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		LogUtils.i(TAG,"onDetachedFromWindow");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		x.view().inject(this);
		super.onCreate(savedInstanceState);
		LogUtils.i(TAG,"onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtils.i(TAG,"onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.i(TAG,"onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.i(TAG,"onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtils.i(TAG,"onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.i(TAG,"onDestroy");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		LogUtils.i(TAG,"onSaveInstanceState");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		LogUtils.i(TAG,"onRestoreInstanceState");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtils.i(TAG,"onConfigurationChanged");
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		LogUtils.i(TAG,"onContentChanged");
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		LogUtils.i(TAG,"onMultiWindowModeChanged");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtils.i(TAG,"onLowMemory");
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		LogUtils.i(TAG,"onAttachFragment");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		LogUtils.i(TAG,"onResumeFragments");
	}

	public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
		super.onWindowAttributesChanged(params);
		LogUtils.i(TAG,"onMultiWindowModeChanged");
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		LogUtils.i(TAG,"onMultiWindowModeChanged");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		LogUtils.i(TAG,"onRequestPermissionsResult");
		LogUtils.i(TAG,"onRequestPermissionsResult request code : " + requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_CANCELED: {
				LogUtils.i(TAG,"onActivityResult result canceled");
				break;
			}
			case RESULT_OK: {
				LogUtils.i(TAG,"onActivityResult result ok");
				break;
			}
			case RESULT_FIRST_USER: {
				LogUtils.i(TAG,"onActivityResult result first user");

				break;
			}
			default: {
				LogUtils.i(TAG,"onActivityResult customer result code : " + resultCode);
				break;
			}
		}
		LogUtils.i(TAG,"onActivityResult request code : " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isAutoClearFocus && ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (v != null && (v instanceof EditText)) {
				int[] l = {0, 0};
				v.getLocationInWindow(l);
				int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
				if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
					// 点击EditText的事件，忽略它。
				} else {
					KeyboardUtil.hide(this, v);
					v.clearFocus();
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	protected void showLoadingDialog(String msg) {
		showLoadingDialog(msg, true);
	}

	protected void showLoadingDialog(int msgId) {
		showLoadingDialog(msgId, true);
	}

	protected void showLoadingDialog(String msg, boolean cancelable) {
		if (isDestroyed()) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(this, msg, cancelable);
		loadingDialog.show();
	}

	protected void showLoadingDialog(int msgId, boolean cancelable) {
		if (isDestroyed()) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(this, msgId, cancelable);
		loadingDialog.show();
	}

	protected void showLoadingDialog(String msg, boolean cancelable, DialogInterface.OnDismissListener onDismissListener) {
		if (isDestroyed()) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(this, msg, cancelable);
		loadingDialog.setOnDismissListener(onDismissListener);
		loadingDialog.show();
	}

	protected void showLoadingDialog(int msgId, boolean cancelable, DialogInterface.OnDismissListener onDismissListener) {
		if (isDestroyed()) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(this, msgId, cancelable);
		loadingDialog.setOnDismissListener(onDismissListener);
		loadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	protected boolean isAutoClearFocus() {
		return isAutoClearFocus;
	}

	public void setAutoClearFocus(boolean autoClearFocus) {
		isAutoClearFocus = autoClearFocus;
	}
}
