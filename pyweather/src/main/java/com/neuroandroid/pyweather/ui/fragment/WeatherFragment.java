package com.neuroandroid.pyweather.ui.fragment;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public class WeatherFragment extends BaseFragment implements MainActivity.MainActivityFragmentCallbacks {
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        initTitleBar(UIUtils.getString(R.string.app_name));
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
