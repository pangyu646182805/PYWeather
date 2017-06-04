package com.neuroandroid.pyweather.mvp.model;


import com.neuroandroid.pyweather.model.response.HeFenWeather;

import retrofit2.Call;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public interface IWeatherModel {
    Call<HeFenWeather> getWeatherInfo(String city, String key);
}
