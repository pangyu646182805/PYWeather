package com.neuroandroid.pyweather.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.neuroandroid.pyweather.base.BaseApplication;

/**
 * Created by NeuroAndroid on 2017/2/8.
 */

public class UIUtils {
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static void toLayout(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    public static float getDimen(int dimen) {
        return getResources().getDimension(dimen);
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 得到String.xml中的字符串数组
     */
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    public static String getEditTextStr(EditText et) {
        return et.getText().toString();
    }

    public static String getTextViewStr(TextView tv) {
        return tv.getText().toString();
    }

    /**
     * @param tv
     * @param str  要设置的文本
     * @param emptyStr  要设置的文本为空时需要设置的文本
     */
    public static void setText(TextView tv, String str, String emptyStr) {
        tv.setText(isEmpty(str) ? emptyStr : str);
    }

    public static void setImage(ImageView iv, int resId) {
        iv.setImageResource(resId);
    }

    /**
     * 获取文本边框
     *
     * @param paint
     * @param text
     * @param rect
     */
    public static void getTextBounds(Paint paint, String text, Rect rect) {
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    public static float getRawSize(Context context, int unit, float size) {
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }
}
