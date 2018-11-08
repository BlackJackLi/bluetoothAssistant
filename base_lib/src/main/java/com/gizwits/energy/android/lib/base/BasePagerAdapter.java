package com.gizwits.energy.android.lib.base;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Black on 2017/12/26 0026.
 */

public abstract class BasePagerAdapter<T> extends PagerAdapter {

	protected final String TAG = getClass().getSimpleName();

	public static final int MODE_NONE_SELECT = 0;
	public static final int MODE_MULTIPLE_SELECT = 1;
	public static final int MODE_SINGLE_SELECT = 2;

	@IntDef({MODE_NONE_SELECT, MODE_MULTIPLE_SELECT, MODE_SINGLE_SELECT})
	@Retention(RetentionPolicy.SOURCE)
	protected @interface selectMode {
	}

	protected List<T> items;

	protected Context context;

	private Queue<View> viewQueue;

	@selectMode
	protected int selectMode = MODE_NONE_SELECT;
	protected List<T> selectedItems;

	public BasePagerAdapter(Context context) {
		this(context, null);
	}

	public BasePagerAdapter(Context context, List<T> items) {
		this.items = new ArrayList<>();
		if (items != null) {
			//复制隔离
			this.items.addAll(items);
		}
		this.context = context;
		viewQueue = new LinkedBlockingQueue<>();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = viewQueue.poll();
		if (view == null) {
			view = newViewHolder().init(LayoutInflater.from(context), container);
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();

		viewHolder.update(position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		if (viewQueue.size() < 2) {
			viewQueue.add((View) object);
		}
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	public final void setSelectMode(@selectMode int selectMode) {
		this.selectMode = selectMode;
		if (selectMode != MODE_NONE_SELECT) {
			selectedItems = new ArrayList<>();
		}
	}

	@selectMode
	public final int getSelectMode() {
		return selectMode;
	}

	public final boolean isSelected(T item) {
		return selectMode != MODE_NONE_SELECT && selectedItems.contains(item);
	}

	public void setSelected(T item) {
		switch (selectMode) {
			case MODE_NONE_SELECT: {
				return;
			}
			case MODE_SINGLE_SELECT: {//单选需要先清除已选对象,然后再添加(跟多选一样)
				selectedItems.clear();
			}
			case MODE_MULTIPLE_SELECT: {
				selectedItems.add(item);
				notifyDataSetChanged();
			}
		}
	}

	protected abstract ViewHolder newViewHolder();

	protected abstract class ViewHolder {

		protected ViewHolder() {
		}

		private View init(LayoutInflater layoutInflater, ViewGroup container) {
			View view = initView(layoutInflater, container);
			x.view().inject(this, view);
			view.setTag(this);
			return view;
		}

		protected abstract View initView(LayoutInflater layoutInflater, ViewGroup container);

		protected abstract void update(int position);
	}
}
