package com.github.black.bluetooth_assistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.black.bluetooth_assistant.R;
import com.gizwits.energy.android.lib.base.BaseAdapter;

import org.xutils.view.annotation.ViewInject;

public class A2dpDeviceAdapter extends BaseAdapter<A2dpDeviceAdapter.A2dpDeviceItem> {

	public A2dpDeviceAdapter(Context context) {
		super(context);
	}

	@Override
	protected ViewHolder newHolder() {
		return new MyViewHolder();
	}

	public class MyViewHolder extends xViewHolder {

		@ViewInject(R.id.tv_name)
		private TextView tv_name;
		@ViewInject(R.id.tv_mac)
		private TextView tv_mac;
		@ViewInject(R.id.tv_vol)
		private TextView tv_vol;

		@Override
		protected View initView(LayoutInflater layoutInflater, ViewGroup container) {
			return layoutInflater.inflate(R.layout.adapter_item_a2dp_device, container, false);
		}

		@Override
		protected void update() {
			A2dpDeviceItem item = get(position);
			tv_name.setText(item.name);
			tv_mac.setText(item.mac);
			tv_vol.setText(item.vol > -1 ? String.valueOf(item.vol) : "- -");
		}
	}

	public static class A2dpDeviceItem {
		String mac;
		String name;
		int vol;

		public A2dpDeviceItem(String mac, String name, int vol) {
			this.mac = mac;
			this.name = name;
			this.vol = vol;
		}

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getVol() {
			return vol;
		}

		public void setVol(int vol) {
			this.vol = vol;
		}
	}
}
