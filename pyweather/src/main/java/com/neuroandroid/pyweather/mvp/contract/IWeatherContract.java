package com.neuroandroid.pyweather.mvp.contract;

import com.neuroandroid.pyweather.base.IBasePresenter;
import com.neuroandroid.pyweather.base.IBaseView;
import com.neuroandroid.pyweather.model.response.HeFenWeather;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public interface IWeatherContract {
    interface Presenter extends IBasePresenter {
        /**
         * 获取
         */
        void getWeatherInfo(String city, String key);
    }

    interface View extends IBaseView<Presenter> {
        /**
         * 获取天气信息
         */
        void showWeatherInfo(HeFenWeather weatherInfo);
    }
}
