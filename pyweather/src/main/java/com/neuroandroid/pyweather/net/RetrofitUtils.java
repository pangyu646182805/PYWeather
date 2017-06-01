package com.neuroandroid.pyweather.net;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.neuroandroid.pyweather.utils.NetworkUtils;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.io.File;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/8/30.
 */
public class RetrofitUtils {
    public static volatile Retrofit retrofit;
    private static final int TIMEOUT_CONNECT = 10; // 10秒
    private static final int TIMEOUT_DISCONNECT = 60 * 60 * 24 * 7; // 7天

    private RetrofitUtils() {
    }

    /**
     * 确保该方法在Application类中调用一次
     * needCache 是否需要缓存
     * needForceCache 是否强制从缓存读取数据
     * needLog 是否需要请求网络打印日志
     *
     * @return
     */
    public static Retrofit getInstance(String url, boolean needCache, boolean needLog) {
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(needCache ? getCacheClient(UIUtils.getContext(), needLog) : getClient(needLog))
                .build();
        return retrofit;
    }

    private static OkHttpClient getClient(boolean needLog) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(new StethoInterceptor());
        // builder.readTimeout(20000, TimeUnit.MILLISECONDS);
        if (needLog)
            builder.addInterceptor(new LogInterceptor());
        return builder.build();
    }

    private static OkHttpClient getCacheClient(Context ctx, boolean needLog) {
        File httpCacheDirectory = new File(ctx.getCacheDir(), ctx.getPackageName());
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache)
                // 连接失败是否重新请求
                .retryOnConnectionFailure(true)
                // 没网络时的拦截器
                .addInterceptor(getInterceptor(ctx))
                // 有网络时的拦截器
                .addNetworkInterceptor(getNetWorkInterceptor(ctx))
                .networkInterceptors()
                .add(new StethoInterceptor());
        if (needLog)
            builder.addInterceptor(new LogInterceptor());
        return builder.build();
    }

    private static Interceptor getInterceptor(final Context ctx) {
        return chain -> {
            Request request = chain.request();
            if (!NetworkUtils.isConnected(ctx)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            return chain.proceed(request);
        };
    }

    private static Interceptor getNetWorkInterceptor(final Context ctx) {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (NetworkUtils.isConnected(ctx)) {
                // int maxAge = 0 * 60;
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + TIMEOUT_CONNECT)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // 无网络时，设置超时为1周
                // int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + TIMEOUT_DISCONNECT)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        };
    }

    public static String getCallUrl(Call call) {
        if (call != null) {
            return call.request().url().url().toString();
        }
        return "url is null";
    }
}
