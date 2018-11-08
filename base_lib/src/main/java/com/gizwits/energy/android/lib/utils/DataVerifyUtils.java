package com.gizwits.energy.android.lib.utils;

import android.support.annotation.NonNull;

import com.gizwits.energy.android.lib.base.AbstractConstantClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by black-Gizwits on 2016/05/06.
 */
public final class DataVerifyUtils extends AbstractConstantClass {

	private static final String NUMBER_REGULAR = "^\\d+$|^\\d+\\.\\d+$";
	private static final String INTEGER_REGULAR = "^\\d+$";

	private DataVerifyUtils() {
		super();
	}

	public static boolean isNotNull(Object... objects) {
		if (objects == null) return false;
		for (Object object : objects) {
			if (object == null) return false;
		}
		return true;
	}

	public static boolean isNotNull(Object object) {
		return (object != null);
	}

	public static boolean isNotNull(List<Object> objects) {
		if (objects == null) return false;
		for (Object object : objects) {
			if (object == null) return false;
		}
		return true;
	}

	public static boolean isAllAccessibleFieldsNotNull(@NonNull Class c, @NonNull Object... objects) {
		if (!isNotNull(objects) || !isNotNull(c)) return false;
		for (Object object : objects) {
			if (!isAllAccessibleFieldsNotNull(c, object)) return false;
		}
		return true;
	}

	public static boolean isAllFieldsNotNull(@NonNull Class c, @NonNull Object... objects) {
		if (!isNotNull(objects) || !isNotNull(c)) return false;
		for (Object object : objects) {
			if (!isFieldsNotNull(object, c.getDeclaredFields())) return false;
		}
		return true;
	}

	public static boolean isAllAccessibleFieldsNotNull(@NonNull Class c, @NonNull Object object) {
		Field[] fields = c.getFields();
		List<Field> accessibleField = new ArrayList<>();
		for (Field field : fields) {
			if (field.isAccessible()) {
				accessibleField.add(field);
			}
		}
		return isFieldsNotNull(object, accessibleField);
	}

	public static boolean isFieldsNotNull(@NonNull Object o, @NonNull Field... fields) {
		if (null == o) return false;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				field.get(o);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static boolean isFieldsNotNull(@NonNull Object o, @NonNull List<Field> fields) {
		if (null == o) return false;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				field.get(o);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static boolean isMatchRegular(@NonNull String regular, @NonNull String... strings) {
		if (null == regular) return false;
		for (String string : strings) {
			if (string == null) return false;
			if (!string.matches(regular)) return false;
		}
		return true;
	}

	public static boolean isNumberFormat(@NonNull String... numbers) {
		return isMatchRegular(NUMBER_REGULAR, numbers);
	}

	public static boolean isIntegerFormat(@NonNull String... numbers) {
		return isMatchRegular(INTEGER_REGULAR, numbers);
	}
}
