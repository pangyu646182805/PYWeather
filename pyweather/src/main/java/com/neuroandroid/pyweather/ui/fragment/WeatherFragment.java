package com.neuroandroid.pyweather.ui.fragment;

import android.database.Cursor;
import android.provider.MediaStore;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.mvp.contract.IWeatherContract;
import com.neuroandroid.pyweather.mvp.presenter.WeatherPresenter;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.utils.L;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.WeatherRefreshHeader;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public class WeatherFragment extends BaseFragment<IWeatherContract.Presenter> implements MainActivity.MainActivityFragmentCallbacks, IWeatherContract.View {
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WeatherPresenter(Constant.BASE_URL, this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        initTitleBar(UIUtils.getString(R.string.app_name));

        WeatherRefreshHeader refreshHeader = new WeatherRefreshHeader(mContext);
        mRefreshLayout.setHeaderView(refreshHeader);
    }

    @Override
    protected void initData() {
        // mPresenter.getWeatherInfo("杭州", Constant.APP_KEY);

        Cursor songs = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while(songs.moveToNext()){
            //音乐名字
            String name = songs.getString(songs.getColumnIndex(MediaStore.Audio.Media.TITLE));
            //演唱者
            String artist = songs.getString(songs.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            //时间
            long time = songs.getLong(songs.getColumnIndex(MediaStore.Audio.Media.DURATION));
            //路径
            String file = songs.getString(songs.getColumnIndex(MediaStore.Audio.Media.DATA));

            L.e(name + " : " + artist);
        }
        songs.close();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                UIUtils.getHandler().postDelayed(() -> refreshLayout.finishRefreshing(), 2000);
            }
        });
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }

    @Override
    public void showWeatherInfo(HeFenWeather weatherInfo) {
        getTitleBar().setTitle(mGson.toJson(weatherInfo));
    }
}
