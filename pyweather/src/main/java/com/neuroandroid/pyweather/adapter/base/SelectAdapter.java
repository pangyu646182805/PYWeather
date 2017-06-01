package com.neuroandroid.pyweather.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.neuroandroid.pyweather.annotation.SelectMode;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.listener.OnItemClickListener;
import com.neuroandroid.pyweather.utils.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by NeuroAndroid on 2017/3/9.
 * 单选或者多选adapter
 */

public abstract class SelectAdapter<T extends ISelect, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private int mCurrentMode = ISelect.SINGLE_MODE;
    private int mPrePos;
    public boolean isSelectMode = true;
    private OnItemClickListener<T> mItemClickListener;
    private ISelect.OnItemSelectedListener<T> mItemSelectedListener;
    protected Context mContext;
    private float mItemHeight;

    public float getItemHeight() {
        return mItemHeight;
    }

    public SelectAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public SelectAdapter(Context context, List<T> dataList, float itemHeight) {
        this(context, dataList);
        this.mItemHeight = itemHeight;
    }

    /**
     * 得到勾选的位置
     */
    private int getCheckedPosition(List<T> dataList) {
        int index = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelected()) {
                index = i;
                break;
            }
        }
        if (index == 0) {
            /**
             * 如果index == 0 保证第一个被勾选
             */
            dataList.get(0).setSelected(true);
        }
        return index;
    }

    public void setItemSelectedListener(ISelect.OnItemSelectedListener<T> itemSelectedListener) {
        mItemSelectedListener = itemSelectedListener;
    }

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setCheckedPos(int prePos) {
        mPrePos = prePos;
    }

    public void setCheckedPos() {
        if (mDataList != null) {
            mPrePos = getCheckedPosition(mDataList);
        }
    }

    protected List<T> mDataList = new ArrayList<T>();
    private final HashSet<T> selectedBeans = new HashSet<>();

    /**
     * 更新选择模式
     */
    public void updateSelectMode(boolean isSelect) {
        if (isSelectMode != isSelect) {
            isSelectMode = isSelect;
            // resetData();
            notifyDataSetChanged();
        }
    }

    /**
     * 清除选择
     */
    private void clearSelect() {
        for (ISelect bean : mDataList) {
            bean.setSelected(false);
        }
    }

    public void setSelectedMode(@SelectMode int mode) {
        mCurrentMode = mode;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> performClick(view, position));
        // holder.itemView.setBackgroundResource(R.drawable.dialog_item_selector);
        onBindItemViewHolder((VH) holder, position);
    }

    public void performClick(View itemView, int position) {
        /**
         * 如果当前选中的和之前的position一样则不作处理
         * 保证至少有一个被选中
         * 但是开放选择接口
         */
        /*final T bean = mDataList.get(position);

        if (isSelectMode) {
            if (bean.isSelected()) {
                if (mItemSelectedListener != null) {
                    mItemSelectedListener.onItemSelected(itemView, position, true, bean);
                }
                return;
            }
            boolean selected = !bean.isSelected();
            bean.setSelected(selected);
            dispatchSelected(itemView, position, bean, selected);
            if (mCurrentMode == ISelect.SINGLE_MODE && position != mPrePos && bean.isSelected()) {
                mDataList.get(mPrePos).setSelected(false);
                dispatchSelected(itemView, mPrePos, bean, false);
                notifyItemChanged(mPrePos);
            }
            notifyItemRangeChanged(position, 1);
            mPrePos = position;
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onItemSelected(itemView, position, true, bean);
            }
        } else {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, position, bean);
            }
        }*/
        final T bean = mDataList.get(position);
        L.e("bean : " + bean.isSelected());

        if (isSelectMode) {
            if (mCurrentMode == ISelect.SINGLE_MODE && bean.isSelected()) {
                if (mItemSelectedListener != null) {
                    mItemSelectedListener.onItemSelected(itemView, position, true, bean);
                }
                return;
            }
            boolean selected = !bean.isSelected();
            bean.setSelected(selected);
            dispatchSelected(itemView, position, bean, selected);
            if (mCurrentMode == ISelect.SINGLE_MODE && position != mPrePos && bean.isSelected()) {
                mDataList.get(mPrePos).setSelected(false);
                dispatchSelected(itemView, mPrePos, bean, false);
                notifyItemChanged(mPrePos);
            }
            notifyItemRangeChanged(position, 1);
            mPrePos = position;
        } else {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, position, bean);
            }
        }
    }

    private void dispatchSelected(View itemView, int position, T bean, boolean isSelected) {
        /*if (isSelected) {
            selectedBeans.add(bean);
        } else {
            selectedBeans.remove(bean);
            if (selectedBeans.isEmpty()) {
                L.e("remove bean : " + bean.getText());
            }
        }*/
        if (isSelected) {
            selectedBeans.add(bean);
        } else {
            selectedBeans.remove(bean);
        }
        if (mItemSelectedListener != null) {
            mItemSelectedListener.onItemSelected(itemView, position, isSelected, bean);
        }
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

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        } else {
            return mDataList.size();
        }
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemViewHolder(final VH holder, int position);
}
