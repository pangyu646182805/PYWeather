package com.neuroandroid.pyweather.listener;

import android.view.View;

/**
 * Created by NeuroAndroid on 2017/2/14.
 */

public interface OnItemLongClickListener<T> {
    void onItemLongClick(View view, int position, T t);
}
