package com.neuroandroid.pyweather.widget.dialog.base;

import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/19.
 */

public class WindowConfig {
    public static final int LAYOUT_PARM_MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT;
    public static final int LAYOUT_PARM_WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;

    /**
     * 点击空白区域可以取消dialog
     */
    private boolean canceledOnTouchOutside;
    /**
     * 点击back键可以取消dialog
     */
    private boolean cancelable;
    /**
     * 窗体透明度
     */
    private float dimAmount;

    private int gravity;

    /**
     * 是否需要动画
     */
    private boolean needAnim;

    /**
     * 对话框的动画资源
     */
    private int dialogAnimResId;

    /**
     * 对话框宽度
     */
    private int lpWidth;

    /**
     * 对话框高度
     */
    private int lpHeight;

    public int getLpWidth() {
        return lpWidth;
    }

    public WindowConfig setLpWidth(int lpWidth) {
        this.lpWidth = lpWidth;
        return this;
    }

    public int getLpHeight() {
        return lpHeight;
    }

    public WindowConfig setLpHeight(int lpHeight) {
        this.lpHeight = lpHeight;
        return this;
    }

    public int getDialogAnimResId() {
        return dialogAnimResId;
    }

    public WindowConfig setDialogAnimResId(int dialogAnimResId) {
        this.dialogAnimResId = dialogAnimResId;
        return this;
    }

    public boolean isCanceledOnTouchOutside() {
        return canceledOnTouchOutside;
    }

    public WindowConfig setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public WindowConfig setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public WindowConfig setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public WindowConfig setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public boolean isNeedAnim() {
        return needAnim;
    }

    public WindowConfig setNeedAnim(boolean needAnim) {
        this.needAnim = needAnim;
        return this;
    }
}
