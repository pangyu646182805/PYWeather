package com.neuroandroid.pyweather.widget.picker.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.neuroandroid.pyweather.widget.picker.lib.WheelView;

public class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
    final WheelView loopView;

    public LoopViewGestureListener(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
