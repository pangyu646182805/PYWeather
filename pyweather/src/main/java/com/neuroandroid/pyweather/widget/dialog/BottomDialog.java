package com.neuroandroid.pyweather.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.DialogUtils;
import com.neuroandroid.pyweather.widget.dialog.base.BaseDialog;
import com.neuroandroid.pyweather.widget.dialog.base.WindowConfig;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/3/20.
 */

public class BottomDialog extends BaseDialog<BottomDialog> {
    @BindView(R.id.ll_take_photo)
    LinearLayout mLlTakePhoto;
    @BindView(R.id.ll_from_gallery)
    LinearLayout mLlFromGallery;
    @BindView(R.id.ll_cancel)
    LinearLayout mLlCancel;

    public static BottomDialog getBottomDialog(Context context) {
        return new BottomDialog(context, null);
    }

    public BottomDialog(@NonNull Context context, WindowConfig config) {
        super(context, DialogUtils.getCustomConfig(true, true, DEFAULT_DIM_AMOUNT, Gravity.BOTTOM, true, R.style.AnimationsGrowFromBottom, WindowConfig.LAYOUT_PARM_MATCH_PARENT));
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_bottom_dialog;
    }

    @Override
    protected void initView() {
        mLlCancel.setOnClickListener(view -> dismiss());
    }

    public BottomDialog setPhotoClickListener(OnDialogClickListener<BottomDialog> onClickListener) {
        mLlTakePhoto.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(BottomDialog.this, v);
            }
        });
        return this;
    }

    public BottomDialog setGalleryClickListener(OnDialogClickListener<BottomDialog> onClickListener) {
        mLlFromGallery.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(BottomDialog.this, v);
            }
        });
        return this;
    }
}
