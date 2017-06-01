package com.neuroandroid.pyweather.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.widget.dialog.base.BaseDialog;

/**
 * 标题对话框(包含一个取消一个确定按钮)
 */
public class TitleDialog extends BaseDialog<TitleDialog> {
    public TitleDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_title_dialog;
    }

    @Override
    protected void initView() {
    }
}
