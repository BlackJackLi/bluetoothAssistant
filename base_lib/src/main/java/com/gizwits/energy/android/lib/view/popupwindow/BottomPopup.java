package com.gizwits.energy.android.lib.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import org.xutils.x;

/**
 * Created by Joke on 2016/4/26.
 */
public abstract class BottomPopup extends EasyPopup implements View.OnClickListener, Animation.AnimationListener {

	private static int ANIMATION_DURATION = 300;
	private View bottomLayout;
	private AlphaAnimation aaIn;
	private AlphaAnimation aaOut;
	private TranslateAnimation taUp;
	private TranslateAnimation taDown;

	public BottomPopup(Context context, int resId) {
		super(context, resId, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		setAnimationStyle(android.R.style.Animation);
		x.view().inject(this, getContentView());
		int[] cancelViewIds = getCancelViewIds();
		if (cancelViewIds != null) {
			for (int cancelViewId : cancelViewIds) {
				findViewById(cancelViewId).setOnClickListener(this);
			}
		}
		bottomLayout = findViewById(getBottomLayoutId());
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(ANIMATION_DURATION);
		aaIn.setFillAfter(true);
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(ANIMATION_DURATION);
		aaOut.setFillAfter(true);
		taUp = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1,
				TranslateAnimation.RELATIVE_TO_SELF, 0);
		taUp.setDuration(ANIMATION_DURATION);
		taUp.setFillAfter(true);
		taDown = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF,
				0, TranslateAnimation.RELATIVE_TO_SELF, 1);
		taDown.setDuration(ANIMATION_DURATION);
		taDown.setFillAfter(true);
		taDown.setAnimationListener(this);
	}

	protected abstract int[] getCancelViewIds();

	protected abstract int getBottomLayoutId();

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		showAnimation();
	}

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		showAnimation();
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		super.showAsDropDown(anchor, xoff, yoff);
		showAnimation();
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
		super.showAsDropDown(anchor, xoff, yoff, gravity);
		showAnimation();
	}

	public void showFullScreen() {
		showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.TOP, 0, 0);
		showAnimation();
	}

	private void showAnimation() {
		getContentView().startAnimation(aaIn);
		if (bottomLayout != null) {
			bottomLayout.startAnimation(taUp);
		}
	}

	@Override
	public void dismiss() {
		getContentView().startAnimation(aaOut);
		if (bottomLayout != null) {
			bottomLayout.startAnimation(taDown);
		} else {
			super.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		super.dismiss();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
