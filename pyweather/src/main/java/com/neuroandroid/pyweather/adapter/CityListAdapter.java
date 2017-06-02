package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.base.BaseRvAdapter;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class CityListAdapter extends BaseRvAdapter<CityBean.CityListBean, CityListAdapter.Holder> {
    public CityListAdapter(Context context, List<CityBean.CityListBean> dataList) {
        super(context, dataList);
    }

    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_city_list, parent, false));
    }

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        holder.onBind(mDataList.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_circle)
        CircleImageView mIvCircle;
        @BindView(R.id.tv_first_word)
        NoPaddingTextView mTvFirstWord;
        @BindView(R.id.tv_title)
        NoPaddingTextView mTvTitle;
        @BindView(R.id.tv_sub_title)
        NoPaddingTextView mTvSubTitle;
        @BindView(R.id.tv_temp)
        NoPaddingTextView mTvTemp;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(CityBean.CityListBean cityListBean) {
            String cityZh = cityListBean.getCityZh();
            int max = cityListBean.getMax();
            int min = cityListBean.getMin();
            // int weatherCode = cityListBean.getWeatherCode();
            String weatherDesc = cityListBean.getWeatherDesc();
            mTvTitle.setText(cityZh);
            mTvFirstWord.setText(cityZh.substring(0, 1));
            if (max == 100 || min == 100) {
                mTvTemp.setText("N/A°");
            }
            if (UIUtils.isEmpty(weatherDesc)) {
                mTvSubTitle.setText("N/A");
            } else {
                mTvSubTitle.setText(weatherDesc);
            }
        }
    }
}
