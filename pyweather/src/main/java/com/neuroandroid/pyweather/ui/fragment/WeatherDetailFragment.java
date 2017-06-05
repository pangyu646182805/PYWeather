package com.neuroandroid.pyweather.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.WeatherAdapter;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.mvp.contract.IWeatherContract;
import com.neuroandroid.pyweather.mvp.presenter.WeatherPresenter;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.WeatherRefreshHeader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherDetailFragment extends BaseFragment<IWeatherContract.Presenter> implements IWeatherContract.View {
    @BindView(R.id.rv_weather)
    RecyclerView mRvWeather;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    private CityBean.CityListBean mCurrentCity;
    private WeatherFragment mWeatherFragment;
    private WeatherAdapter mWeatherAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_weather_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WeatherPresenter(Constant.BASE_URL, this);
    }

    @Override
    protected void initView() {
        WeatherRefreshHeader refreshHeader = new WeatherRefreshHeader(mContext);
        mRefreshLayout.setHeaderView(refreshHeader);
        mRefreshLayout.setHeaderHeight(UIUtils.getDimen(R.dimen.y64));
        mRefreshLayout.setMaxHeadHeight(UIUtils.getDimen(R.dimen.y1280));
        mRefreshLayout.setEnableLoadmore(false);

        mCurrentCity = (CityBean.CityListBean) getArguments().getSerializable(Constant.CITY);
        mWeatherFragment = (WeatherFragment) getParentFragment();

        mRvWeather.setLayoutManager(new LinearLayoutManager(mContext));
        mWeatherAdapter = new WeatherAdapter(mContext, new ArrayList<>());
        mRvWeather.setAdapter(mWeatherAdapter);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {  // 不可见 -> 可见
            // mRefreshLayout.startRefresh();
        }
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.getWeatherInfo(mCurrentCity.getCityZh(), Constant.APP_KEY);
            }
        });
    }

    @Override
    public void showWeatherInfo(HeFenWeather weatherInfo) {
        HeFenWeather.HeWeather5Bean weatherBean = weatherInfo.getHeWeather5().get(0);
        if (Constant.STATUS_OK.equals(weatherBean.getStatus())) {
            // 请求成功
            mWeatherAdapter.replaceAll(weatherInfo.getHeWeather5());
        } else {

        }
        mRefreshLayout.finishRefreshing();
    }

    @Override
    public void showTip(String tip) {
        ShowUtils.showToast("请求失败");
        mRefreshLayout.finishRefreshing();
    }
}
