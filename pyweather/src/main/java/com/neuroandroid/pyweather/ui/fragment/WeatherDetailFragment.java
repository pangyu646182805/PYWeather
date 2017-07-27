package com.neuroandroid.pyweather.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.WeatherAdapter;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.listener.OnSettingConfigChangeListener;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.mvp.contract.IWeatherContract;
import com.neuroandroid.pyweather.mvp.presenter.WeatherPresenter;
import com.neuroandroid.pyweather.provider.PYCityStore;
import com.neuroandroid.pyweather.utils.L;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.WeatherRefreshHeader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherDetailFragment extends BaseFragment<IWeatherContract.Presenter> implements IWeatherContract.View, OnSettingConfigChangeListener {
    @BindView(R.id.rv_weather)
    RecyclerView mRvWeather;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    private CityBean.CityListBean mCurrentCity;
    private WeatherFragment mWeatherFragment;
    private WeatherAdapter mWeatherAdapter;
    private int mScrolledY;
    private HeFenWeather.HeWeather5Bean mWeatherBean;
    private WeatherRefreshHeader mRefreshHeader;

    public HeFenWeather.HeWeather5Bean getWeatherBean() {
        return mWeatherBean;
    }

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
        mRefreshHeader = new WeatherRefreshHeader(mContext);
        mRefreshHeader.setRefreshHeaderColorFilter(SPUtils.getInt(mContext, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE));
        mRefreshLayout.setHeaderView(mRefreshHeader);
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
            if (mWeatherAdapter.getDataList().isEmpty()) {
                mRefreshLayout.startRefresh();
            }
        } else {
            mRefreshLayout.finishRefreshing();
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
        mRvWeather.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrolledY += dy;
                if (mWeatherAdapter.getRlHeaderHeight() != 0) {
                    if (mScrolledY >= mWeatherAdapter.getRlHeaderHeight()) {
                        // 展开WeatherCustomTitle
                        mWeatherFragment.expandWeatherCustomTitle();
                    } else {
                        // 收缩WeatherCustomTitle
                        mWeatherFragment.shrinkWeatherCustomTitle();
                    }
                }
            }
        });
    }

    @Override
    public void showWeatherInfo(HeFenWeather weatherInfo) {
        if (weatherInfo.getHeWeather5().size() > 1) {
            HeFenWeather.HeWeather5Bean heWeather5Bean = weatherInfo.getHeWeather5().get(0);
            if (heWeather5Bean.hasNullObject()) {
                weatherInfo.getHeWeather5().remove(0);
            }
        }
        mWeatherBean = weatherInfo.getHeWeather5().get(0);
        mWeatherFragment.setWeatherCustomTitle(mWeatherBean, 0);
        if (Constant.STATUS_OK.equals(mWeatherBean.getStatus())) {
            // 请求成功
            mWeatherAdapter.replaceAll(weatherInfo.getHeWeather5());
            long start = System.currentTimeMillis();
            updateWeatherInfo(weatherInfo.getHeWeather5().get(0));
            L.e("time : " + (System.currentTimeMillis() - start));
        } else {

        }
        mRefreshLayout.finishRefreshing();
    }

    @Override
    public void showTip(String tip) {
        ShowUtils.showToast("请求失败");
        mRefreshLayout.finishRefreshing();
    }

    /**
     * 刷新item
     */
    public void refreshItem(int position) {
        if (mWeatherAdapter != null) {
            mWeatherAdapter.refreshItem(position);
        }
    }

    /**
     * 更新天气信息
     */
    private void updateWeatherInfo(HeFenWeather.HeWeather5Bean weatherBean) {
        HeFenWeather.HeWeather5Bean.BasicBean basic = weatherBean.getBasic();
        HeFenWeather.HeWeather5Bean.NowBean now = weatherBean.getNow();
        int weatherCode = Integer.parseInt(now.getCond().getCode());
        HeFenWeather.HeWeather5Bean.DailyForecastBean.TmpBean tmp = weatherBean.getDaily_forecast().get(0).getTmp();
        int max = Integer.parseInt(tmp.getMax());
        int min = Integer.parseInt(tmp.getMin());
        PYCityStore.getInstance(mContext).update(basic.getId(), basic.getCity(), weatherCode, max, min, now.getCond().getTxt());
    }

    /**
     * 当折线图发生改变时候的监听
     */
    @Override
    public void onLineTypeChange() {
        refreshItem(0);
    }

    @Override
    public void onThemeStyleChange(boolean lightThemeStyle) {
        if (mWeatherAdapter != null) {
            mWeatherAdapter.setThemeStyleColor(lightThemeStyle);
            mRefreshHeader.setRefreshHeaderColorFilter(lightThemeStyle ? Color.WHITE : Color.BLACK);
        }
    }
}
