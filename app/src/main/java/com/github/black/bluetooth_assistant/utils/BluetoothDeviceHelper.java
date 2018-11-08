package com.github.black.bluetooth_assistant.utils;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BluetoothDeviceHelper {


	public boolean isConnected(BluetoothDevice device) {
		try {
			Method isConnected = BluetoothDevice.class.getMethod("isConnected");
			return (boolean) isConnected.invoke(device);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeBond(@NonNull BluetoothDevice device) {
		try {
			Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
			return (boolean) removeBondMethod.invoke(device);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

}
