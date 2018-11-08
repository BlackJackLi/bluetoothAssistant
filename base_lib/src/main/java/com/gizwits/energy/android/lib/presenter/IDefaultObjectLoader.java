package com.gizwits.energy.android.lib.presenter;

/**
 * Created by black-Gizwits on 2016/06/28.
 */
public interface IDefaultObjectLoader<ObjectType> extends IRequestLifeCycleHandler {
	void onLoadFail(String msg);

	void onLoadSuccess(ObjectType object);
}
