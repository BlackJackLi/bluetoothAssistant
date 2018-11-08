package com.gizwits.energy.android.lib.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Black on 2016/3/6 0006.
 */
public abstract class BaseMultilayerFragment extends BaseFragment {
	protected FragmentManager fragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentManager = getChildFragmentManager();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void showFragment(int viewId, Fragment fragment) {
		showFragment(viewId, fragment, false, true);
	}

	protected void showFragment(int viewId, Fragment fragment, boolean isAddToBackStack) {
		showFragment(viewId, fragment, isAddToBackStack, true);
	}

	protected void showFragment(int viewId, Fragment fragment, boolean isAddToBackStack, boolean isAllowStateLoss) {
		if (getActivity() == null || getActivity().isDestroyed()) return;
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment currentFragment = fragmentManager.findFragmentById(viewId);
		if (currentFragment != null && currentFragment.isAdded() && currentFragment != fragment) {
			transaction.remove(currentFragment);
		}
		if (fragment.isAdded()) {
			transaction.show(fragment);
		} else {
			transaction.add(viewId, fragment);
		}
		if (isAddToBackStack) {
			transaction.addToBackStack(fragment.getClass().getName());
		}
		if (isAllowStateLoss) {
			transaction.commitAllowingStateLoss();
		} else {
			transaction.commit();
		}

	}
}
