package com.gizwits.energy.android.lib.utils;

import com.gizwits.energy.android.lib.base.AbstractConstantClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Joke Huang
 * @version 1.0.0
 * @Description
 * @createDate 2015年1月27日
 */

public final class GsonUtil extends AbstractConstantClass {

	private GsonUtil() {
		super();
	}
	/**
	 * 构建一个安全的Gson
	 * <p/>
	 * 当解析遇到错误时忽略改字段，返回null
	 *
	 * @return
	 */
	public static Gson createSecurityGson() {
		return new GsonBuilder().registerTypeAdapter(String.class, new JsonDeserializer<String>() {
			@Override
			public String deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
				try {
					return je.getAsString();
				} catch (Exception e) {
					return null;
				}
			}
		}).registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
			@Override
			public Integer deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
				try {
					return je.getAsInt();
				} catch (Exception e) {
					return null;
				}
			}
		}).registerTypeAdapter(Long.class, new JsonDeserializer<Long>() {
			@Override
			public Long deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
				try {
					return je.getAsLong();
				} catch (Exception e) {
					return null;
				}
			}
		}).registerTypeAdapter(Float.class, new JsonDeserializer<Float>() {
			@Override
			public Float deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
				try {
					return je.getAsFloat();
				} catch (Exception e) {
					return null;
				}
			}
		}).registerTypeAdapter(Double.class, new JsonDeserializer<Double>() {
			@Override
			public Double deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
				try {
					return je.getAsDouble();
				} catch (Exception e) {
					return null;
				}
			}
		}).create();
	}
}
