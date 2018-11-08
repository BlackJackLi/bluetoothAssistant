package com.gizwits.energy.android.lib.presenter;

/**
 * Created by black-Gizwits on 2016/03/01.
 */
public interface IRequestLifeCycleHandler {

	void onRequestStarted();

	void onRequestCanceled();

	void onRequestFinished();
}
