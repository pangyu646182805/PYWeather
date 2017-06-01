package com.neuroandroid.pyweather.listener;

import android.view.View;

/**
 * Created by NeuroAndroid on 2017/3/23.
 */

public interface OnItemChildClickListener<T> {
    void onItemChildClick(View view, int position, T t);
}
