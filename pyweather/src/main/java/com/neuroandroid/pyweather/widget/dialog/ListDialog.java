package com.neuroandroid.pyweather.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.NormalListAdapter;
import com.neuroandroid.pyweather.annotation.SelectMode;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.widget.dialog.base.BaseDialog;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/3/10.
 */

public class ListDialog extends BaseDialog<ListDialog> {
    @BindView(R.id.rv)
    RecyclerView mRv;
    private NormalListAdapter mNormalListAdapter;

    public ListDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_list_dialog;
    }

    @Override
    protected void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(mContext));
        mRv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.split).sizeResId(R.dimen.y2).build());

        RecyclerView.ItemAnimator animator = mRv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRv.getItemAnimator().setChangeDuration(333);
        mRv.getItemAnimator().setMoveDuration(333);

        /**
         * 单选模式
         * 默认没有标题栏和底部栏
         * 如果需要显示则调用
         * @see BaseDialog#showTitle()
         * @see BaseDialog#showButton()
         */
        setNoTitle();
        setNoButton();
    }

    /**
     * 自定义适配器
     */
    public ListDialog setAdapter(RecyclerView.Adapter adapter) {
        mRv.setAdapter(adapter);
        return this;
    }

    /**
     * 设置选择模式(单选或者多选)
     * 默认单选
     *
     * @param mode {@link SelectMode}
     */
    public ListDialog setSelectMode(@SelectMode int mode) {
        if (mNormalListAdapter != null) {
            mNormalListAdapter.setSelectedMode(mode);
        }
        return this;
    }

    /**
     * 设置(单选或者多选)模式的适配器
     * item高度自定义
     *
     * @param dataList             数据源
     * @param itemHeight           item高度
     * @param itemSelectedListener 选择监听器
     */
    public ListDialog setNormalListAdapter(List<NormalListBean> dataList, float itemHeight,
                                           ISelect.OnItemSelectedListener<NormalListBean> itemSelectedListener) {
        if (dataList == null && dataList.size() == 0) {
            throw new IllegalArgumentException("dataList is null or dataList.size is 0");
        }
        mNormalListAdapter = new NormalListAdapter(mContext, dataList, itemHeight);
        if (itemSelectedListener != null) {
            mNormalListAdapter.setItemSelectedListener(itemSelectedListener);
        }
        mRv.setAdapter(mNormalListAdapter);
        return this;
    }

    /**
     * 设置(单选或者多选)模式的适配器
     * item高度默认96px
     *
     * @param dataList             数据源
     * @param itemSelectedListener 选择监听器
     */
    public ListDialog setNormalListAdapter(List<NormalListBean> dataList, ISelect.OnItemSelectedListener<NormalListBean> itemSelectedListener) {
        setNormalListAdapter(dataList, 0, itemSelectedListener);
        return this;
    }
}
