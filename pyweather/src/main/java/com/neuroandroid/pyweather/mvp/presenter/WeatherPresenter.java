package com.neuroandroid.pyweather.mvp.presenter;

import com.neuroandroid.pyweather.base.BasePresenter;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.mvp.contract.IWeatherContract;
import com.neuroandroid.pyweather.mvp.model.IWeatherModel;
import com.neuroandroid.pyweather.mvp.model.impl.WeatherModelImpl;
import com.neuroandroid.pyweather.net.HeFenCallBack;

import retrofit2.Response;

/**
 * Created by Administrator on 2017/6/4.
 */

public class WeatherPresenter extends BasePresenter<HeFenWeather> implements IWeatherContract.Presenter {
    private IWeatherModel mWeatherModel;
    private IWeatherContract.View mView;

    public WeatherPresenter(String baseUrl, IWeatherContract.View view) {
        super(baseUrl);
        this.mView = view;
        mWeatherModel = new WeatherModelImpl(baseUrl, true);
        mView.setPresenter(this);
    }

    @Override
    public void getWeatherInfo(String city, String key) {
        mCall = mWeatherModel.getWeatherInfo(city, key);
        mCall.enqueue(new HeFenCallBack<HeFenWeather>() {
            @Override
            public void onSuccess(Response<HeFenWeather> response) {
                mView.showWeatherInfo(response.body());
            }

            @Override
            public void onFail(String msg) {
                mView.showTip(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
