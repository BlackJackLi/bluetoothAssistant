package com.gizwits.energy.android.lib.web.parser;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gizwits.energy.android.lib.utils.GsonUtil;
import com.gizwits.energy.android.lib.web.base.WebResponse;
import com.gizwits.energy.android.lib.web.base.WebResponseParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Black on 2016/3/3 0003.
 */
public final class DefaultListParser<T> implements WebResponseParser<List<T>> {

	private Class<T> targetClass;

	private DefaultListParser(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	@Override @Nullable
	public List<T> parse(WebResponse<List<T>> webResponse) {
		String json = webResponse.getResult();
		List<T> list=null;
		if (!TextUtils.isEmpty(json)) {
			list = new ArrayList<>();
			JsonArray array = new JsonParser().parse(json).getAsJsonArray();
			Gson gson = GsonUtil.createSecurityGson();
			for (JsonElement jsonElement : array) {
				T obj = gson.fromJson(jsonElement, targetClass);
				if (obj != null) {
					list.add(obj);
				}
			}
		}
		return list;
	}

	public static <T> DefaultListParser<T> createDefaultArrayParser(Class<T> targetClass) {
		return new DefaultListParser<>(targetClass);
	}
}
