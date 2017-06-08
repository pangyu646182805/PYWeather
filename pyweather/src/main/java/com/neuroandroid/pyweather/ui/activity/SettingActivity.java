package com.neuroandroid.pyweather.ui.activity;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.event.LineTypeEvent;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.dialog.ListDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/7.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_color_title)
    NoPaddingTextView mTvColorTitle;
    @BindView(R.id.rl_main_background)
    RelativeLayout mRlMainBackground;
    @BindView(R.id.tv_line_type)
    NoPaddingTextView mTvLineType;
    @BindView(R.id.rl_line_type)
    RelativeLayout mRlLineType;
    @BindView(R.id.sw_life_index)
    SwitchCompat mSwLifeIndex;
    @BindView(R.id.rl_life_index)
    RelativeLayout mRlLifeIndex;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initTitleBar(UIUtils.getString(R.string.setting));
        initLeftAction(new TitleBar.ImageAction(R.drawable.ic_arrow_back) {
            @Override
            public void performAction(View view) {
                onBackPressed();
            }
        });

        setLineTypeText(SPUtils.getInt(this, Constant.SP_LINE_TYPE, 0));
    }

    @Override
    protected void initListener() {
        mRlMainBackground.setOnClickListener(view -> {
            mIntent.setClass(this, CustomBackgroundActivity.class);
            UIUtils.toLayout(mIntent);
        });
        mRlLineType.setOnClickListener(view -> {
            ListDialog listDialog = new ListDialog(this);
            listDialog.setNormalListAdapter(generateDataList(SPUtils.getInt(this, Constant.SP_LINE_TYPE, 0) == 0), (v, position, isSelected, normalListBean) -> {
                if (isSelected) {
                    setLineTypeText(position);
                    switch (position) {
                        case 0:  // 折线图
                            SPUtils.putInt(SettingActivity.this, Constant.SP_LINE_TYPE, 0);
                            break;
                        case 1:  // 曲线图
                            SPUtils.putInt(SettingActivity.this, Constant.SP_LINE_TYPE, 1);
                            break;
                    }
                    EventBus.getDefault().post(new LineTypeEvent());
                    listDialog.dismiss();
                }
            }).setSelectMode(ISelect.SINGLE_MODE);
            listDialog.show();
        });
    }

    private List<NormalListBean> generateDataList(boolean lineChart) {
        List<NormalListBean> dataList = new ArrayList<>();
        NormalListBean normalListBean = new NormalListBean();
        normalListBean.setText("折线图");
        normalListBean.setSelected(lineChart);
        dataList.add(normalListBean);
        normalListBean = new NormalListBean();
        normalListBean.setText("曲线图");
        normalListBean.setSelected(!lineChart);
        dataList.add(normalListBean);
        return dataList;
    }

    private void setLineTypeText(int lineType) {
        switch (lineType) {
            case 0:
                mTvLineType.setText("折线图");
                break;
            case 1:
                mTvLineType.setText("曲线图");
                break;
        }
    }
}
