package com.neuroandroid.pyweather.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.DialogUtils;
import com.neuroandroid.pyweather.widget.dialog.base.BaseDialog;
import com.neuroandroid.pyweather.widget.dialog.base.WindowConfig;

/**
 * Created by Administrator on 2017/3/19.
 */

public class LoadingDialog extends BaseDialog<LoadingDialog> {
    public LoadingDialog(@NonNull Context context, WindowConfig config) {
        super(context, config);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView() {

    }

    private static LoadingDialog mLoadingDialog;

    public static void showLoadingDialog(Context context) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(context, DialogUtils.getCustomConfig(false, true, 0.6f, Gravity.CENTER, false, -1, WindowConfig.LAYOUT_PARM_WRAP_CONTENT));
        }
        mLoadingDialog.show();
    }

    public static void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        }
    }
}
