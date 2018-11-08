package com.github.black.bluetooth_assistant.service;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.black.bluetooth_assistant.common.ConfigSp;

import org.xutils.x;

import java.util.List;

public class BluetoothAssistantService extends Service {

	private final static String TAG = "AssistantService";

	public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";

	private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

	private BluetoothAdapter bluetoothAdapter;

	private BluetoothHeadset bluetoothHeadset;

	private BluetoothA2dp bluetoothA2dp;

	private AudioManager mAudioManager;

	private BroadcastReceiver myBluetoothReceiver;
	private VolumeBroadcastReceiver volumeBroadcastReceiver;

	private ConfigSp configSp;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind assistant service");

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d(TAG, "start assistant service");

		configSp = new ConfigSp(this);

		initBluetoothReceiver();

		initVolumeChangedReceiver();

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		Log.d(TAG, "max music vol " + mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

		if (bluetoothAdapter != null) {
			if (bluetoothAdapter.isEnabled()) {
				initProfileProxy();
			}
		} else {
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "destroy assistant service");

		unregisterReceiver();
		closeProfileProxy();

		super.onDestroy();
	}

	private void initBluetoothReceiver() {
		IntentFilter mFilter = new IntentFilter();
		//发现设备
		mFilter.addAction(BluetoothDevice.ACTION_FOUND);
		//设备连接状态改变
		mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		//蓝牙设备状态改变
		mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

		//蓝牙profile状态改变
		mFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);

