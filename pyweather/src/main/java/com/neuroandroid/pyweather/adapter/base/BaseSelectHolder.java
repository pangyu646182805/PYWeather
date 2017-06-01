package com.neuroandroid.pyweather.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/18.
 */

public class BaseSelectHolder<T extends ISelect> extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_done)
    ImageView mIvDone;
    @BindView(R.id.tv_text)
    NoPaddingTextView mTvText;
    private float mItemHeight;

    public BaseSelectHolder(View itemView, float itemHeight) {
        super(itemView);
        this.mItemHeight = itemHeight;
        ButterKnife.bind(this, itemView);
        if (mItemHeight != 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mItemHeight);
            itemView.setLayoutParams(params);
        }
    }

    public void setDataAndRefreshUI(T t) {
        mTvText.setText(t.getText());
        mIvDone.setVisibility(t.isSelected() ? View.VISIBLE : View.GONE);
    }
}
