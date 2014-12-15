package com.shanbay.reader.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.nineoldandroids.view.ViewHelper;
import com.shanbay.reader.utils.SlidingMenuScreenUtils;
import com.shanbay.reader.R;

@SuppressLint("ClickableViewAccessibility")
public class SlidingMenu extends HorizontalScrollView {
	// Window Setting Variables
	private int myScreenWidth;
	private int myMenuRightPadding;
	private int myMenuWidth;
	private int myHalfMenuWidth;
	private boolean firstOpen;
	private ViewGroup myMenu;
	private ViewGroup myContent;

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		myScreenWidth = SlidingMenuScreenUtils.getScreenWidth(context);
		TypedArray myTypedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = myTypedArray.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = myTypedArray.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				myMenuRightPadding = myTypedArray.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
			}
		}
		myTypedArray.recycle();
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Set the window width
		if (!firstOpen) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			myMenu = (ViewGroup) wrapper.getChildAt(0);
			myContent = (ViewGroup) wrapper.getChildAt(1);

			myMenuWidth = myScreenWidth - myMenuRightPadding;
			myHalfMenuWidth = myMenuWidth / 2;
			myMenu.getLayoutParams().width = myMenuWidth;
			myContent.getLayoutParams().width = myScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(myMenuWidth, 0);
			firstOpen = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > myHalfMenuWidth) {
				this.smoothScrollTo(myMenuWidth, 0);
			} else {
				this.smoothScrollTo(0, 0);
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void closeMenu() {
		this.smoothScrollTo(myMenuWidth, 0);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / myMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(myMenu, leftScale);
		ViewHelper.setScaleY(myMenu, leftScale);
		ViewHelper.setAlpha(myMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(myMenu, myMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(myContent, 0);
		ViewHelper.setPivotY(myContent, myContent.getHeight() / 2);
		ViewHelper.setScaleX(myContent, rightScale);
		ViewHelper.setScaleY(myContent, rightScale);

	}

}
