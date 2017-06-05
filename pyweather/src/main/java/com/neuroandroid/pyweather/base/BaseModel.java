package com.neuroandroid.pyweather.base;

import com.neuroandroid.pyweather.model.api.ApiService;
import com.neuroandroid.pyweather.net.RetrofitUtils;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public class BaseModel {
    protected ApiService mService;

    public BaseModel(String baseUrl) {
        mService = RetrofitUtils.getInstance(baseUrl, false, true).create(ApiService.class);
    }

    public BaseModel(String baseUrl, boolean needCache) {
        mService = RetrofitUtils.getInstance(baseUrl, needCache, true).create(ApiService.class);
    }

    public BaseModel(String baseUrl, boolean needCache, boolean needLog) {
        mService = RetrofitUtils.getInstance(baseUrl, needCache, needLog).create(ApiService.class);
    }
}
