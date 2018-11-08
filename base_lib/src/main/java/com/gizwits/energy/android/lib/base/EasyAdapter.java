//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gizwits.energy.android.lib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class EasyAdapter<T> extends BaseAdapter {
	public static final int MODE_NON = 0;
	/**
	 * @deprecated
	 */
	@Deprecated
	public static final int MODE_CHECK_BOX = 1;
	public static final int MODE_MULTIPLE_SELECT = 1;
	/**
	 * @deprecated
	 */
	@Deprecated
	public static final int MODE_RADIO_GROUP = 2;
	public static final int MODE_SINGLE_SELECT = 2;
	protected int mode = 0;
	protected Context context;
	protected List<T> items;
	protected List<T> selectedItems = new ArrayList<>();
	protected boolean isAutoNotify = true;

	public EasyAdapter(Context context) {
		this.context = context;
		this.items = new ArrayList();
	}

	public EasyAdapter(Context context, List<T> items) {
		this.context = context;
		this.items = items;
	}

	public List<T> getItems() {
		return this.items;
	}

	public void setItems(List<T> items) {
		this.items = items;
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public T get(int position) {
		return this.items != null && position >= 0 && position < this.items.size() ? this.items.get(position) : null;
	}

	public int indexOf(T item) {
		return this.items == null ? -1 : this.items.indexOf(item);
	}

	public void add(T item) {
		if (this.items == null) {
			this.items = new ArrayList();
		}

		this.items.add(item);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void add(int position, T item) {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}

		this.items.add(position, item);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void add(List<? extends T> items) {
		if (this.items == null) {
			items = new ArrayList<>();
		}

		this.items.addAll((Collection<? extends T>) items);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void add(int position, List<? extends T> items) {
		if (this.items == null) {
			items = new ArrayList<>();
		}

		this.items.addAll(position, (Collection<? extends T>) items);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void remove(int position) {
		if (this.items != null) {
			T item = this.items.remove(position);
			this.selectedItems.remove(item);
			if (this.isAutoNotify) {
				this.notifyDataSetChanged();
			}

		}
	}

	public void remove(T item) {
		if (this.items != null) {
			this.items.remove(item);
			this.selectedItems.remove(item);
			if (this.isAutoNotify) {
				this.notifyDataSetChanged();
			}

		}
	}

	public void remove(List<? extends T> items) {
		if (this.items != null) {
			this.items.removeAll(items);
			this.selectedItems.removeAll(items);
			if (this.isAutoNotify) {
				this.notifyDataSetChanged();
			}

		}
	}

	public void clear() {
		if (this.items != null) {
			this.items.clear();
			this.selectedItems.clear();
			if (this.isAutoNotify) {
				this.notifyDataSetChanged();
			}

		}
	}

	public int getCount() {
		return this.items == null ? 0 : this.items.size();
	}

	public Object getItem(int position) {
		return this.items != null && position >= 0 && position < this.items.size() ? this.items.get(position) : null;
	}

	public long getItemId(int position) {
		return (long) position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		EasyAdapter.ViewHolder holder = null;
		if (convertView == null) {
			holder = this.newHolder();
			holder.setPosition(position);
			holder.setSelected(this.isSelected(position));
			convertView = holder.init(LayoutInflater.from(this.context),parent);
			convertView.setTag(holder);
		} else {
			holder = (EasyAdapter.ViewHolder) convertView.getTag();
			holder.setPosition(position);
			holder.setSelected(this.isSelected(position));
		}

		holder.update();
		return convertView;
	}

	protected abstract EasyAdapter<T>.ViewHolder newHolder();

	public int getMode() {
		return this.mode;
	}

	public void setMode(int mode) {
		if (this.mode != mode) {
			this.mode = mode;
			this.selectedItems.clear();
			if (this.isAutoNotify) {
				this.notifyDataSetChanged();
			}
		}

	}

	public boolean isSelected(int position) {
		return this.isSelected(this.get(position));
	}

	public boolean isSelected(T item) {
		return this.selectedItems.contains(item);
	}

	public void reverseSelect(int position) {
		this.reverseSelect(this.get(position));
	}

	public void reverseSelect(T item) {
		if (this.isSelected(item)) {
			this.unselect(item);
		} else {
			this.select(item);
		}

	}

	public void select(int position) {
		this.select(this.get(position));
	}

	public void select(T item) {
		if (this.items != null && this.items.contains(item)) {
			switch (this.mode) {
				case 1:
					this.selectedItems.remove(item);
					this.selectedItems.add(item);
					if (this.isAutoNotify) {
						this.notifyDataSetChanged();
					}
					break;
				case 2:
					this.selectedItems.clear();
					this.selectedItems.add(item);
					if (this.isAutoNotify) {
						this.notifyDataSetChanged();
					}
			}

		}
	}

	public void selectAll() {
		this.selectedItems.clear();
		this.selectedItems.addAll(this.items);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void unselectAll() {
		this.selectedItems.clear();
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public void unselect(int position) {
		this.unselect(this.get(position));
	}

	public void unselect(T item) {
		this.selectedItems.remove(item);
		if (this.isAutoNotify) {
			this.notifyDataSetChanged();
		}

	}

	public int getSelection() {
		return this.selectedItems.size() > 0 ? this.indexOf(this.selectedItems.get(0)) : -1;
	}

	public List<Integer> getSelections() {
		ArrayList<Integer> result = new ArrayList<>();
		Iterator<T> i$ = this.selectedItems.iterator();

		while (i$.hasNext()) {
			T item = i$.next();
			int index = this.indexOf(item);
			if (index != -1) {
				result.add(index);
			}
		}

		return result;
	}

	public T getSelectedItem() {
		return this.selectedItems.size() > 0 ? this.selectedItems.get(0) : null;
	}

	public List<T> getSelectedItems() {
		return this.selectedItems;
	}

	protected abstract class ViewHolder {
		protected int position;
		protected boolean isSelected;

		protected ViewHolder() {
		}

		protected abstract View init(LayoutInflater layoutInflater,ViewGroup container);

		protected abstract void update();

		public int getPosition() {
			return this.position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public boolean isSelected() {
			return this.isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
	}
}
