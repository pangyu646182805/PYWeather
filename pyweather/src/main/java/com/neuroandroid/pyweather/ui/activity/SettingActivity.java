package com.neuroandroid.pyweather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.event.CustomBackgroundEvent;
import com.neuroandroid.pyweather.event.LineTypeEvent;
import com.neuroandroid.pyweather.utils.ColorUtils;
import com.neuroandroid.pyweather.utils.PhotoGalleryUtils;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.dialog.ListDialog;
import com.neuroandroid.pyweather.widget.dialog.TitleDialog;
import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

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
        // 设置Switch颜色
        int stateChecked = android.R.attr.state_checked;
        ColorStateList thumbTintList = generateColorStateList(new int[][]{{stateChecked}, {-stateChecked}},
                UIUtils.getColor(R.color.colorPrimary), UIUtils.getColor(R.color.grayccc));
        mSwLifeIndex.setThumbTintList(thumbTintList);
        ColorStateList trackTintList = generateColorStateList(new int[][]{{stateChecked}, {-stateChecked}},
                ColorUtils.adjustAlpha(UIUtils.getColor(R.color.colorPrimary), 0.5f), UIUtils.getColor(R.color.grayccc));
        mSwLifeIndex.setTrackTintList(trackTintList);

        setLineTypeText(SPUtils.getInt(this, Constant.SP_LINE_TYPE, 0));
    }

    private ColorStateList generateColorStateList(int[][] states, @ColorInt int color1, @ColorInt int color2) {
        return new ColorStateList(states, new int[]{color1, color2});
    }

    @Override
    protected void initListener() {
        mRlMainBackground.setOnClickListener(view ->
                new TitleDialog(this).setCustomTitle("自定义主页背景")
                        .setLeftBtnText("清除背景")
                        .setRightBtnText("选择图片")
                        .setCancelClickListener((dialog, v) -> {
                            clearCustomBackground();
                            dialog.dismiss();
                        })
                        .setConfirmClickListener((dialog, v) -> {
                            PhotoGalleryUtils.choseImgFromGallery(this, 1, false, PickConfig.MODE_SINGLE_PICK);
                            dialog.dismiss();
                        }).show());
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

    /**
     * 清除自定义背景
     */
    private void clearCustomBackground() {
        String customBackground = SPUtils.getString(this, Constant.SP_CUSTOM_BACKGROUND, null);
        if (UIUtils.isEmpty(customBackground)) {
            ShowUtils.showToast("清除失败，当前并没有设置自定义背景");
        } else {
            SPUtils.putString(this, Constant.SP_CUSTOM_BACKGROUND, null);
            EventBus.getDefault().post(new CustomBackgroundEvent());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoGalleryUtils.PICK_REQUEST_CODE) {
            String path;
            if (requestCode == PhotoGalleryUtils.PICK_REQUEST_CODE) {
                ArrayList<String> paths = data.getStringArrayListExtra("data");
                path = paths.get(0);
            } else {
                Uri resultUri = UCrop.getOutput(data);
                path = resultUri.getPath();
            }
            SPUtils.putString(this, Constant.SP_CUSTOM_BACKGROUND, path);
            EventBus.getDefault().post(new CustomBackgroundEvent());
        }
    }
}
