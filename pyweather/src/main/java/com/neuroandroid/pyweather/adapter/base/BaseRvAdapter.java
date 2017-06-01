package com.neuroandroid.pyweather.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.listener.OnItemChildClickListener;
import com.neuroandroid.pyweather.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeuroAndroid on 2017/3/9.
 */

public abstract class BaseRvAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> mDataList = new ArrayList<T>();
    protected Context mContext;
    protected OnItemClickListener<T> mItemClickListener;
    protected OnItemChildClickListener<T> mItemChildClickListener;

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemChildClickListener(OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildClickListener = itemChildClickListener;
    }

    public BaseRvAdapter(Context context, List<T> dataList) {
        this.mDataList = dataList;
        mContext = context;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, position, mDataList.get(position));
            }
        });
        onBindItemViewHolder((VH) holder, position);
    }

    public void add(T item) {
        this.mDataList.add(item);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void addAll(List<T> items) {
        int originSize = mDataList.size();
        mDataList.addAll(items);
        notifyItemRangeInserted(originSize, items.size());
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        } else {
            return mDataList.size();
        }
    }

    public T getItem(int position) {
        if (position > mDataList.size() - 1) {
            return null;
        }
        return mDataList.get(position);
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(final VH holder, int position);
}
