package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.base.BaseRvAdapter;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class SelectCityAdapter extends BaseRvAdapter<String, SelectCityAdapter.Holder> {
    public static final int PROVINCE_LEVEL = 0;
    public static final int CITY_LEVEL = 1;
    public static final int AREA_LEVEL = 2;
    public int mCurrentLevel = PROVINCE_LEVEL;

    public int getCurrentLevel() {
        return mCurrentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        mCurrentLevel = currentLevel;
    }

    public SelectCityAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_select_city, parent, false));
    }

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        holder.onBind(mDataList.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final NoPaddingTextView mTvCity;

        public Holder(View itemView) {
            super(itemView);
            mTvCity = ButterKnife.findById(itemView, R.id.tv_city);
        }

        public void onBind(String city) {
            mTvCity.setText(city);
        }
    }
}
