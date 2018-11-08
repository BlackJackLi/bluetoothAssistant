package com.gizwits.energy.android.lib.presenter;

import android.content.Context;

import com.gizwits.energy.android.lib.web.base.WebRequestCancelHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by black-Gizwits on 2016/06/28.
 */
public abstract class BasePresenter {
	private final static int MAX_CANCEL_HANDLER_COUNT = 20;
	private Context mContext;
	protected List<WebRequestCancelHandler> cancelHandlers;

	public BasePresenter(Context context) {
		mContext = context;
		cancelHandlers = Collections.synchronizedList(new ArrayList<WebRequestCancelHandler>());
	}

	public Context getContext() {
		return mContext;
	}

	public final void cancelAllCallingRequest() {
		synchronized (cancelHandlers) {
			int size = cancelHandlers.size();
			for (int i = 0; i < size; i++) {
				cancelHandlers.get(i).cancel();
			}
			cancelHandlers.clear();
		}
	}

	public final void addCancelHandler(WebRequestCancelHandler cancelHandler) {
		releaseFinishedHandler();
		cancelHandlers.add(cancelHandler);
	}

	/**
	 * 当缓存的cancel handler 超过20的时候进行释放操作
	 */
	private void releaseFinishedHandler() {
		if (cancelHandlers.size() > MAX_CANCEL_HANDLER_COUNT) {
			synchronized (cancelHandlers) {
				if (cancelHandlers.size() > MAX_CANCEL_HANDLER_COUNT) {
					ArrayList<WebRequestCancelHandler> finishHandlers = new ArrayList<>();
					int size = cancelHandlers.size();
					for (int i = 0; i < size; i++) {
						WebRequestCancelHandler handler = cancelHandlers.get(i);
						if (handler.isCancelled() || handler.isFinished()) {
							finishHandlers.add(handler);
						}
					}
					cancelHandlers.removeAll(finishHandlers);
				}
			}
		}
	}
}