		myBluetoothReceiver = new BluetoothReceiver();
		registerReceiver(myBluetoothReceiver, mFilter);
	}

	private void initVolumeChangedReceiver() {
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(VOLUME_CHANGED_ACTION);
		volumeBroadcastReceiver = new VolumeBroadcastReceiver();
		getApplication().registerReceiver(volumeBroadcastReceiver, mFilter);
	}

	private void unregisterReceiver() {
		if (myBluetoothReceiver != null) {
			unregisterReceiver(myBluetoothReceiver);
			myBluetoothReceiver = null;
		}

		if (volumeBroadcastReceiver != null) {
			getApplication().unregisterReceiver(volumeBroadcastReceiver);
			volumeBroadcastReceiver = null;
		}
	}

	private void initProfileProxy() {
		bluetoothAdapter.getProfileProxy(this, new HeadsetServiceListener(), BluetoothProfile.HEADSET);

		bluetoothAdapter.getProfileProxy(this, new A2dpServiceListener(), BluetoothProfile.A2DP);
	}

	private void closeProfileProxy() {
		bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset);
		bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, bluetoothA2dp);
	}

	private void checkCurrentConnectedDevice() {
		int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
		int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
		Log.d(TAG, "a2dp connected " + (a2dp == BluetoothAdapter.STATE_CONNECTED));
		Log.d(TAG, "headset connected " + (headset == BluetoothAdapter.STATE_CONNECTED));
	}


	private void handleA2dpConnectionCHanged(int connectionState, @NonNull BluetoothDevice device) {
		switch (connectionState) {
			case BluetoothA2dp.STATE_CONNECTED: {
				Log.d(TAG, "device a2dp connected");
				List<BluetoothDevice> devices = bluetoothA2dp.getConnectedDevices();

				if (devices.contains(device)) {
					int vol = configSp.getDeviceMusicVol(device.getAddress());
					if (vol > -1) {
						Log.d(TAG, "set device vol " + vol);
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_SHOW_UI);
					} else {
						configSp.putDeviceMusicVol(device.getAddress(), vol);
					}
				}
				break;
			}
			case BluetoothA2dp.STATE_CONNECTING: {
				Log.d(TAG, "device a2dp connecting");
				break;
			}
			case BluetoothA2dp.STATE_DISCONNECTING: {
				Log.d(TAG, "device a2dp disconnecting");
				break;
			}
			case BluetoothA2dp.STATE_DISCONNECTED: {
				Log.d(TAG, "device a2dp disconnected");
				break;
			}
		}
	}

	private class HeadsetServiceListener implements BluetoothProfile.ServiceListener {

		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			bluetoothHeadset = (BluetoothHeadset) proxy;

			List<BluetoothDevice> devices = bluetoothHeadset.getConnectedDevices();

			for (BluetoothDevice device : devices) {
				Log.d(TAG, "headset name= " + device.getName() + " address= " + device.getAddress());
			}
		}

		@Override
		public void onServiceDisconnected(int profile) {

		}
	}

	private class A2dpServiceListener implements BluetoothProfile.ServiceListener {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			bluetoothA2dp = (BluetoothA2dp) proxy;

			List<BluetoothDevice> devices = bluetoothA2dp.getConnectedDevices();

			for (BluetoothDevice device : devices) {
				Log.d(TAG, "a2dp name= " + device.getName() + " address= " + device.getAddress());
			}
		}

		@Override
		public void onServiceDisconnected(int profile) {

		}
	}


	private class BluetoothReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action == null) return;
			Log.d(TAG, "mBluetoothReceiver action =" + action);

			switch (action) {
				case BluetoothAdapter.ACTION_STATE_CHANGED: {
					int adapterState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);

					if (adapterState == BluetoothAdapter.STATE_ON) {
						Log.d(TAG, "bluetooth ON");
						initProfileProxy();
						checkCurrentConnectedDevice();
						x.task().postDelayed(BluetoothAssistantService.this::checkCurrentConnectedDevice, 5000);
					} else if (adapterState == BluetoothAdapter.STATE_OFF) {
						Log.d(TAG, "bluetooth OFF");
						closeProfileProxy();
					}

					break;
				}

				case BluetoothAdapter.ACTION_DISCOVERY_STARTED: {
					Log.d(TAG, "bluetooth device discovery started");
					break;
				}
				case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: {
					Log.d(TAG, "bluetooth device discovery finish");
					break;
				}
				case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED: {//蓝牙设备连接状态(不等于蓝牙profile连接状态)
					int connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(TAG, "name= " + device.getName() + " address= " + device.getAddress() + " connection state " + (connectionState == BluetoothAdapter
							.STATE_CONNECTED));
					checkCurrentConnectedDevice();
					break;
				}

				case BluetoothDevice.ACTION_FOUND: {//每扫描到一个设备，系统都会发送此广播。
					//获取蓝牙设备
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device == null || device.getName() == null) return;
					//蓝牙设备名称
					Log.d(TAG, "name= " + device.getName() + " address= " + device.getAddress() + " bond state " + (device.getBondState() == BluetoothDevice
							.BOND_BONDED));
					break;
				}
				case BluetoothDevice.ACTION_BOND_STATE_CHANGED: {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(TAG, "name= " + device.getName() + " address= " + device.getAddress() + " bond state " + (device.getBondState() == BluetoothDevice
							.BOND_BONDED));
					break;
				}

				case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED: {//a2dp profile 连接状态
					int connectionState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, BluetoothProfile.STATE_DISCONNECTED);
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(TAG, "name= " + device.getName() + " address= " + device.getAddress() + " a2dp connection state " + (connectionState ==
							BluetoothProfile.STATE_CONNECTED));
					handleA2dpConnectionCHanged(connectionState, device);
					checkCurrentConnectedDevice();
					break;
				}

			}
		}
	}

	private class VolumeBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(VOLUME_CHANGED_ACTION) && (intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC)) {
				int vol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				Log.d(TAG, "vol changed " + vol);
				if (bluetoothA2dp != null) {
					List<BluetoothDevice> devices = bluetoothA2dp.getConnectedDevices();
					for (BluetoothDevice device : devices) {
						configSp.putDeviceMusicVol(device.getAddress(), vol);
					}
				}
			}
		}
	}
}
