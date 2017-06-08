package com.neuroandroid.pyweather.ui.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.event.CustomBackgroundEvent;
import com.neuroandroid.pyweather.event.ThemeStyleEvent;
import com.neuroandroid.pyweather.utils.ColorUtils;
import com.neuroandroid.pyweather.utils.ImageLoader;
import com.neuroandroid.pyweather.utils.PhotoGalleryUtils;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.TimeUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.WeatherLineChartView;
import com.neuroandroid.pyweather.widget.dialog.ListDialog;
import com.neuroandroid.pyweather.widget.dialog.TitleDialog;
import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NeuroAndroid on 2017/6/8.
 */

public class CustomBackgroundActivity extends BaseActivity {
    @BindView(R.id.sw_blur)
    SwitchCompat mSwBlur;
    @BindView(R.id.cv_custom_background)
    CardView mCvCustomBackground;
    @BindView(R.id.iv_custom_background)
    ImageView mIvCustomBackground;
    @BindView(R.id.sb_transparency)
    SeekBar mSbTransparency;
    @BindView(R.id.rl_theme_style)
    RelativeLayout mRlThemeStyle;
    @BindView(R.id.rl_blur)
    RelativeLayout mRlBlur;
    @BindView(R.id.tv_theme_style)
    NoPaddingTextView mTvThemeStyle;
    @BindView(R.id.blur_view)
    RealtimeBlurView mBlurView;
    @BindView(R.id.rl_blur_level)
    RelativeLayout mRlBlurLevel;
    @BindView(R.id.sb_blur_level)
    SeekBar mSbBlurLevel;

    @BindView(R.id.tv_current_temp)
    NoPaddingTextView mTvCurrentTemp;
    @BindView(R.id.tv_refresh_time)
    NoPaddingTextView mTvRefreshTime;
    @BindView(R.id.tv_weather_desc)
    NoPaddingTextView mTvWeatherDesc;
    @BindView(R.id.iv_wet)
    ImageView mIvWet;
    @BindView(R.id.iv_wind)
    ImageView mIvWind;
    @BindView(R.id.iv_temp)
    ImageView mIvTemp;
    @BindView(R.id.tv_wet_desc)
    NoPaddingTextView mTvWetDesc;
    @BindView(R.id.tv_wet)
    NoPaddingTextView mTvWet;
    @BindView(R.id.tv_wind_desc)
    NoPaddingTextView mTvWindDesc;
    @BindView(R.id.tv_wind)
    NoPaddingTextView mTvWind;
    @BindView(R.id.tv_temp_desc)
    NoPaddingTextView mTvTempDesc;
    @BindView(R.id.tv_temp)
    NoPaddingTextView mTvTemp;
    @BindView(R.id.weather_line_chart)
    WeatherLineChartView mWeatherLineChartView;

    private ImageView mIvNight1, mIvNight2, mIvNight3, mIvNight4, mIvNight5, mIvNight6, mIvNight7;
    private ImageView mIvDay1, mIvDay2, mIvDay3, mIvDay4, mIvDay5, mIvDay6, mIvDay7;
    private NoPaddingTextView mTvDay1, mTvDay2, mTvDay3, mTvDay4, mTvDay5, mTvDay6, mTvDay7;
    private NoPaddingTextView mTvDate1, mTvDate2, mTvDate3, mTvDate4, mTvDate5, mTvDate6, mTvDate7;
    private NoPaddingTextView mTvDayDesc1, mTvDayDesc2, mTvDayDesc3, mTvDayDesc4, mTvDayDesc5, mTvDayDesc6, mTvDayDesc7;
    private NoPaddingTextView mTvNightDesc1, mTvNightDesc2, mTvNightDesc3, mTvNightDesc4, mTvNightDesc5, mTvNightDesc6, mTvNightDesc7;

