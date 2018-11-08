package com.gizwits.energy.android.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ListView;

import com.gizwits.energy.android.lib.R;


public class FixHeightListView extends ListView {

	private static final String MAX_HEIGHT = "maxHeight";
	private static final String STATE = "state";
	private int maxHeight;

	public FixHeightListView(Context context) {
		super(context);
	}

	public FixHeightListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FixHeightListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		//加载属性
		if (null != attrs && attrs.getAttributeCount() > 0) {
			TypedArray mAttrs = context.obtainStyledAttributes(attrs, R.styleable.FixHeightListView);
			maxHeight = mAttrs.getDimensionPixelSize(R.styleable.FixHeightListView_maxHeight, Integer.MAX_VALUE >> 2);
			mAttrs.recycle();
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(STATE, super.onSaveInstanceState());
		bundle.putInt(MAX_HEIGHT, maxHeight);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE));
			maxHeight = ((Bundle) state).getInt(MAX_HEIGHT);
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isInEditMode()) {
			if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
				int expandSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
				super.onMeasure(widthMeasureSpec, expandSpec);
			} else {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		} else {
			int expandSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);

			super.onMeasure(widthMeasureSpec, expandSpec);

			int height = MeasureSpec.getSize(heightMeasureSpec);
			if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY && getMeasuredHeight() < height) {
				expandSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
				super.onMeasure(widthMeasureSpec, expandSpec);
			}
		}
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}
}
