package com.neuroandroid.pyweather.mvp.model;


import com.neuroandroid.pyweather.base.BaseResponse;
import com.neuroandroid.pyweather.model.response.User;

import retrofit2.Call;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public interface ILoginModel {
    Call<BaseResponse<User>> login(String param, String password, int userType, String Ip);
}
