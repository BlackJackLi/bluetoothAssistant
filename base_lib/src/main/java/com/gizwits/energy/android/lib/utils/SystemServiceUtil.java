package com.gizwits.energy.android.lib.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SystemServiceUtil {
	private SystemServiceUtil() {
	}

	public static WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService("window");
	}

	public static LayoutInflater getLayoutInflater(Context context) {
		return (LayoutInflater) context.getSystemService("layout_inflater");
	}

	public static ActivityManager getActivityManager(Context context) {
		return (ActivityManager) context.getSystemService("activity");
	}

	public static PowerManager getPowerManger(Context context) {
		return (PowerManager) context.getSystemService("power");
	}

	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService("alarm");
	}

	public static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context.getSystemService("notification");
	}

	public static KeyguardManager getKeyguardManager(Context context) {
		return (KeyguardManager) context.getSystemService("keyguard");
	}

	public static LocationManager getLocationManager(Context context) {
		return (LocationManager) context.getSystemService("location");
	}

	public static SearchManager getSearchManager(Context context) {
		return (SearchManager) context.getSystemService("search");
	}

	public static Vibrator getVebrator(Context context) {
		return (Vibrator) context.getSystemService("vibrator");
	}

	public static ConnectivityManager getConnectivityManager(Context context) {
		return (ConnectivityManager) context.getSystemService("connectivity");
	}

	public static WifiManager getWifiManager(Context context) {
		return (WifiManager) context.getSystemService("wifi");
	}

	public static TelephonyManager getTelephonyManager(Context context) {
		return (TelephonyManager) context.getSystemService("phone");
	}

	public static InputMethodManager getInputMethodManager(Context context) {
		return (InputMethodManager) context.getSystemService("input_method");
	}

	public static AudioManager getAudioManager(Context context) {
		return (AudioManager) context.getSystemService("audio");
	}

	@SuppressLint({"InlinedApi"})
	public static BluetoothManager getBluetoothManager(Context context) {
		return (BluetoothManager) context.getSystemService("bluetooth");
	}
}
