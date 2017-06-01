package com.neuroandroid.pyweather.base;

import retrofit2.Call;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public class BasePresenter<T> {
    protected String baseUrl;
    protected Call<T> mCall;

    public BasePresenter(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
