package com.github.black.bluetooth_assistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.black.bluetooth_assistant.service.BluetoothAssistantService;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d("BootReceiver", "system boot completed");
			Intent service = new Intent(context, BluetoothAssistantService.class);
			context.startService(service);
		}
	}
}
