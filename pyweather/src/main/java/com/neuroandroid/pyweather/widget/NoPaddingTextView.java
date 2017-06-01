package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by NeuroAndroid on 2017/2/8.
 */

public class NoPaddingTextView extends TextView {
    public NoPaddingTextView(Context context) {
        this(context, null);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setIncludeFontPadding(false);
    }
}
