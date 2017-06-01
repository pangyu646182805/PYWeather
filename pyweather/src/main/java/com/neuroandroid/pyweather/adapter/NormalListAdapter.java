package com.neuroandroid.pyweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.base.SelectAdapter;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NeuroAndroid on 2017/3/10.
 */

public class NormalListAdapter extends SelectAdapter<NormalListBean, NormalListAdapter.Holder> {
    private float mItemHeight;

    public NormalListAdapter(Context context, List<NormalListBean> dataList) {
        super(context, dataList);
    }

    public NormalListAdapter(Context context, List<NormalListBean> dataList, float itemHeight) {
        super(context, dataList);
        this.mItemHeight = itemHeight;
    }

    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_normal_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        holder.setDataAndRefreshUI(mDataList.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_done)
        ImageView mIvDone;
        @BindView(R.id.tv_text)
        NoPaddingTextView mTvText;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mItemHeight != 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mItemHeight);
                itemView.setLayoutParams(params);
            }
        }

        public void setDataAndRefreshUI(NormalListBean normalListBean) {
            mTvText.setText(normalListBean.getText());
            mIvDone.setVisibility(normalListBean.isSelected() ? View.VISIBLE : View.GONE);
        }
    }
}
