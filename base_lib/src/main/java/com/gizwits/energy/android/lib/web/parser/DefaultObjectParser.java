package com.gizwits.energy.android.lib.web.parser;

import com.gizwits.energy.android.lib.utils.GsonUtil;
import com.gizwits.energy.android.lib.web.base.WebResponse;
import com.gizwits.energy.android.lib.web.base.WebResponseParser;
import com.google.gson.Gson;

/**
 * Created by black-Gizwits on 2016/03/15.
 */
public final class DefaultObjectParser<T> implements WebResponseParser<T> {
	private Class<? extends T> targetClass;

	private DefaultObjectParser(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public T parse(WebResponse<T> webResponse) throws Exception {
		Gson gson = GsonUtil.createSecurityGson();
		String json = webResponse.getResult();
		T object = gson.fromJson(json, targetClass);
		return object;
	}

	public static <T> DefaultObjectParser<T> createDefaultParser(Class<T> targetClass) {
		return new DefaultObjectParser<>(targetClass);
	}
}
