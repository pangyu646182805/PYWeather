package com.neuroandroid.pyweather.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.itsronald.widget.ViewPagerIndicator;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.WeatherPagerAdapter;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.provider.PYCityStore;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public class WeatherFragment extends BaseFragment implements MainActivity.MainActivityFragmentCallbacks {
    @BindView(R.id.view_pager_indicator)
    ViewPagerIndicator mPagerIndicator;
    @BindView(R.id.vp_content)
    ViewPager mVpContent;

    private ArrayList<CityBean.CityListBean> mAllCities;

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
        initLeftAction(new TitleBar.ImageAction(R.drawable.ic_menu_white) {
            @Override
            public void performAction(View view) {
                getMainActivity().openDrawer();
            }
        });
    }

    @Override
    protected void initData() {
        // mPresenter.getWeatherInfo("杭州", Constant.APP_KEY);
        mAllCities = PYCityStore.getInstance(mContext).getAllCities();
        if (!mAllCities.isEmpty()) getTitleBar().setTitle(mAllCities.get(0).getCityZh());
        setUpViewPager();
    }

    @Override
    protected void initListener() {
        mVpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getTitleBar().setTitle(mAllCities.get(position).getCityZh());
            }
        });
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    /**
     * ViewPager相关设置
     */
    private void setUpViewPager() {
        WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(mContext, getChildFragmentManager(), mAllCities);
        mVpContent.setAdapter(weatherPagerAdapter);
        mVpContent.setOffscreenPageLimit(weatherPagerAdapter.getCount() - 1);
    }
}
