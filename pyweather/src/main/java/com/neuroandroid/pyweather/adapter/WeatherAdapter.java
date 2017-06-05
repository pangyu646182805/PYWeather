package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.base.BaseRvAdapter;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.WeatherLineChartView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherAdapter extends BaseRvAdapter<HeFenWeather.HeWeather5Bean, WeatherAdapter.Holder> {
    private static final int ITEM_HEADER = 0;
    private static final int ITEM_LINE_CHART = 1;  // 折线图
    private static final int ITEM_AIR_QUALITY = 2;  // 空气质量
    private static final int ITEM_SUN = 3;  // 日出日落
    private static final int ITEM_SUGGESTION = 4;  // 生活指数

    public WeatherAdapter(Context context, List<HeFenWeather.HeWeather5Bean> dataList) {
        super(context, dataList);
    }

    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        Holder holder = null;
        View view;
        switch (viewType) {
            case ITEM_HEADER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_header, parent, false);
                holder = new Holder(view, viewType);
                break;
            case ITEM_LINE_CHART:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_line_chart, parent, false);
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

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_HEADER:
                holder.setHeaderData();
                break;
            case ITEM_LINE_CHART:
                holder.setLineChartData();
                break;
            case ITEM_AIR_QUALITY:
                break;
            case ITEM_SUN:
                break;
            case ITEM_SUGGESTION:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList == null || mDataList.isEmpty()) return 0;
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case ITEM_HEADER:
                return ITEM_HEADER;
            case ITEM_LINE_CHART:
                return ITEM_LINE_CHART;
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

        private NoPaddingTextView mTvCurrentTemp;
        private NoPaddingTextView mTvWeatherDesc;
        private NoPaddingTextView mTvRefreshTime;
        private NoPaddingTextView mTvWet;
        private NoPaddingTextView mTvWind;
        private NoPaddingTextView mTvWindDesc;
        private NoPaddingTextView mTvTemp;

        private WeatherLineChartView mWeatherLineChartView;

        public Holder(View itemView, int viewType) {
            super(itemView);
            mWeatherBean = mDataList.get(0);
            switch (viewType) {
                case ITEM_HEADER:
                    mTvCurrentTemp = ButterKnife.findById(itemView, R.id.tv_current_temp);
                    mTvWeatherDesc = ButterKnife.findById(itemView, R.id.tv_weather_desc);
                    mTvRefreshTime = ButterKnife.findById(itemView, R.id.tv_refresh_time);
                    mTvWet = ButterKnife.findById(itemView, R.id.tv_wet);
                    mTvWind = ButterKnife.findById(itemView, R.id.tv_wind);
                    mTvWindDesc = ButterKnife.findById(itemView, R.id.tv_wind_desc);
                    mTvTemp = ButterKnife.findById(itemView, R.id.tv_temp);
                    break;
                case ITEM_LINE_CHART:
                    mWeatherLineChartView = ButterKnife.findById(itemView, R.id.weather_line_chart);
            }
        }

        public void setHeaderData() {
            HeFenWeather.HeWeather5Bean.NowBean now = mWeatherBean.getNow();
            // HeFenWeather.HeWeather5Bean.BasicBean basic = mWeatherBean.getBasic();
            mTvCurrentTemp.setText(now.getTmp() + "°");
            mTvWeatherDesc.setText(now.getCond().getTxt());
            mTvRefreshTime.setText("12:52更新");
            mTvWet.setText(now.getHum() + "%");
            mTvWindDesc.setText(now.getWind().getDir());
            mTvWind.setText(now.getWind().getSc() + "级");
            mTvTemp.setText(now.getFl() + "°");
        }

        public void setLineChartData() {
            mWeatherLineChartView.setDailyForecastDataList(mWeatherBean.getDaily_forecast());
        }
    }
}
