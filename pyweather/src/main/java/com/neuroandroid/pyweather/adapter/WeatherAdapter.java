package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.base.BaseRvAdapter;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.utils.ImageLoader;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.TimeUtils;
import com.neuroandroid.pyweather.widget.AirQualityView;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.SunriseAndSunsetView;
import com.neuroandroid.pyweather.widget.WeatherLineChartView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherAdapter extends BaseRvAdapter<HeFenWeather.HeWeather5Bean, WeatherAdapter.Holder> {
    private static final int ITEM_HEADER_AND_LINE_CHART = 0;
    private static final int ITEM_AIR_QUALITY = 1;  // 空气质量
    private static final int ITEM_SUN = 2;  // 日出日落
    private static final int ITEM_SUGGESTION = 3;  // 生活指数

    private int mRlHeaderHeight;
    private int mThemeStyleColor = Constant.LIGHT_THEME_STYLE_COLOR;

    public void setThemeStyleColor(boolean lightThemeStyle) {
        mThemeStyleColor = lightThemeStyle ? Constant.LIGHT_THEME_STYLE_COLOR : Constant.DARK_THEME_STYLE_COLOR;
        notifyItemRangeChanged(ITEM_HEADER_AND_LINE_CHART, 4);
    }

    public int getRlHeaderHeight() {
        return mRlHeaderHeight;
    }

    public WeatherAdapter(Context context, List<HeFenWeather.HeWeather5Bean> dataList) {
        super(context, dataList);
        mThemeStyleColor = SPUtils.getInt(mContext, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
    }

    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        Holder holder = null;
        View view;
        switch (viewType) {
            case ITEM_HEADER_AND_LINE_CHART:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_header, parent, false);
                holder = new Holder(view, viewType);
                break;
            case ITEM_AIR_QUALITY:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_air_quality, parent, false);
                holder = new Holder(view, viewType);
                break;
            case ITEM_SUN:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_sun, parent, false);
                holder = new Holder(view, viewType);
                break;
            case ITEM_SUGGESTION:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false);
                holder = new Holder(view, viewType);
                break;
        }
        return holder;
    }

    public void refreshItem(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_HEADER_AND_LINE_CHART:
                holder.setHeaderData();
                break;
            case ITEM_AIR_QUALITY:
                holder.setAirQualityData();
                break;
            case ITEM_SUN:
                holder.setSunriseAndSunsetData();
                break;
            case ITEM_SUGGESTION:
                holder.setLifeIndexData();
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList == null || mDataList.isEmpty()) return 0;
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case ITEM_HEADER_AND_LINE_CHART:
                return ITEM_HEADER_AND_LINE_CHART;
            case ITEM_AIR_QUALITY:
                return ITEM_AIR_QUALITY;
            case ITEM_SUGGESTION:
                return ITEM_SUGGESTION;
            case ITEM_SUN:
                return ITEM_SUN;
        }
        return super.getItemViewType(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private HeFenWeather.HeWeather5Bean mWeatherBean;

        // 头布局
        private RelativeLayout mRlHeader;
        private ImageView mIvWet, mIvWind, mIvTemp;
        private NoPaddingTextView mTvCurrentTemp;
        private NoPaddingTextView mTvWeatherDesc;
        private NoPaddingTextView mTvRefreshTime;
        private NoPaddingTextView mTvWet;
        private NoPaddingTextView mTvWetDesc;
        private NoPaddingTextView mTvWind;
        private NoPaddingTextView mTvWindDesc;
        private NoPaddingTextView mTvTemp;
        private NoPaddingTextView mTvTempDesc;
        private WeatherLineChartView mWeatherLineChartView;
        private ImageView mIvNight1, mIvNight2, mIvNight3, mIvNight4, mIvNight5, mIvNight6, mIvNight7;
        private ImageView mIvDay1, mIvDay2, mIvDay3, mIvDay4, mIvDay5, mIvDay6, mIvDay7;
        private NoPaddingTextView mTvDay1, mTvDay2, mTvDay3, mTvDay4, mTvDay5, mTvDay6, mTvDay7;
        private NoPaddingTextView mTvDate1, mTvDate2, mTvDate3, mTvDate4, mTvDate5, mTvDate6, mTvDate7;
        private NoPaddingTextView mTvDayDesc1, mTvDayDesc2, mTvDayDesc3, mTvDayDesc4, mTvDayDesc5, mTvDayDesc6, mTvDayDesc7;
        private NoPaddingTextView mTvNightDesc1, mTvNightDesc2, mTvNightDesc3, mTvNightDesc4, mTvNightDesc5, mTvNightDesc6, mTvNightDesc7;

        // 空气质量指数
        private AirQualityView mAirQualityView;
        private NoPaddingTextView mTvAirQuality;
        private NoPaddingTextView mTvPm25, mTvPm10, mTvSo2, mTvCo, mTvNo2, mTvO3;
        private NoPaddingTextView mTvPm25Desc, mTvPm10Desc, mTvSo2Desc, mTvCoDesc, mTvNo2Desc, mTvO3Desc;
        private View mSplit0, mSplit1, mSplit2, mSplit3, mSplit4, mSplit5, mSplit6, mSplit7;

        // 日出日落
        private SunriseAndSunsetView mSunriseAndSunsetView;
        private NoPaddingTextView mTvSunriseSunset;
        private View mSplit8;

        // 生活指数
        private NoPaddingTextView mTvComfortDesc, mTvComfortTxt;
        private NoPaddingTextView mTvCarWashDesc, mTvCarWashTxt;
        private NoPaddingTextView mTvDressingDesc, mTvDressingTxt;
        private NoPaddingTextView mTvColdDesc, mTvColdTxt;
        private NoPaddingTextView mTvSportDesc, mTvSportTxt;
        private NoPaddingTextView mTvTourDesc, mTvTourTxt;
        private NoPaddingTextView mTvUvDesc, mTvUvTxt;
        private ImageView mIvConfotr, mIvCarWash, mIvDressing, mIvCold, mIvSport, mIvTour, mIvUv;
        private NoPaddingTextView mTvDataSource, mTvLifeIndex;
        private View mSplit9;

        public Holder(View itemView, int viewType) {
            super(itemView);
            mWeatherBean = mDataList.get(0);
            switch (viewType) {
                case ITEM_HEADER_AND_LINE_CHART:
                    mRlHeader = ButterKnife.findById(itemView, R.id.rl_header);
                    mRlHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mRlHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            mRlHeaderHeight = mRlHeader.getHeight();
                        }
                    });

                    mIvWet = ButterKnife.findById(itemView, R.id.iv_wet);
                    mIvWind = ButterKnife.findById(itemView, R.id.iv_wind);
                    mIvTemp = ButterKnife.findById(itemView, R.id.iv_temp);

                    mTvCurrentTemp = ButterKnife.findById(itemView, R.id.tv_current_temp);
                    mTvWeatherDesc = ButterKnife.findById(itemView, R.id.tv_weather_desc);
                    mTvRefreshTime = ButterKnife.findById(itemView, R.id.tv_refresh_time);
                    mTvWet = ButterKnife.findById(itemView, R.id.tv_wet);
                    mTvWetDesc = ButterKnife.findById(itemView, R.id.tv_wet_desc);
                    mTvWind = ButterKnife.findById(itemView, R.id.tv_wind);
                    mTvWindDesc = ButterKnife.findById(itemView, R.id.tv_wind_desc);
                    mTvTemp = ButterKnife.findById(itemView, R.id.tv_temp);
                    mTvTempDesc = ButterKnife.findById(itemView, R.id.tv_temp_desc);
                    mWeatherLineChartView = ButterKnife.findById(itemView, R.id.weather_line_chart);
                    mIvNight1 = ButterKnife.findById(itemView, R.id.iv_night1);
                    mIvNight2 = ButterKnife.findById(itemView, R.id.iv_night2);
                    mIvNight3 = ButterKnife.findById(itemView, R.id.iv_night3);
                    mIvNight4 = ButterKnife.findById(itemView, R.id.iv_night4);
                    mIvNight5 = ButterKnife.findById(itemView, R.id.iv_night5);
                    mIvNight6 = ButterKnife.findById(itemView, R.id.iv_night6);
                    mIvNight7 = ButterKnife.findById(itemView, R.id.iv_night7);

                    mIvDay1 = ButterKnife.findById(itemView, R.id.iv_day1);
                    mIvDay2 = ButterKnife.findById(itemView, R.id.iv_day2);
                    mIvDay3 = ButterKnife.findById(itemView, R.id.iv_day3);
                    mIvDay4 = ButterKnife.findById(itemView, R.id.iv_day4);
                    mIvDay5 = ButterKnife.findById(itemView, R.id.iv_day5);
                    mIvDay6 = ButterKnife.findById(itemView, R.id.iv_day6);
                    mIvDay7 = ButterKnife.findById(itemView, R.id.iv_day7);

                    mTvDay1 = ButterKnife.findById(itemView, R.id.tv_day1);
                    mTvDay2 = ButterKnife.findById(itemView, R.id.tv_day2);
                    mTvDay3 = ButterKnife.findById(itemView, R.id.tv_day3);
                    mTvDay4 = ButterKnife.findById(itemView, R.id.tv_day4);
                    mTvDay5 = ButterKnife.findById(itemView, R.id.tv_day5);
                    mTvDay6 = ButterKnife.findById(itemView, R.id.tv_day6);
                    mTvDay7 = ButterKnife.findById(itemView, R.id.tv_day7);

                    mTvDate1 = ButterKnife.findById(itemView, R.id.tv_date1);
                    mTvDate2 = ButterKnife.findById(itemView, R.id.tv_date2);
                    mTvDate3 = ButterKnife.findById(itemView, R.id.tv_date3);
                    mTvDate4 = ButterKnife.findById(itemView, R.id.tv_date4);
                    mTvDate5 = ButterKnife.findById(itemView, R.id.tv_date5);
                    mTvDate6 = ButterKnife.findById(itemView, R.id.tv_date6);
                    mTvDate7 = ButterKnife.findById(itemView, R.id.tv_date7);

                    mTvDayDesc1 = ButterKnife.findById(itemView, R.id.tv_day_desc1);
                    mTvDayDesc2 = ButterKnife.findById(itemView, R.id.tv_day_desc2);
                    mTvDayDesc3 = ButterKnife.findById(itemView, R.id.tv_day_desc3);
                    mTvDayDesc4 = ButterKnife.findById(itemView, R.id.tv_day_desc4);
                    mTvDayDesc5 = ButterKnife.findById(itemView, R.id.tv_day_desc5);
                    mTvDayDesc6 = ButterKnife.findById(itemView, R.id.tv_day_desc6);
                    mTvDayDesc7 = ButterKnife.findById(itemView, R.id.tv_day_desc7);

                    mTvNightDesc1 = ButterKnife.findById(itemView, R.id.tv_night_desc1);
                    mTvNightDesc2 = ButterKnife.findById(itemView, R.id.tv_night_desc2);
                    mTvNightDesc3 = ButterKnife.findById(itemView, R.id.tv_night_desc3);
                    mTvNightDesc4 = ButterKnife.findById(itemView, R.id.tv_night_desc4);
                    mTvNightDesc5 = ButterKnife.findById(itemView, R.id.tv_night_desc5);
                    mTvNightDesc6 = ButterKnife.findById(itemView, R.id.tv_night_desc6);
                    mTvNightDesc7 = ButterKnife.findById(itemView, R.id.tv_night_desc7);
                    break;
                case ITEM_AIR_QUALITY:
                    mAirQualityView = ButterKnife.findById(itemView, R.id.air_quality_view);
                    mTvAirQuality = ButterKnife.findById(itemView, R.id.tv_air_quality);

                    mTvPm25 = ButterKnife.findById(itemView, R.id.tv_pm25);
                    mTvPm10 = ButterKnife.findById(itemView, R.id.tv_pm10);
                    mTvSo2 = ButterKnife.findById(itemView, R.id.tv_so2);
                    mTvCo = ButterKnife.findById(itemView, R.id.tv_co);
                    mTvNo2 = ButterKnife.findById(itemView, R.id.tv_no2);
                    mTvO3 = ButterKnife.findById(itemView, R.id.tv_o3);

                    mTvPm25Desc = ButterKnife.findById(itemView, R.id.tv_pm25_desc);
                    mTvPm10Desc = ButterKnife.findById(itemView, R.id.tv_pm10_desc);
                    mTvSo2Desc = ButterKnife.findById(itemView, R.id.tv_so2_desc);
                    mTvCoDesc = ButterKnife.findById(itemView, R.id.tv_co_desc);
                    mTvNo2Desc = ButterKnife.findById(itemView, R.id.tv_no2_desc);
                    mTvO3Desc = ButterKnife.findById(itemView, R.id.tv_o3_desc);

                    mSplit0 = ButterKnife.findById(itemView, R.id.split_0);
                    mSplit1 = ButterKnife.findById(itemView, R.id.split_1);
                    mSplit2 = ButterKnife.findById(itemView, R.id.split_2);
                    mSplit3 = ButterKnife.findById(itemView, R.id.split_3);
                    mSplit4 = ButterKnife.findById(itemView, R.id.split_4);
                    mSplit5 = ButterKnife.findById(itemView, R.id.split_5);
                    mSplit6 = ButterKnife.findById(itemView, R.id.split_6);
                    mSplit7 = ButterKnife.findById(itemView, R.id.split_7);
                    break;
                case ITEM_SUN:
                    mSunriseAndSunsetView = ButterKnife.findById(itemView, R.id.sunrise_and_sunset_view);
                    mTvSunriseSunset = ButterKnife.findById(itemView, R.id.tv_sunrise_sunset);
                    mSplit8 = ButterKnife.findById(itemView, R.id.split_8);
                    break;
                case ITEM_SUGGESTION:
                    mTvComfortDesc = ButterKnife.findById(itemView, R.id.tv_comfort_desc);
                    mTvComfortTxt = ButterKnife.findById(itemView, R.id.tv_comfort_txt);
                    mTvCarWashDesc = ButterKnife.findById(itemView, R.id.tv_car_wash_desc);
                    mTvCarWashTxt = ButterKnife.findById(itemView, R.id.tv_car_wash_txt);
                    mTvDressingDesc = ButterKnife.findById(itemView, R.id.tv_dressing_desc);
                    mTvDressingTxt = ButterKnife.findById(itemView, R.id.tv_dressing_txt);
                    mTvColdDesc = ButterKnife.findById(itemView, R.id.tv_cold_desc);
                    mTvColdTxt = ButterKnife.findById(itemView, R.id.tv_cold_txt);
                    mTvSportDesc = ButterKnife.findById(itemView, R.id.tv_sport_desc);
                    mTvSportTxt = ButterKnife.findById(itemView, R.id.tv_sport_txt);
                    mTvTourDesc = ButterKnife.findById(itemView, R.id.tv_tour_desc);
                    mTvTourTxt = ButterKnife.findById(itemView, R.id.tv_tour_txt);
                    mTvUvDesc = ButterKnife.findById(itemView, R.id.tv_uv_desc);
                    mTvUvTxt = ButterKnife.findById(itemView, R.id.tv_uv_txt);

                    mTvDataSource = ButterKnife.findById(itemView, R.id.tv_data_source);
                    mTvLifeIndex = ButterKnife.findById(itemView, R.id.tv_life_index);
                    mIvConfotr = ButterKnife.findById(itemView, R.id.iv_comfort);
                    mIvCarWash = ButterKnife.findById(itemView, R.id.iv_car_wash);
                    mIvDressing = ButterKnife.findById(itemView, R.id.iv_dressing);
                    mIvCold = ButterKnife.findById(itemView, R.id.iv_cold);
                    mIvSport = ButterKnife.findById(itemView, R.id.iv_sport);
                    mIvTour = ButterKnife.findById(itemView, R.id.iv_tour);
                    mIvUv = ButterKnife.findById(itemView, R.id.iv_uv);

                    mSplit9 = ButterKnife.findById(itemView, R.id.split_9);
                    break;
            }
        }

        public void setHeaderData() {
            HeFenWeather.HeWeather5Bean.NowBean now = mWeatherBean.getNow();
            // HeFenWeather.HeWeather5Bean.BasicBean basic = mWeatherBean.getBasic();
            setTextColor(mTvWetDesc);
            setTextColor(mTvTempDesc);

            setText(mTvCurrentTemp, now.getTmp() + "°");
            setText(mTvWeatherDesc, now.getCond().getTxt());
            setText(mTvRefreshTime, mWeatherBean.getBasic().getUpdate().getLoc() + "更新");
            setText(mTvWet, now.getHum() + "%");
            setText(mTvWindDesc, now.getWind().getDir());
            setText(mTvWind, now.getWind().getSc() + "级");
            setText(mTvTemp, now.getFl() + "°");
            List<HeFenWeather.HeWeather5Bean.DailyForecastBean> dailyForecast = mWeatherBean.getDaily_forecast();
            mWeatherLineChartView.setDailyForecastDataList(dailyForecast, mThemeStyleColor);
            for (int i = 0; i < dailyForecast.size(); i++) {
                HeFenWeather.HeWeather5Bean.DailyForecastBean dailyForecastBean = dailyForecast.get(i);
                HeFenWeather.HeWeather5Bean.DailyForecastBean.CondBeanX cond = dailyForecastBean.getCond();
                HeFenWeather.HeWeather5Bean.DailyForecastBean.WindBeanX wind = dailyForecastBean.getWind();
                String week = TimeUtils.getWeekText(dailyForecastBean.getDate());
                String date = TimeUtils.getDateText(dailyForecastBean.getDate());
                switch (i) {
                    case 0:
                        displayWeatherIcon(mIvDay1, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight1, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay1, "今天");
                        setText(mTvDate1, date);
                        setText(mTvDayDesc1, cond.getTxt_d());
                        setText(mTvNightDesc1, cond.getTxt_n());
                        break;
                    case 1:
                        displayWeatherIcon(mIvDay2, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight2, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay2, week);
                        setText(mTvDate2, date);
                        setText(mTvDayDesc2, cond.getTxt_d());
                        setText(mTvNightDesc2, cond.getTxt_n());
                        break;
                    case 2:
                        displayWeatherIcon(mIvDay3, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight3, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay3, week);
                        setText(mTvDate3, date);
                        setText(mTvDayDesc3, cond.getTxt_d());
                        setText(mTvNightDesc3, cond.getTxt_n());
                        break;
                    case 3:
                        displayWeatherIcon(mIvDay4, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight4, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay4, week);
                        setText(mTvDate4, date);
                        setText(mTvDayDesc4, cond.getTxt_d());
                        setText(mTvNightDesc4, cond.getTxt_n());
                        break;
                    case 4:
                        displayWeatherIcon(mIvDay5, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight5, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay5, week);
                        setText(mTvDate5, date);
                        setText(mTvDayDesc5, cond.getTxt_d());
                        setText(mTvNightDesc5, cond.getTxt_n());
                        break;
                    case 5:
                        displayWeatherIcon(mIvDay6, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight6, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay6, week);
                        setText(mTvDate6, date);
                        setText(mTvDayDesc6, cond.getTxt_d());
                        setText(mTvNightDesc6, cond.getTxt_n());
                        break;
                    case 6:
                        displayWeatherIcon(mIvDay7, Integer.parseInt(cond.getCode_d()));
                        displayWeatherIcon(mIvNight7, Integer.parseInt(cond.getCode_n()));
                        setText(mTvDay7, week);
                        setText(mTvDate7, date);
                        setText(mTvDayDesc7, cond.getTxt_d());
                        setText(mTvNightDesc7, cond.getTxt_n());
                        break;
                }
            }
            setWeatherIconColorFilter(mIvWet, mThemeStyleColor);
            setWeatherIconColorFilter(mIvWind, mThemeStyleColor);
            setWeatherIconColorFilter(mIvTemp, mThemeStyleColor);

            setWeatherIconColorFilter(mIvNight1, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight2, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight3, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight4, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight5, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight6, mThemeStyleColor);
            setWeatherIconColorFilter(mIvNight7, mThemeStyleColor);

            setWeatherIconColorFilter(mIvDay1, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay2, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay3, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay4, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay5, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay6, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDay7, mThemeStyleColor);
        }

        public void setAirQualityData() {
            HeFenWeather.HeWeather5Bean.AqiBean aqiBean = mWeatherBean.getAqi();
            mAirQualityView.setAqiBean(aqiBean, mThemeStyleColor);
            HeFenWeather.HeWeather5Bean.AqiBean.CityBean city = aqiBean.getCity();
            setText(mTvPm25, city.getPm25());
            setText(mTvPm10, city.getPm10());
            setText(mTvSo2, city.getSo2());
            setText(mTvNo2, city.getNo2());
            setText(mTvCo, city.getCo());
            setText(mTvO3, city.getO3());

            setTextColor(mTvAirQuality);
            setTextColor(mTvPm25Desc);
            setTextColor(mTvPm10Desc);
            setTextColor(mTvSo2Desc);
            setTextColor(mTvNo2Desc);
            setTextColor(mTvCoDesc);
            setTextColor(mTvO3Desc);

            setBackgroundColor(mSplit0);
            setBackgroundColor(mSplit1);
            setBackgroundColor(mSplit2);
            setBackgroundColor(mSplit3);
            setBackgroundColor(mSplit4);
            setBackgroundColor(mSplit5);
            setBackgroundColor(mSplit6);
            setBackgroundColor(mSplit7);
        }

        public void setSunriseAndSunsetData() {
            mSunriseAndSunsetView.setAstroBean(mWeatherBean.getDaily_forecast().get(0).getAstro(), mThemeStyleColor);
            setBackgroundColor(mSplit8);
            setTextColor(mTvSunriseSunset);
        }

        public void setLifeIndexData() {
            HeFenWeather.HeWeather5Bean.SuggestionBean suggestion = mWeatherBean.getSuggestion();
            setText(mTvComfortDesc, "舒适度指数 " + suggestion.getComf().getBrf());
            setText(mTvComfortTxt, suggestion.getComf().getTxt());

            setText(mTvCarWashDesc, "洗车指数 " + suggestion.getCw().getBrf());
            setText(mTvCarWashTxt, suggestion.getCw().getTxt());

            setText(mTvDressingDesc, "穿衣指数 " + suggestion.getDrsg().getBrf());
            setText(mTvDressingTxt, suggestion.getDrsg().getTxt());

            setText(mTvColdDesc, "感冒指数 " + suggestion.getFlu().getBrf());
            setText(mTvColdTxt, suggestion.getFlu().getTxt());

            setText(mTvSportDesc, "运动指数 " + suggestion.getSport().getBrf());
            setText(mTvSportTxt, suggestion.getSport().getTxt());

            setText(mTvTourDesc, "旅游指数 " + suggestion.getTrav().getBrf());
            setText(mTvTourTxt, suggestion.getTrav().getTxt());

            setText(mTvUvDesc, "紫外线指数 " + suggestion.getUv().getBrf());
            setText(mTvUvTxt, suggestion.getUv().getTxt());

            setBackgroundColor(mSplit9);
            setTextColor(mTvLifeIndex);
            setTextColor(mTvDataSource);
            setWeatherIconColorFilter(mIvConfotr, mThemeStyleColor);
            setWeatherIconColorFilter(mIvCarWash, mThemeStyleColor);
            setWeatherIconColorFilter(mIvDressing, mThemeStyleColor);
            setWeatherIconColorFilter(mIvCold, mThemeStyleColor);
            setWeatherIconColorFilter(mIvSport, mThemeStyleColor);
            setWeatherIconColorFilter(mIvTour, mThemeStyleColor);
            setWeatherIconColorFilter(mIvUv, mThemeStyleColor);
        }

        private void setWeatherIconColorFilter(ImageView iv, int color) {
            iv.setColorFilter(color);
        }

        private void displayWeatherIcon(ImageView iv, int weatherCode) {
            ImageLoader.getInstance().displayImage(mContext, Constant.BASE_WEATHER_IMG_URL + weatherCode + ".png", R.mipmap.iconfont_baitianqing, iv);
        }

        private void setText(TextView tv, String text) {
            tv.setText(text);
            setTextColor(tv);
        }

        private void setTextColor(TextView tv) {
            tv.setTextColor(mThemeStyleColor);
        }

        private void setBackgroundColor(View split) {
            split.setBackgroundColor(mThemeStyleColor == Color.WHITE ? Constant.LIGHT_THEME_STYLE_SUB_COLOR : Constant.DARK_THEME_STYLE_SUB_COLOR);
        }
    }
}
