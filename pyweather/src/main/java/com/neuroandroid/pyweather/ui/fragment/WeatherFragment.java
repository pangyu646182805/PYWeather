package com.neuroandroid.pyweather.ui.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;

import com.itsronald.widget.ViewPagerIndicator;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.WeatherPagerAdapter;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.provider.PYCityStore;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.ui.activity.SettingActivity;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.WeatherTitleCustomWidget;

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
    @BindView(R.id.view_status_bar)
    View mStatusBar;

    private ArrayList<CityBean.CityListBean> mAllCities;
    private WeatherTitleCustomWidget mWeatherTitleCustomWidget;
    private WeatherPagerAdapter mWeatherPagerAdapter;
    private TitleBar.ImageAction mMenuAction;
    private TitleBar.ImageAction mSpinnerAction;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        initTitleBar(UIUtils.getString(R.string.app_name), false);
        setStatusBar(mStatusBar);
        mWeatherTitleCustomWidget = new WeatherTitleCustomWidget(mContext);
        getTitleBar().setCustomTitle(mWeatherTitleCustomWidget);
        setThemeStyle(SPUtils.getInt(mContext, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE) == Color.WHITE);
    }

    private void initImageAction(boolean lightThemeStyle) {
        mMenuAction = new TitleBar.ImageAction(lightThemeStyle ? R.drawable.ic_menu_white : R.drawable.ic_menu_black) {
            @Override
            public void performAction(View view) {
                getMainActivity().openDrawer();
            }
        };
        mSpinnerAction = new TitleBar.ImageAction(lightThemeStyle ? R.drawable.ic_more_vert_white : R.drawable.ic_more_vert_black) {
            @Override
            public void performAction(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, mStatusBar, Gravity.END);
                popupMenu.inflate(R.menu.menu_main);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.action_setting:
                            mIntent.setClass(mContext, SettingActivity.class);
                            UIUtils.toLayout(mIntent);
                            break;
                        case R.id.action_exit:
                            getMainActivity().finish();
                            break;
                    }
                    return false;
                });
                popupMenu.show();
            }
        };
        initLeftAction(mMenuAction);
        initRightAction(mSpinnerAction);
    }

    @Override
    protected void initData() {
        mAllCities = PYCityStore.getInstance(mContext).getAllCities();
        if (!mAllCities.isEmpty()) {
            // getTitleBar().setTitle(mAllCities.get(0).getCityZh());
            setWeatherCustomTitle(null, 0);
        } else {
            setReloadBtnText(UIUtils.getString(R.string.add_city));
            setStatusTextColor(Color.WHITE);
            showError(() -> getMainActivity().toCityManagerFragment(), UIUtils.getString(R.string.str_empty));
        }
        setUpViewPager();
    }

    @Override
    protected void initListener() {
        mVpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) getFragment(position);
                setWeatherCustomTitle(weatherDetailFragment.getWeatherBean(), position);
            }
        });
    }

    public void setWeatherCustomTitle(HeFenWeather.HeWeather5Bean weatherBean, int position) {
        mWeatherTitleCustomWidget.setWeatherBean(weatherBean, mAllCities.get(position).getCityZh());
    }

    public void expandWeatherCustomTitle() {
        mWeatherTitleCustomWidget.expand();
    }

    public void shrinkWeatherCustomTitle() {
        mWeatherTitleCustomWidget.shrink();
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    /**
     * ViewPager相关设置
     */
    private void setUpViewPager() {
        mWeatherPagerAdapter = new WeatherPagerAdapter(mContext, getChildFragmentManager(), mAllCities);
        mVpContent.setAdapter(mWeatherPagerAdapter);
        mVpContent.setOffscreenPageLimit(mWeatherPagerAdapter.getCount() - 1);
    }

    /**
     * 返回当前Fragment
     */
    public Fragment getCurrentFragment() {
        return getFragment(mVpContent.getCurrentItem());
    }

    /**
     * 根据position返回Fragment
     */
    public Fragment getFragment(int position) {
        return mWeatherPagerAdapter.getFragment(position);
    }

    /**
     * 刷新条目
     */
    public void onLineTypeChange() {
        for (int i = 0; i < mWeatherPagerAdapter.getCount(); i++) {
            WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) getFragment(i);
            weatherDetailFragment.onLineTypeChange();
        }
    }

    /**
     * 当设置配置发生改变
     */
    public void onThemeStyleChange(boolean lightThemeStyle) {
        for (int i = 0; i < mWeatherPagerAdapter.getCount(); i++) {
            WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) getFragment(i);
            weatherDetailFragment.onThemeStyleChange(lightThemeStyle);
        }
    }

    public void setThemeStyle(boolean lightThemeStyle) {
        mWeatherTitleCustomWidget.setThemeStyle(lightThemeStyle);
        getTitleBar().removeLeftAction(mMenuAction);
        getTitleBar().removeRightAction(mSpinnerAction);
        initImageAction(lightThemeStyle);
        setPagerIndicatorDotColor(lightThemeStyle);
    }

    /**
     * 设置指示器颜色
     */
    private void setPagerIndicatorDotColor(boolean lightThemeStyle) {
        mPagerIndicator.setSelectedDotColor(lightThemeStyle ? Color.WHITE : Color.BLACK);
        mPagerIndicator.setUnselectedDotColor(lightThemeStyle ? Color.parseColor("#99FFFFFF") : Color.parseColor("#99000000"));
    }
}
