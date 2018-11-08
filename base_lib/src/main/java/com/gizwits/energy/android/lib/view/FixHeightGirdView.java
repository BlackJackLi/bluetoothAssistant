package com.gizwits.energy.android.lib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.GridView;

import com.gizwits.energy.android.lib.R;

public class FixHeightGirdView extends GridView {

	private static final String MAX_HEIGHT = "maxHeight";
	private static final String STATE = "state";
	private int maxHeight;

	public FixHeightGirdView(Context context) {
		super(context);
	}

	public FixHeightGirdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FixHeightGirdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FixHeightGirdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
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

	private void init(Context context, AttributeSet attrs) {
		//加载属性
		if (null != attrs && attrs.getAttributeCount() > 0) {
			TypedArray mAttrs = context.obtainStyledAttributes(attrs, R.styleable.FixHeightGirdView);
			maxHeight = mAttrs.getDimensionPixelSize(R.styleable.FixHeightGirdView_maxHeight, Integer.MAX_VALUE >> 2);
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
	public void setSelector(int resID) {
		super.setSelector(android.R.color.transparent);
	}

}
