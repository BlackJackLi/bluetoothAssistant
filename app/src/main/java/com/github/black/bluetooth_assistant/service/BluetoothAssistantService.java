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
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class BluetoothAssistantService extends Service {

	private BluetoothAdapter bluetoothAdapter;

	private BluetoothHeadset bluetoothHeadset;

	private BluetoothA2dp bluetoothA2dp;

	private AudioManager mAudioManager;

	private IntentFilter mFilter;

	private BroadcastReceiver myBluetoothReceiver;

	private final static String TAG = "AssistantService";

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind assistant service");

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d(TAG, "start assistant service");

		initBluetoothReceiver();

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		if (bluetoothAdapter != null) {
			if (bluetoothAdapter.isEnabled()) {
				initProfileProxy();
				checkCurrentConnectedDevice();
			}
		} else {
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
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

	@Override
	public void onDestroy() {
		Log.d(TAG, "destroy assistant service");

			unregisterReceiver(myBluetoothReceiver);
			myBluetoothReceiver = null;

		closeProfileProxy();
		super.onDestroy();
	}

	private void initBluetoothReceiver() {
		mFilter = new IntentFilter();
		//发现设备
		mFilter.addAction(BluetoothDevice.ACTION_FOUND);
		//设备连接状态改变
		mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		//蓝牙设备状态改变
		mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

		myBluetoothReceiver = new BluetoothReceiver();
		registerReceiver(myBluetoothReceiver, mFilter);
	}

	private class A2dpServiceListener implements BluetoothProfile.ServiceListener {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			bluetoothA2dp = (BluetoothA2dp) proxy;

			List<BluetoothDevice> devices = bluetoothA2dp.getConnectedDevices();
//			List<BluetoothDevice> devices = bluetoothA2dp.getDevicesMatchingConnectionStates(new int[]{BluetoothProfile.STATE_CONNECTED});

			for (BluetoothDevice device : devices) {
				Log.d(TAG, "a2dp name= " + device.getName() + " address= " + device.getAddress());
			}
		}

		@Override
		public void onServiceDisconnected(int profile) {

		}
	}

	private class HeadsetServiceListener implements BluetoothProfile.ServiceListener {

		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			bluetoothHeadset = (BluetoothHeadset) proxy;

			List<BluetoothDevice> devices = bluetoothHeadset.getConnectedDevices();
//			List<BluetoothDevice> devices = bluetoothHeadset.getDevicesMatchingConnectionStates(new int[]{BluetoothProfile.STATE_CONNECTED});

			for (BluetoothDevice device : devices) {
				Log.d(TAG, "headset name= " + device.getName() + " address= " + device.getAddress());
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
						initProfileProxy();
						checkCurrentConnectedDevice();
					} else {
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
				case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED: {
					BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(TAG, "name= " + scanDevice.getName() + " address= " + scanDevice.getAddress());
					checkCurrentConnectedDevice();
					break;
				}

				case BluetoothDevice.ACTION_FOUND: {//每扫描到一个设备，系统都会发送此广播。
					//获取蓝牙设备
					BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (scanDevice == null || scanDevice.getName() == null) return;
					//蓝牙设备名称
					Log.d(TAG, "name= " + scanDevice.getName() + " address= " + scanDevice.getAddress() + " bond state " + (scanDevice.getBondState() ==
							BluetoothDevice.BOND_BONDED));
					break;
				}
				case BluetoothDevice.ACTION_BOND_STATE_CHANGED: {
					BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(TAG, "name= " + scanDevice.getName() + " address= " + scanDevice.getAddress() + " bond state " + (scanDevice.getBondState() ==
							BluetoothDevice.BOND_BONDED));
					break;
				}
			}
		}
	}
}
