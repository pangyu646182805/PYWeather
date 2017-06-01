package com.neuroandroid.pyweather.bean;

import android.view.View;

/**
 * Created by NeuroAndroid on 2017/3/9.
 */

public interface ISelect {
    int SINGLE_MODE = 1;
    int MULTIPLE_MODE = 2;

    String getText();

    void setText(String text);

    boolean isSelected();

    void setSelected(boolean selected);

    interface OnItemSelectedListener<T> {
        void onItemSelected(View view, int position, boolean isSelected, T t);
    }
}
