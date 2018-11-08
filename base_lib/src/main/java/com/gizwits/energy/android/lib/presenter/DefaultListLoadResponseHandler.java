package com.gizwits.energy.android.lib.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gizwits.energy.android.lib.web.base.ErrorHandler;
import com.gizwits.energy.android.lib.web.base.WebResponse;
import com.gizwits.energy.android.lib.web.base.WebResponseHandler;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by black-Gizwits on 2016/06/28.
 */
public class DefaultListLoadResponseHandler<objectType> extends WebResponseHandler<List<objectType>> {
	private final IDefaultListLoader<objectType> loader;
	private final int pageSize;

	public DefaultListLoadResponseHandler(String tag, Context context, List<ErrorHandler> errorHandlers, IDefaultListLoader<objectType> listLoader, int
			pageSize) {
		super(tag, context, errorHandlers);
		loader = listLoader;
		this.pageSize = pageSize;
	}

	public DefaultListLoadResponseHandler(Context context, IDefaultListLoader<objectType> listLoader, int pageSize) {
		super(context);
		loader = listLoader;
		this.pageSize = pageSize;
	}

	public DefaultListLoadResponseHandler(Context context, ErrorHandler errorHandler, IDefaultListLoader<objectType> listLoader, int pageSize) {
		super(context, errorHandler);
		loader = listLoader;
		this.pageSize = pageSize;
	}

	@Override
	public void onStarted() {
		super.onStarted();
		loader.onRequestStarted();
	}

	@Override
	public void onError(@NonNull Throwable e) {
		super.onError(e);
		loader.onLoadFail(e.getMessage());
	}

	@Override
	public void onFailure(@NonNull WebResponse<List<objectType>> response) {
		super.onFailure(response);
		loader.onLoadFail(response.getMessage());
	}

	@Override
	public void onSuccess(@NonNull WebResponse<List<objectType>> response) {
		super.onSuccess(response);
		List<objectType> list = response.getResultObj();
		if (list != null) {
			loader.onLoadSuccess(list);
			if (list.size() < pageSize) {
				loader.onListEnd();
			}
		} else {
			loader.onLoadFail(response.getMessage());
		}
	}

	@Override
	public void onCancelled(Callback.CancelledException cancelledException) {
		super.onCancelled(cancelledException);
		loader.onRequestCanceled();
	}

	@Override
	public void onFinished() {
		super.onFinished();
		loader.onRequestFinished();
	}
}
