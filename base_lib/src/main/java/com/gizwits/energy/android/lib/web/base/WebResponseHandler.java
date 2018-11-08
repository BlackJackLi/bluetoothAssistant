package com.gizwits.energy.android.lib.web.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.gizwits.energy.android.lib.utils.DataVerifyUtils;
import com.gizwits.energy.android.lib.utils.LogUtils;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

public abstract class WebResponseHandler<T> {
	private static ErrorHandler defaultErrorHandler;

	protected String TAG = "WebResponseHandler";
	private boolean enableWebErrorHandler = true;
	private boolean enableNetWorkErrorTips = true;
	private List<ErrorHandler> specificErrorHandlers;
	private Context mContext;

	public WebResponseHandler(Context context) {
		this.mContext = context;
		specificErrorHandlers = new ArrayList<>();
	}

	public WebResponseHandler(Context context, ErrorHandler specificErrorHandler) {
		this.mContext = context;
		List<ErrorHandler> specificErrorHandlers = new ArrayList<>();
		specificErrorHandlers.add(specificErrorHandler);
		this.specificErrorHandlers = specificErrorHandlers;
	}

	public WebResponseHandler(Context context, List<ErrorHandler> specificErrorHandlers) {
		this.mContext = context;
		this.specificErrorHandlers = specificErrorHandlers;
	}

	public WebResponseHandler(String tag, Context context, List<ErrorHandler> specificErrorHandlers) {
		this.TAG = tag;
		this.mContext = context;
		this.specificErrorHandlers = specificErrorHandlers;
	}

	/**
	 * 服务器请求开始
	 */
	public void onStarted() {
		LogUtils.v(TAG, "onStarted");
	}

	/**
	 * 网络错误,没有访问到服务器
	 */
	public void onError(@NonNull Throwable e) {
		LogUtils.v(TAG, "onError");
		if (enableNetWorkErrorTips && mContext != null) {
			Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 服务器业务请求错误,脸上服务器了,但是业务请求不符合要求
	 *
	 * @param response 服务器返回的内容
	 */
	public void onFailure(@NonNull WebResponse<T> response) {
		LogUtils.v(TAG, "onFailure");
		if (enableWebErrorHandler) {
			LogUtils.v(TAG, "onErrorHandler");
			int size = specificErrorHandlers.size();
			boolean interrupt = false;
			if (DataVerifyUtils.isNotNull(mContext, specificErrorHandlers)) {
				for (int i = 0; i < size; i++) {
					ErrorHandler errorHandler = specificErrorHandlers.get(i);
					interrupt = errorHandler.handleErrorCode(mContext, response);
					if (interrupt) {
						break;
					}
				}
			}
			if (!interrupt && DataVerifyUtils.isNotNull(mContext, defaultErrorHandler)) {
				defaultErrorHandler.handleErrorCode(mContext, response);
			}
		}
	}

	/**
	 * 服务器业务请求成功
	 *
	 * @param response 服务器返回的内容
	 */
	public void onSuccess(@NonNull WebResponse<T> response) {
		LogUtils.v(TAG, "onSuccess");
	}

	/**
	 * @param cancelledException 取消异常信息
	 */
	public void onCancelled(Callback.CancelledException cancelledException) {
		LogUtils.v(TAG, "onCancelled");
	}

	/**
	 * 服务器请求结束
	 */
	public void onFinished() {
		LogUtils.v(TAG, "onFinished");
	}

	public final void setEnableWebErrorHandler(boolean enable) {
		enableWebErrorHandler = enable;
	}

	public final void setEnableNetWorkErrorTips(boolean enable) {
		enableNetWorkErrorTips = enable;
	}

	public final boolean isEnableWebErrorHandler() {
		return enableWebErrorHandler;
	}

	public final boolean isEnableNetWorkErrorTips() {
		return enableNetWorkErrorTips;
	}

	public synchronized final List<ErrorHandler> getSpecificErrorHandlers() {
		//数据复制隔离
		List<ErrorHandler> errorHandlerList = new ArrayList<>(specificErrorHandlers.size());
		errorHandlerList.addAll(specificErrorHandlers);
		return errorHandlerList;
	}

	public synchronized final ErrorHandler getSpecificErrorHandler(int i) {
		return specificErrorHandlers.get(i);
	}

	public synchronized final void addSpecificErrorHandlers(ErrorHandler specificErrorHandler) {
		if (!specificErrorHandlers.contains(specificErrorHandler)) specificErrorHandlers.add(specificErrorHandler);
	}

	/**
	 * 默认错误处理器,只能注册一个,新的会覆盖旧的
	 *
	 * @param errorHandler
	 */
	public synchronized final static void registerDefaultErrorHandler(ErrorHandler errorHandler) {
		defaultErrorHandler = errorHandler;
	}

	public final Context getContext() {
		return mContext;
	}
}
