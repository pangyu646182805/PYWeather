package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	@Override  // 表示事件是否拦截, 返回false表示不拦截
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override  // 重写ontouch时间，什么事都不用做
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
