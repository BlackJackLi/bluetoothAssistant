package com.gizwits.energy.android.lib.presenter;

import java.util.List;

/**
 * Created by black-Gizwits on 2016/06/28.
 */
public interface IDefaultListLoader<ObjectType> extends IRequestLifeCycleHandler {
	void onLoadFail(String msg);

	void onLoadSuccess(List<ObjectType> list);

	void onListEnd();
}
