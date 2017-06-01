package com.neuroandroid.pyweather.utils;

import android.view.Gravity;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.widget.dialog.base.WindowConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeuroAndroid on 2017/3/10.
 */

public class DialogUtils {
    public static List<NormalListBean> provideGenderDataList() {
        List<NormalListBean> dataList = new ArrayList<>();
        dataList.add(provideNormalListBean("男", true));
        dataList.add(provideNormalListBean("女", false));
        return dataList;
    }

    public static NormalListBean provideNormalListBean(String text, boolean selected) {
        NormalListBean normalListBean = new NormalListBean();
        normalListBean.setText(text);
        normalListBean.setSelected(selected);
        return normalListBean;
    }

    public static WindowConfig getDefaultConfig() {
        return getCustomConfig(true, true, 0.6f, Gravity.CENTER, true, R.style.custom_dialog_anim, WindowConfig.LAYOUT_PARM_WRAP_CONTENT);
    }

    public static WindowConfig getCustomConfig(boolean canceledOnTouchOutside, boolean cancelable, float dimAmount,
                                               int gravity, boolean needAnim, int dialogAnimResId, int lpWidth) {
        return new WindowConfig()
                .setCanceledOnTouchOutside(canceledOnTouchOutside)
                .setCancelable(cancelable)
                .setDimAmount(dimAmount)
                .setGravity(gravity)
                .setNeedAnim(needAnim)
                .setDialogAnimResId(dialogAnimResId)
                .setLpWidth(lpWidth);
    }
}
