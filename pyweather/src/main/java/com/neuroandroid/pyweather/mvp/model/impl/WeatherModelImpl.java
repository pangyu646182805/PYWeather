package com.neuroandroid.pyweather.mvp.model.impl;

import com.neuroandroid.pyweather.base.BaseModel;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.mvp.model.IWeatherModel;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/6/4.
 */

public class WeatherModelImpl extends BaseModel implements IWeatherModel {
    public WeatherModelImpl(String baseUrl, boolean needCache) {
        super(baseUrl, needCache);
    }

    @Override
    public Call<HeFenWeather> getWeatherInfo(String city, String key) {
        return mService.getWeatherInfo(city, key);
    }
}
