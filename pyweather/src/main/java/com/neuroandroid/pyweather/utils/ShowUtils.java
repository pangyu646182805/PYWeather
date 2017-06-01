package com.neuroandroid.pyweather.utils;

import android.widget.Toast;

/**
 * Created by Administrator on 2016/2/19.
 */
public class ShowUtils {
    public static Toast mToast;

    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(UIUtils.getContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
