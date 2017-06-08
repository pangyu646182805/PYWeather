package com.neuroandroid.pyweather.listener;

/**
 * Created by NeuroAndroid on 2017/6/7.
 * 当设置内容改变时候的回调
 */
public interface OnSettingConfigChangeListener {
    void onLineTypeChange();
    void onThemeStyleChange(boolean lightThemeStyle);
}
