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
public class DefaultObjectLoadResponseHandler<objectType> extends WebResponseHandler<objectType> {
	private IDefaultObjectLoader<objectType> loader;


	public DefaultObjectLoadResponseHandler(String tag, Context context, List<ErrorHandler> errorHandlers, IDefaultObjectLoader<objectType> objectLoader) {
		super(tag, context, errorHandlers);
		loader = objectLoader;
	}

	public DefaultObjectLoadResponseHandler(Context context, ErrorHandler errorHandler, IDefaultObjectLoader<objectType> objectLoader) {
		super(context, errorHandler);
		loader = objectLoader;
	}

	public DefaultObjectLoadResponseHandler(Context context, IDefaultObjectLoader<objectType> objectLoader) {
		super(context);
		loader = objectLoader;
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
	public void onFailure(@NonNull WebResponse<objectType> response) {
		super.onFailure(response);
		loader.onLoadFail(response.getMessage());
	}

	@Override
	public void onSuccess(@NonNull WebResponse<objectType> response) {
		super.onSuccess(response);
		objectType object = response.getResultObj();
		if (object != null) {
			loader.onLoadSuccess(object);
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
