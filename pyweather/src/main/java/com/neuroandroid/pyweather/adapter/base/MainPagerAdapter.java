package com.neuroandroid.pyweather.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.base.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/5/1.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<BaseFragment> fragments;

    public MainPagerAdapter(FragmentManager fm, List<String> titles, List<BaseFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        if (titles != null) {
            return titles.size();
        } else {
            return fragments.size();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return super.getPageTitle(position);
        } else {
            return titles.get(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
        // super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