    private int mRlHeight;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_custom_background;
    }

    @Override
    protected void initView() {
        mIvNight1 = ButterKnife.findById(this, R.id.iv_night1);
        mIvNight2 = ButterKnife.findById(this, R.id.iv_night2);
        mIvNight3 = ButterKnife.findById(this, R.id.iv_night3);
        mIvNight4 = ButterKnife.findById(this, R.id.iv_night4);
        mIvNight5 = ButterKnife.findById(this, R.id.iv_night5);
        mIvNight6 = ButterKnife.findById(this, R.id.iv_night6);
        mIvNight7 = ButterKnife.findById(this, R.id.iv_night7);

        mIvDay1 = ButterKnife.findById(this, R.id.iv_day1);
        mIvDay2 = ButterKnife.findById(this, R.id.iv_day2);
        mIvDay3 = ButterKnife.findById(this, R.id.iv_day3);
        mIvDay4 = ButterKnife.findById(this, R.id.iv_day4);
        mIvDay5 = ButterKnife.findById(this, R.id.iv_day5);
        mIvDay6 = ButterKnife.findById(this, R.id.iv_day6);
        mIvDay7 = ButterKnife.findById(this, R.id.iv_day7);

        mTvDay1 = ButterKnife.findById(this, R.id.tv_day1);
        mTvDay2 = ButterKnife.findById(this, R.id.tv_day2);
        mTvDay3 = ButterKnife.findById(this, R.id.tv_day3);
        mTvDay4 = ButterKnife.findById(this, R.id.tv_day4);
        mTvDay5 = ButterKnife.findById(this, R.id.tv_day5);
        mTvDay6 = ButterKnife.findById(this, R.id.tv_day6);
        mTvDay7 = ButterKnife.findById(this, R.id.tv_day7);

        mTvDate1 = ButterKnife.findById(this, R.id.tv_date1);
        mTvDate2 = ButterKnife.findById(this, R.id.tv_date2);
        mTvDate3 = ButterKnife.findById(this, R.id.tv_date3);
        mTvDate4 = ButterKnife.findById(this, R.id.tv_date4);
        mTvDate5 = ButterKnife.findById(this, R.id.tv_date5);
        mTvDate6 = ButterKnife.findById(this, R.id.tv_date6);
        mTvDate7 = ButterKnife.findById(this, R.id.tv_date7);

        mTvDayDesc1 = ButterKnife.findById(this, R.id.tv_day_desc1);
        mTvDayDesc2 = ButterKnife.findById(this, R.id.tv_day_desc2);
        mTvDayDesc3 = ButterKnife.findById(this, R.id.tv_day_desc3);
        mTvDayDesc4 = ButterKnife.findById(this, R.id.tv_day_desc4);
        mTvDayDesc5 = ButterKnife.findById(this, R.id.tv_day_desc5);
        mTvDayDesc6 = ButterKnife.findById(this, R.id.tv_day_desc6);
        mTvDayDesc7 = ButterKnife.findById(this, R.id.tv_day_desc7);

        mTvNightDesc1 = ButterKnife.findById(this, R.id.tv_night_desc1);
        mTvNightDesc2 = ButterKnife.findById(this, R.id.tv_night_desc2);
        mTvNightDesc3 = ButterKnife.findById(this, R.id.tv_night_desc3);
        mTvNightDesc4 = ButterKnife.findById(this, R.id.tv_night_desc4);
        mTvNightDesc5 = ButterKnife.findById(this, R.id.tv_night_desc5);
        mTvNightDesc6 = ButterKnife.findById(this, R.id.tv_night_desc6);
        mTvNightDesc7 = ButterKnife.findById(this, R.id.tv_night_desc7);
        initTitleBar("自定义主页背景");
        initLeftAction(new TitleBar.ImageAction(R.drawable.ic_arrow_back) {
            @Override
            public void performAction(View view) {
                onBackPressed();
            }
        });
        mRlHeight = (int) UIUtils.getDimen(R.dimen.y108);
        mSbTransparency.setMax(70);
        mSbBlurLevel.setMax(100);
        setCustomBackground();
    }

    private void setCustomBackground() {
        String customBackground = SPUtils.getString(this, Constant.SP_CUSTOM_BACKGROUND, null);
        int transparency = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, 0);
        int themeStyle = SPUtils.getInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
        int blurLevel = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, 0);
        ImageLoader.getInstance().displayImage(this, customBackground, getBackground(), mIvCustomBackground);
        mBlurView.setOverlayColor(ColorUtils.adjustAlpha(themeStyle == Color.WHITE ? Color.BLACK : Color.WHITE, transparency * 1f / 100));
        setThemeStyleText(themeStyle == Color.WHITE ? 0 : 1);
        mSbTransparency.setProgress(transparency);
        mBlurView.setBlurRadius(blurLevel);
        controlBlurSwitch(blurLevel);

        setMiniWeatherHeader(themeStyle);
    }

    private void setMiniWeatherHeader(int themeStyleColor) {
        mWeatherLineChartView.setDailyForecastDataList(null, themeStyleColor);

        setTextColor(mTvCurrentTemp, themeStyleColor);
        setTextColor(mTvWeatherDesc, themeStyleColor);
        setTextColor(mTvRefreshTime, themeStyleColor);
        setTextColor(mTvWet, themeStyleColor);
        setTextColor(mTvWetDesc, themeStyleColor);
        setTextColor(mTvWind, themeStyleColor);
        setTextColor(mTvWindDesc, themeStyleColor);
        setTextColor(mTvTemp, themeStyleColor);
        setTextColor(mTvTempDesc, themeStyleColor);

        setTextColor(mTvDay1, themeStyleColor);
        setTextColor(mTvDay2, themeStyleColor);
        setTextColor(mTvDay3, themeStyleColor);
        setTextColor(mTvDay4, themeStyleColor);
        setTextColor(mTvDay5, themeStyleColor);
        setTextColor(mTvDay6, themeStyleColor);
        setTextColor(mTvDay7, themeStyleColor);
        
        setTextColor(mTvDate1, themeStyleColor);
        setTextColor(mTvDate2, themeStyleColor);
        setTextColor(mTvDate3, themeStyleColor);
        setTextColor(mTvDate4, themeStyleColor);
        setTextColor(mTvDate5, themeStyleColor);
        setTextColor(mTvDate6, themeStyleColor);
        setTextColor(mTvDate7, themeStyleColor);
        
        setTextColor(mTvDayDesc1, themeStyleColor);
        setTextColor(mTvDayDesc2, themeStyleColor);
        setTextColor(mTvDayDesc3, themeStyleColor);
        setTextColor(mTvDayDesc4, themeStyleColor);
        setTextColor(mTvDayDesc5, themeStyleColor);
        setTextColor(mTvDayDesc6, themeStyleColor);
        setTextColor(mTvDayDesc7, themeStyleColor);

        setTextColor(mTvNightDesc1, themeStyleColor);
        setTextColor(mTvNightDesc2, themeStyleColor);
        setTextColor(mTvNightDesc3, themeStyleColor);
        setTextColor(mTvNightDesc4, themeStyleColor);
        setTextColor(mTvNightDesc5, themeStyleColor);
        setTextColor(mTvNightDesc6, themeStyleColor);
        setTextColor(mTvNightDesc7, themeStyleColor);

        setWeatherIconColorFilter(mIvDay1, themeStyleColor);
        setWeatherIconColorFilter(mIvDay2, themeStyleColor);
        setWeatherIconColorFilter(mIvDay3, themeStyleColor);
        setWeatherIconColorFilter(mIvDay4, themeStyleColor);
        setWeatherIconColorFilter(mIvDay5, themeStyleColor);
        setWeatherIconColorFilter(mIvDay6, themeStyleColor);
        setWeatherIconColorFilter(mIvDay7, themeStyleColor);

        setWeatherIconColorFilter(mIvNight1, themeStyleColor);
        setWeatherIconColorFilter(mIvNight2, themeStyleColor);
        setWeatherIconColorFilter(mIvNight3, themeStyleColor);
        setWeatherIconColorFilter(mIvNight4, themeStyleColor);
        setWeatherIconColorFilter(mIvNight5, themeStyleColor);
        setWeatherIconColorFilter(mIvNight6, themeStyleColor);
        setWeatherIconColorFilter(mIvNight7, themeStyleColor);

        setWeatherIconColorFilter(mIvWet, themeStyleColor);
        setWeatherIconColorFilter(mIvWind, themeStyleColor);
        setWeatherIconColorFilter(mIvTemp, themeStyleColor);
    }

    private void setTextColor(TextView tv, int themeStyleColor) {
        tv.setTextColor(themeStyleColor);
    }

    private void setWeatherIconColorFilter(ImageView iv, int color) {
        iv.setColorFilter(color);
    }

    private void controlBlurSwitch(int blurLevel) {
        if (blurLevel == 0) {
            mSwBlur.setChecked(false);
            mRlBlurLevel.getLayoutParams().height = 0;
            mRlBlurLevel.requestLayout();
        } else {
            mSwBlur.setChecked(true);
        }
        mSbBlurLevel.setProgress(blurLevel);
    }

    private int getBackground() {
        if (TimeUtils.judgeDayOrNight()) {
            return R.mipmap.img_day;
        } else {
            return R.mipmap.img_night;
        }
    }

    @Override
    protected void initListener() {
        mCvCustomBackground.setOnClickListener(view ->
                new TitleDialog(this).setCustomTitle("自定义主页背景")
                        .setLeftBtnText("清除背景")
                        .setRightBtnText("选择图片")
                        .setCancelClickListener((dialog, v) -> {
                            clearCustomBackground();
                            dialog.dismiss();
                        })
                        .setConfirmClickListener((dialog, v) -> {
                            PhotoGalleryUtils.choseImgFromGallery(this, 1, false, PickConfig.MODE_SINGLE_PICK);
                            dialog.dismiss();
                        }).show());
        mSbTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBlurView.setOverlayColor(ColorUtils.adjustAlpha(SPUtils.getInt(CustomBackgroundActivity.this,
                            Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE) == Color.WHITE ?
                            Color.BLACK : Color.WHITE, progress * 1.0f / 100));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                SPUtils.putInt(CustomBackgroundActivity.this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, progress);
                EventBus.getDefault().post(new CustomBackgroundEvent());
            }
        });
        mSbBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBlurView.setBlurRadius(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress == 0) {
                    mSwBlur.setChecked(false);
                } else {
                    SPUtils.putInt(CustomBackgroundActivity.this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, progress);
                }
                EventBus.getDefault().post(new CustomBackgroundEvent());
            }
        });
        mRlThemeStyle.setOnClickListener(view -> {
            ListDialog listDialog = new ListDialog(this);
            listDialog.setNormalListAdapter(generateDataList(SPUtils.getInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE)),
                    (v, position, isSelected, normalListBean) -> {
                        if (isSelected) {
                            // setThemeStyleText(position);
                            switch (position) {
                                case 0:  // 亮色
                                    SPUtils.putInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
                                    break;
                                case 1:  // 暗色
                                    SPUtils.putInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.BLACK);
                                    break;
                            }
                            setCustomBackground();
                            EventBus.getDefault().post(new ThemeStyleEvent());
                            listDialog.dismiss();
                        }
                    }).setSelectMode(ISelect.SINGLE_MODE);
            listDialog.show();
        });
        mRlBlur.setOnClickListener(view -> mSwBlur.setChecked(!mSwBlur.isChecked()));
        mSwBlur.setOnCheckedChangeListener((compoundButton, checked) -> {
            controlBlurLevelLayout(checked);
            if (!checked) {
                // 如果未选中, 清除SP_CUSTOM_BACKGROUND_BLUR_LEVEL
                SPUtils.putInt(this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, 0);
                mSbBlurLevel.setProgress(0);
                mBlurView.setBlurRadius(0);
            }
        });
    }

    /**
     * 控制mRlBlurLevel的显示与隐藏
     * checked为true显示
     */
    private void controlBlurLevelLayout(boolean checked) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(checked ? 0 : mRlHeight, checked ? mRlHeight : 0);
        valueAnimator.addUpdateListener(animator -> {
            int rlHeight = (int) animator.getAnimatedValue();
            mRlBlurLevel.getLayoutParams().height = rlHeight;
            mRlBlurLevel.requestLayout();
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private void setThemeStyleText(int position) {
        switch (position) {
            case 0:
                mTvThemeStyle.setText("亮色");
                break;
            case 1:
                mTvThemeStyle.setText("暗色");
                break;
        }
    }

    private List<NormalListBean> generateDataList(int color) {
        List<NormalListBean> dataList = new ArrayList<>();
        NormalListBean normalListBean = new NormalListBean();
        normalListBean.setText("亮色");
        normalListBean.setSelected(color == Color.WHITE);
        dataList.add(normalListBean);
        normalListBean = new NormalListBean();
        normalListBean.setText("暗色");
        normalListBean.setSelected(color == Color.BLACK);
        dataList.add(normalListBean);
        return dataList;
    }

    /**
     * 清除自定义背景
     */
    private void clearCustomBackground() {
        String customBackground = SPUtils.getString(this, Constant.SP_CUSTOM_BACKGROUND, null);
        int transparency = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, 0);
        int blurLevel = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, 0);
        if (UIUtils.isEmpty(customBackground) && transparency == 0 && blurLevel == 0) {
            ShowUtils.showToast("清除失败，当前并没有设置自定义背景");
        } else {
            SPUtils.putString(this, Constant.SP_CUSTOM_BACKGROUND, null);
            SPUtils.putInt(this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, 0);
            SPUtils.putInt(this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, 0);
            SPUtils.putInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
            setCustomBackground();
            EventBus.getDefault().post(new CustomBackgroundEvent());
            EventBus.getDefault().post(new ThemeStyleEvent());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoGalleryUtils.PICK_REQUEST_CODE) {
            String path;
            if (requestCode == PhotoGalleryUtils.PICK_REQUEST_CODE) {
                ArrayList<String> paths = data.getStringArrayListExtra("data");
                path = paths.get(0);
            } else {
                Uri resultUri = UCrop.getOutput(data);
                path = resultUri.getPath();
            }
            SPUtils.putString(this, Constant.SP_CUSTOM_BACKGROUND, path);
            EventBus.getDefault().post(new CustomBackgroundEvent());
            ImageLoader.getInstance().displayImage(this, path, getBackground(), mIvCustomBackground);
        }
    }
}
