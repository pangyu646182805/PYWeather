package com.neuroandroid.pyweather.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.L;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by Administrator on 2017/6/4.
 */

public class WeatherRefreshHeader extends FrameLayout implements IHeaderView {
    private Context mContext;
    private ImageView mIvSun;
    private int mSunWidthAndHeight;

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
        mIvSun.setBackgroundResource(R.mipmap.ic_sun);
        mIvSun.setScaleX(0f);
        mIvSun.setScaleY(0f);
        addView(mIvSun);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        L.e("onPullingDown : fraction : " + fraction);
        // ViewCompat.animate(mIvSun).scaleX()
        if (fraction <= 1f) {
            mIvSun.setScaleX(fraction);
            mIvSun.setScaleY(fraction);
        }
        mIvSun.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        L.e("onPullReleasing : fraction : " + fraction);
        if (fraction <= 1f) {
            mIvSun.setScaleX(fraction);
            mIvSun.setScaleY(fraction);
            mIvSun.setRotation(fraction * headHeight / maxHeadHeight * 180);
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        L.e("onPullReleasing : maxHeadHeight : " + maxHeadHeight + " headHeight : " + headHeight);
        ValueAnimator animator = ValueAnimator.ofFloat(mIvSun.getRotation(), 720f);
        animator.addUpdateListener(animation -> {
            float rotation = (float) animation.getAnimatedValue();
            mIvSun.setRotation(rotation);
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        L.e("onFinish");
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        L.e("reset");
        mIvSun.setRotation(0f);
    }
}
