package com.neuroandroid.pyweather.model.api;

import com.neuroandroid.pyweather.base.BaseResponse;
import com.neuroandroid.pyweather.model.response.User;
import com.neuroandroid.pyweather.model.response.HeFenWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by NeuroAndroid on 2017/3/10.
 */

public interface ApiService {
    @GET("login/{param}")
    Call<BaseResponse<User>> login(@Path("param") String param,
                                   @Query("password") String password,
                                   @Query("userType") int userType,
                                   @Query("visitIp") String ip);

    @GET("weather")
    Call<HeFenWeather> getWeatherInfo(@Query("city") String city, @Query("key") String key);
}
