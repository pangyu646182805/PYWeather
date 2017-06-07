package com.neuroandroid.pyweather.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.model.response.HeFenWeather;

/**
 * Created by NeuroAndroid on 2017/6/7.
 */

public class WeatherTitleCustomWidget extends LinearLayout {
    private final Context mContext;
    private NoPaddingTextView mTvCity;
    private NoPaddingTextView mTvTemp;
    private NoPaddingTextView mTvWeatherDesc;

    private HeFenWeather.HeWeather5Bean mWeatherBean;
    private LinearLayout mLlSub;
    private int mLlSubWidth;
    private boolean expand;  // 默认不是展开状态

    public void setWeatherBean(HeFenWeather.HeWeather5Bean weatherBean, String city) {
        mWeatherBean = weatherBean;
        setText(weatherBean, city);
    }

    public WeatherTitleCustomWidget(Context context) {
        this(context, null);
    }

    public WeatherTitleCustomWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherTitleCustomWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_weather_title, this);
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);

        mLlSub = (LinearLayout) findViewById(R.id.ll_sub);
        mTvCity = (NoPaddingTextView) findViewById(R.id.tv_city);
        mTvTemp = (NoPaddingTextView) findViewById(R.id.tv_temp);
        mTvWeatherDesc = (NoPaddingTextView) findViewById(R.id.tv_weather_desc);
        mLlSub.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlSub.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mLlSubWidth = mLlSub.getWidth();
                mLlSub.getLayoutParams().width = 0;
                mLlSub.requestLayout();
                mLlSub.setAlpha(0f);
            }
        });
    }

    private void setText(HeFenWeather.HeWeather5Bean weatherBean, String city) {
        if (weatherBean != null) {
            mTvCity.setText(weatherBean.getBasic().getCity());
            mTvTemp.setText(weatherBean.getNow().getTmp() + "℃");
            mTvWeatherDesc.setText(weatherBean.getNow().getCond().getTxt());
        } else {
            mTvCity.setText(city);
        }
    }

    /**
     * 展开天气描述
     */
    public void expand() {
        if (!expand) {  // 不是展开状态才去展开
            expand = true;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
            valueAnimator.addUpdateListener(animator -> {
                float percent = (float) animator.getAnimatedValue();
                mLlSub.setAlpha(percent);
                mLlSub.getLayoutParams().width = (int) (percent * mLlSubWidth);
                mLlSub.requestLayout();
            });
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }
    }

    /**
     * 收缩
     */
    public void shrink() {
        if (expand) {  // 是展开状态才去收缩
            expand = false;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0);
            valueAnimator.addUpdateListener(animator -> {
                float percent = (float) animator.getAnimatedValue();
                mLlSub.setAlpha(percent);
                mLlSub.getLayoutParams().width = (int) (percent * mLlSubWidth);
                mLlSub.requestLayout();
            });
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }
    }
}
