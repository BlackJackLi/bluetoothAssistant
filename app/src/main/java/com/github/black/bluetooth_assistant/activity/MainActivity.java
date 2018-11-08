package com.github.black.bluetooth_assistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ListView;

import com.github.black.bluetooth_assistant.R;
import com.github.black.bluetooth_assistant.adapter.A2dpDeviceAdapter;
import com.github.black.bluetooth_assistant.common.ConfigSp;
import com.github.black.bluetooth_assistant.service.BluetoothAssistantService;
import com.github.black.bluetooth_assistant.utils.PermissionUtil;
import com.gizwits.energy.android.lib.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@ViewInject(R.id.lv_a2dp_device)
	private ListView lv_a2dp_device;

	private A2dpDeviceAdapter a2dpDeviceAdapter;

	private boolean isAllGranted = false;

	private BluetoothAdapter bluetoothAdapter;

	private BluetoothA2dp bluetoothA2dp;

	private ConfigSp configSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		configSp = new ConfigSp(this);
		a2dpDeviceAdapter = new A2dpDeviceAdapter(this);
		lv_a2dp_device.setAdapter(a2dpDeviceAdapter);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter != null) {
			initA2dpProfileListener();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean oldGranted = isAllGranted;

		isAllGranted = PermissionUtil.checkPermissionAllGranted(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
				.ACCESS_COARSE_LOCATION});

		//用户手动授权
		if (!oldGranted && isAllGranted) {
			startService(new Intent(this, BluetoothAssistantService.class));
		}

		if (!isAllGranted) {
			requestPermission();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == 0xab) {
			boolean isAllGranted = false;

			for (int result : grantResults) {
				isAllGranted = result == PackageManager.PERMISSION_GRANTED;
				if (!isAllGranted) {
					openAppDetails();
					break;
				}
			}
			if (isAllGranted) {
				startService(new Intent(this, BluetoothAssistantService.class));
			}
		}
	}

	@Event(R.id.btn_refresh)
	private void refresh(View v) {
		if (bluetoothA2dp != null) {
			updateA2dpList(bluetoothA2dp.getDevicesMatchingConnectionStates(new int[]{BluetoothA2dp.STATE_CONNECTED, BluetoothA2dp.STATE_DISCONNECTED,
					BluetoothA2dp.STATE_CONNECTING, BluetoothA2dp.STATE_DISCONNECTING}));
		}
	}

	private void initA2dpProfileListener() {
		bluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
				bluetoothA2dp = (BluetoothA2dp) proxy;
				updateA2dpList(bluetoothA2dp.getDevicesMatchingConnectionStates(new int[]{BluetoothA2dp.STATE_CONNECTED, BluetoothA2dp.STATE_DISCONNECTED,
						BluetoothA2dp.STATE_CONNECTING, BluetoothA2dp.STATE_DISCONNECTING}));
			}

			@Override
			public void onServiceDisconnected(int profile) {

			}
		}, BluetoothProfile.A2DP);
	}

	private void updateA2dpList(List<BluetoothDevice> bluetoothDevices) {
		a2dpDeviceAdapter.clear();
		for (BluetoothDevice device : bluetoothDevices) {
			a2dpDeviceAdapter.add(new A2dpDeviceAdapter.A2dpDeviceItem(device.getAddress(), device.getName(), configSp.getDeviceMusicVol(device.getAddress()
			)));
		}
	}

	/**
	 * 打开 APP 的详情设置
	 */
	private void openAppDetails() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.manual_authorize_tips);
		builder.setPositiveButton(R.string.go_to_manual_authorize, (dialog, which) -> {
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("package:" + getPackageName()));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			startActivity(intent);
		});

		builder.setCancelable(false);
		builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
			finish();
		});
		builder.show();
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0xab);
	}
}
