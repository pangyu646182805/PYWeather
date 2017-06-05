package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.ui.fragment.WeatherDetailFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private ArrayList<CityBean.CityListBean> mAllCities;
    private final SparseArray<WeakReference<Fragment>> mFragmentArray = new SparseArray<>();
    private final List<Holder> mHolderList = new ArrayList<>();

    public WeatherPagerAdapter(Context context, FragmentManager fm, ArrayList<CityBean.CityListBean> allCities) {
        super(fm);
        this.mContext = context;
        mAllCities = allCities;

        Bundle bundle;
        for (CityBean.CityListBean bean : allCities) {
            bundle = new Bundle();
            bundle.putSerializable(Constant.CITY, bean);
            add(WeatherDetailFragment.class, bundle);
        }
    }

    @SuppressWarnings("synthetic-access")
    public void add(@NonNull final Class<? extends Fragment> className, final Bundle params) {
        final Holder mHolder = new Holder();
        mHolder.mClassName = className.getName();
        mHolder.mParams = params;

        final int mPosition = mHolderList.size();
        mHolderList.add(mPosition, mHolder);
        notifyDataSetChanged();
    }

    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null && mWeakFragment.get() != null) {
            return mWeakFragment.get();
        }
        return getItem(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final Fragment mFragment = (Fragment) super.instantiateItem(container, position);
        final WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null) {
            mWeakFragment.clear();
        }
        mFragmentArray.put(position, new WeakReference<>(mFragment));
        return mFragment;
    }

    @Override
    public Fragment getItem(int position) {
        final Holder mCurrentHolder = mHolderList.get(position);
        return Fragment.instantiate(mContext,
                mCurrentHolder.mClassName, mCurrentHolder.mParams);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);
        final WeakReference<Fragment> mWeakFragment = mFragmentArray.get(position);
        if (mWeakFragment != null) {
            mWeakFragment.clear();
        }
    }

    @Override
    public int getCount() {
        return mAllCities.size();
    }

    private final static class Holder {
        String mClassName;
        Bundle mParams;
    }
}
