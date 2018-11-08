package com.github.black.bluetooth_assistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.github.black.bluetooth_assistant.R;
import com.github.black.bluetooth_assistant.service.BluetoothAssistantService;
import com.github.black.bluetooth_assistant.utils.PermissionUtil;
import com.gizwits.energy.android.lib.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	private boolean isAllGranted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		stopService(new Intent(this, BluetoothAssistantService.class));
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
		stopService(new Intent(this, BluetoothAssistantService.class));

		x.task().postDelayed(new Runnable() {
			@Override
			public void run() {
				startService(new Intent(MainActivity.this, BluetoothAssistantService.class));
			}
		}, 5000);
	}

	/**
	 * 打开 APP 的详情设置
	 */
	private void openAppDetails() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("蓝牙助手以下权限: “大致位置(蓝牙发现设备)” 和 “外部存储器” 才能正常运行，请到 “应用信息 -> 权限” 中授予！");
		builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setData(Uri.parse("package:" + getPackageName()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				startActivity(intent);
			}
		});
		builder.setCancelable(false);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.show();
	}

	private void requestPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0xab);
	}
}
