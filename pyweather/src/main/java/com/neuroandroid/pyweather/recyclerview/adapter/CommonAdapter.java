package com.neuroandroid.pyweather.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.listener.OnItemChildClickListener;
import com.neuroandroid.pyweather.listener.OnItemClickListener;
import com.neuroandroid.pyweather.recyclerview.holder.BaseViewHolder;

import java.util.List;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDataList;
    protected OnItemClickListener<T> mItemClickListener;
    protected OnItemChildClickListener<T> mItemChildClickListener;

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemChildClickListener(OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildClickListener = itemChildClickListener;
    }

    public CommonAdapter(Context context, int layoutId, List<T> dataList) {
        mContext = context;
        mLayoutId = layoutId;
        mDataList = dataList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, position, mDataList.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            if (mItemChildClickListener != null) {
                mItemChildClickListener.onItemChildClick(holder.itemView, position, mDataList.get(position));
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        } else {
            return mDataList.size();
        }
    }

    public abstract void onBind(BaseViewHolder holder, int position);
}
