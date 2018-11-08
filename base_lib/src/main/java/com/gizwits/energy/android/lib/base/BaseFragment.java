package com.gizwits.energy.android.lib.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.gizwits.energy.android.lib.dialog.LoadingDialog;
import com.gizwits.energy.android.lib.utils.LogUtils;

import org.xutils.x;

/**
 * Created by black-Gizwits on 2016/02/18.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment {

	protected final String TAG = getClass().getSimpleName();

	private LoadingDialog loadingDialog = null;

	private boolean injected = false;

	public BaseFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		LogUtils.i(TAG, "onAttach");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.i(TAG, "onAttach");
	}

	@Override
	public void onAttachFragment(Fragment childFragment) {
		super.onAttachFragment(childFragment);
		LogUtils.i(TAG, "onAttachFragment");
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		LogUtils.i(TAG, "onCreateAnimation");
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.i(TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		injected = true;
		LogUtils.i(TAG, "onCreateView");
		return x.view().inject(this, inflater, container);
	}

	@Override
	public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(context, attrs, savedInstanceState);
		LogUtils.i(TAG, "onInflate");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (!injected) {
			x.view().inject(this, this.getView());
		}
		LogUtils.i(TAG, "onViewCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i(TAG, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(TAG, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.i(TAG, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtils.i(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.i(TAG, "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i(TAG, "onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.i(TAG, "onDetach");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		LogUtils.i(TAG, "onSaveInstanceState");
	}


	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		LogUtils.i(TAG, "onViewStateRestored");
	}


	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtils.i(TAG, "onLowMemory");
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(TAG, "onActivityCreated");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		LogUtils.i(TAG, "onRequestPermissionsResult");
		LogUtils.i(TAG, "onRequestPermissionsResult request code : " + requestCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case Activity.RESULT_CANCELED: {
				LogUtils.i(TAG, "onActivityResult result canceled");
				break;
			}
			case Activity.RESULT_OK: {
				LogUtils.i(TAG, "onActivityResult result ok");
				break;
			}
			case Activity.RESULT_FIRST_USER: {
				LogUtils.i(TAG, "onActivityResult result first user");

				break;
			}
			default: {
				LogUtils.i(TAG, "onActivityResult customer result code : " + resultCode);
				break;
			}
		}
		LogUtils.i(TAG, "onActivityResult request code : " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		LogUtils.i(TAG, "onMultiWindowModeChanged");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtils.i(TAG, "onConfigurationChanged");
	}

	protected void showLoadingDialog(String msg) {
		showLoadingDialog(msg, true);
	}

	protected void showLoadingDialog(int msgId) {
		showLoadingDialog(msgId, true);
	}

	protected void showLoadingDialog(String msg, boolean cancelable) {
		if (getActivity() == null) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(getActivity(), msg, cancelable);
		loadingDialog.show();
	}

	protected void showLoadingDialog(int msgId, boolean cancelable) {
		if (getActivity() == null) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(getActivity(), msgId, cancelable);
		loadingDialog.show();
	}

	protected void showLoadingDialog(String msg, boolean cancelable, DialogInterface.OnDismissListener onDismissListener) {
		if (getActivity() == null) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(getActivity(), msg, cancelable);
		loadingDialog.setOnDismissListener(onDismissListener);
		loadingDialog.show();
	}

	protected void showLoadingDialog(int msgId, boolean cancelable, DialogInterface.OnDismissListener onDismissListener) {
		if (getActivity() == null) return;
		dismissLoadingDialog();
		loadingDialog = new LoadingDialog(getActivity(), msgId, cancelable);
		loadingDialog.setOnDismissListener(onDismissListener);
		loadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}
}
