package com.neuroandroid.pyweather.ui.fragment;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.ui.activity.MainActivity;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public class CityManageFragment extends BaseFragment implements MainActivity.MainActivityFragmentCallbacks {
    public static CityManageFragment newInstance() {
        return new CityManageFragment();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_city_manage;
    }

    @Override
    protected void initView() {

    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
