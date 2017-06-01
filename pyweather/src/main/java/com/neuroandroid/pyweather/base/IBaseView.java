package com.neuroandroid.pyweather.base;

import com.neuroandroid.pyweather.widget.LoadingLayout;

/**
 * Created by NeuroAndroid on 2017/3/8.
 */

public interface IBaseView<T> {
    /**
     * 设置presenter
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示网络错误(不带错误的文本描述)
     */
    void showError(LoadingLayout.OnRetryListener onRetryListener);

    /**
     * 显示网络错误(带错误的文本描述)
     */
    void showError(LoadingLayout.OnRetryListener onRetryListener, String statusStr);

    void showTip(String tip);
}
