package com.neuroandroid.pyweather.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.SelectCityAdapter;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.event.CityManageEvent;
import com.neuroandroid.pyweather.loader.CityLoader;
import com.neuroandroid.pyweather.loader.WrappedAsyncTaskLoader;
import com.neuroandroid.pyweather.utils.L;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class SelectedCityActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<CityBean> {
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.rv_city)
    RecyclerView mRvCity;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    private SelectCityAdapter mSelectCityAdapter;
    private CityBean mCityBean;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout.setTitle(UIUtils.getString(R.string.select_province));
        mRvCity.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.y2).colorResId(R.color.split).build());
        mRvCity.setLayoutManager(new LinearLayoutManager(this));
        mSelectCityAdapter = new SelectCityAdapter(this, new ArrayList<>());
        mRvCity.setAdapter(mSelectCityAdapter);
    }

    @Override
    protected void initData() {
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<CityBean> onCreateLoader(int id, Bundle args) {
        return new AsyncCityLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<CityBean> loader, CityBean data) {
        mCityBean = data;
        mSelectCityAdapter.replaceAll(data.getProvinceList());
        mSelectCityAdapter.setItemClickListener((view, position, city) -> {
            long start = System.currentTimeMillis();
            switch (mSelectCityAdapter.getCurrentLevel()) {
                case SelectCityAdapter.PROVINCE_LEVEL:
                    selectProvince(city);
                    break;
                case SelectCityAdapter.CITY_LEVEL:
                    selectCity(city);
                    break;
                case SelectCityAdapter.AREA_LEVEL:
                    ArrayList<CityBean.CityListBean> dataList = mCityBean.getDataList();
                    for (int i = 0, size = dataList.size(); i < size; i++) {
                        CityBean.CityListBean cityListBean = dataList.get(i);
                        if (mCollapsingToolbarLayout.getTitle().toString().equals(cityListBean.getLeaderZh())
                                && city.equals(cityListBean.getCityZh())) {
                            EventBus.getDefault().post(new CityManageEvent().setSelectCity(cityListBean));
                            break;
                        }
                    }
                    finish();
                    break;
            }
            L.e("time : " + (System.currentTimeMillis() - start));
        });
    }

    @Override
    public void onLoaderReset(Loader<CityBean> loader) {

    }

    /**
     * 选择省份
     * 传递进来辽宁则填充辽宁的城市
     */
    private void selectProvince(String province) {
        ArrayList<CityBean.CityListBean> dataList = mCityBean.getDataList();
        ArrayList<String> cityList = new ArrayList<>();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            CityBean.CityListBean cityListBean = dataList.get(i);
            String leaderZh = cityListBean.getLeaderZh();
            String cityZh = cityListBean.getCityZh();
            if (province.equals(cityListBean.getProvinceZh())) {
                if (cityList.size() == 0) {
                    cityList.add(leaderZh);
                } else {
                    if (!cityList.contains(leaderZh)) {
                        cityList.add(leaderZh);
                    }
                }
                if (province.equals(cityZh)) break;
            }
        }
        mSelectCityAdapter.setCurrentLevel(SelectCityAdapter.CITY_LEVEL);
        mSelectCityAdapter.replaceAll(cityList);
        mCollapsingToolbarLayout.setTitle(province);
    }

    /**
     * 选择城市
     * 传递进来沈阳则填充沈阳的区域
     */
    private void selectCity(String city) {
        ArrayList<CityBean.CityListBean> dataList = mCityBean.getDataList();
        ArrayList<String> areaList = new ArrayList<>();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            CityBean.CityListBean cityListBean = dataList.get(i);
            String leaderZh = cityListBean.getLeaderZh();
            String cityZh = cityListBean.getCityZh();
            if (city.equals(leaderZh)) {
                areaList.add(cityZh);
            }
        }
        mSelectCityAdapter.setCurrentLevel(SelectCityAdapter.AREA_LEVEL);
        mSelectCityAdapter.replaceAll(areaList);
        mCollapsingToolbarLayout.setTitle(city);
    }

    /**
     * 异步加载城市列表
     */
    private static class AsyncCityLoader extends WrappedAsyncTaskLoader<CityBean> {
        public AsyncCityLoader(Context context) {
            super(context);
        }

        @Override
        public CityBean loadInBackground() {
            return CityLoader.getAllCities(getContext());
        }
    }

    @Override
    public void onBackPressed() {
        if (mSelectCityAdapter.getCurrentLevel() == SelectCityAdapter.AREA_LEVEL) {
            selectProvince(getProvinceByCity(mCollapsingToolbarLayout.getTitle().toString()));
        } else if (mSelectCityAdapter.getCurrentLevel() == SelectCityAdapter.CITY_LEVEL) {
            mSelectCityAdapter.replaceAll(mCityBean.getProvinceList());
            mSelectCityAdapter.setCurrentLevel(SelectCityAdapter.PROVINCE_LEVEL);
            mCollapsingToolbarLayout.setTitle(UIUtils.getString(R.string.select_province));
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 根据城市的名字获取省份的名字
     */
    private String getProvinceByCity(String city) {
        String province = null;
        ArrayList<CityBean.CityListBean> dataList = mCityBean.getDataList();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (city.equals(dataList.get(i).getCityZh())) {
                province = dataList.get(i).getProvinceZh();
                break;
            }
        }
        return province;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
