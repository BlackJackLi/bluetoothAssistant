package com.gizwits.energy.android.lib.web.base;

import org.xutils.common.Callback;

/**
 * Created by black-Gizwits on 2016/04/06.
 */
public class WebRequestCancelHandler {
	private Callback.Cancelable cancelable;
	private volatile boolean isFinished;

	public WebRequestCancelHandler() {
	}

	public void cancel() {
		cancelable.cancel();
	}

	public boolean isCancelled() {
		return cancelable.isCancelled();
	}

	public boolean isFinished() {
		return isFinished;
	}

	/*default*/
	synchronized void setFinished(boolean finished) {
		this.isFinished = finished;
	}


	/*default*/
	synchronized void setCancelable(Callback.Cancelable cancelable) {
		this.cancelable = cancelable;
	}
}
