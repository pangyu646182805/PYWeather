package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by Administrator on 2017/6/4.
 */

public class WeatherRefreshHeader extends FrameLayout implements IHeaderView {
    private Context mContext;
    private ImageView mIvSun;
    private int mSunWidthAndHeight;
    private RotateAnimation mRotateAnimation;

    public WeatherRefreshHeader(@NonNull Context context) {
        this(context, null);
    }

    public WeatherRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mSunWidthAndHeight = (int) UIUtils.getDimen(R.dimen.x64);
        mIvSun = new ImageView(mContext);
        LayoutParams params = new LayoutParams(mSunWidthAndHeight, mSunWidthAndHeight);
        params.gravity = Gravity.CENTER;
        mIvSun.setLayoutParams(params);
        mIvSun.setImageResource(R.mipmap.iconfont_baitianqing);
        mIvSun.setScaleX(0f);
        mIvSun.setScaleY(0f);
        addView(mIvSun);

        mRotateAnimation = new RotateAnimation(mIvSun.getRotation(), mIvSun.getRotation() + 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.INFINITE);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setDuration(2000);
    }

    /**
     * 改变头部刷新图标的颜色
     */
    public void setRefreshHeaderColorFilter(int color) {
        mIvSun.setColorFilter(color);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        // L.e("onPullingDown : fraction : " + headHeight);
        if (fraction <= 1f) {
            mIvSun.setScaleX(fraction);
            mIvSun.setScaleY(fraction);
            mIvSun.setRotation(fraction * 180);
        }
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        // L.e("onPullReleasing : fraction : " + fraction);
        if (fraction <= 1f) {
            mIvSun.setScaleX(fraction);
            mIvSun.setScaleY(fraction);
            mIvSun.setRotation(fraction * 180);
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        // L.e("onPullReleasing : maxHeadHeight : " + maxHeadHeight + " headHeight : " + headHeight);

        mIvSun.startAnimation(mRotateAnimation);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        // L.e("onFinish");
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        // L.e("reset");
        mIvSun.setRotation(0f);
        mIvSun.clearAnimation();
    }
}
