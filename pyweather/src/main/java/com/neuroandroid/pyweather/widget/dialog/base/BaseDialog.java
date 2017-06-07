package com.neuroandroid.pyweather.widget.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.DialogUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by NeuroAndroid on 2017/3/8.
 */

public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {
    public static final float DEFAULT_DIM_AMOUNT = 0.6f;
    @Nullable
    @BindView(R.id.tv_title)
    NoPaddingTextView mTvTitle;
    @Nullable
    @BindView(R.id.tv_left)
    NoPaddingTextView mTvLeft;
    @Nullable
    @BindView(R.id.tv_right)
    NoPaddingTextView mTvRight;
    @Nullable
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @Nullable
    @BindView(R.id.ll_button)
    LinearLayout mLlButton;
    protected Context mContext;
    private Unbinder mUnbinder;

    public BaseDialog(@NonNull Context context) {
        this(context, DialogUtils.getDefaultConfig());
    }

    public BaseDialog(@NonNull Context context, WindowConfig config) {
        super(context);
        setDialogTheme();
        View view = View.inflate(context, attachLayoutRes(), null);
        setContentView(view);
        mUnbinder = ButterKnife.bind(this, view);
        mContext = context;
        init(config);
        initView();
        if (mTvLeft != null && mTvRight != null) {
            /**
             * 如果需要修改按钮文本请在外部调用setLeftBtnText()方法
             */
            mTvLeft.setText("取消");
            mTvRight.setText("确定");
            mTvLeft.setOnClickListener(view1 -> dismiss());
        }
    }

    private void setDialogTheme() {
        // android:windowNoTitle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // android:windowBackground
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // android:backgroundDimEnabled默认是true的
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void init(WindowConfig config) {
        // 点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(config.isCanceledOnTouchOutside());
        // 点击back键可以取消dialog
        this.setCancelable(config.isCancelable());
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // 让Dialog显示在屏幕的底部
        window.setGravity(config.getGravity());
        // 设置窗口出现和窗口隐藏的动画
        if (config.isNeedAnim())
            window.setWindowAnimations(config.getDialogAnimResId());
        // 设置Dialog的宽高属性
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = config.getLpWidth();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 1.0f;
        lp.dimAmount = config.getDimAmount();
        window.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
    }

    public T setCustomTitle(CharSequence title) {
        if (mTvTitle != null)
            mTvTitle.setText(title);
        return (T) this;
    }

    public T setLeftBtnText(CharSequence leftBtnText) {
        if (mTvLeft != null)
            mTvLeft.setText(leftBtnText);
        return (T) this;
    }

    public T setRightBtnText(CharSequence rightBtnText) {
        if (mTvRight != null)
            mTvRight.setText(rightBtnText);
        return (T) this;
    }

    public T setConfirmClickListener(OnDialogClickListener<T> onDialogClickListener) {
        if (mTvRight != null) {
            mTvRight.setOnClickListener(view -> {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onClick((T) this, view);
                }
            });
        }
        return (T) this;
    }

    public T setCancelClickListener(OnDialogClickListener<T> onDialogClickListener) {
        if (mTvLeft != null) {
            mTvLeft.setOnClickListener(view -> {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onClick((T) this, view);
                }
            });
        }
        return (T) this;
    }

    /**
     * 隐藏标题栏
     */
    public T setNoTitle() {
        if (mLlTitle != null) {
            mLlTitle.setVisibility(View.GONE);
        }
        return (T) this;
    }

    /**
     * 隐藏底部栏
     */
    public T setNoButton() {
        if (mLlButton != null) {
            mLlButton.setVisibility(View.GONE);
        }
        return (T) this;
    }

    /**
     * 显示标题栏
     */
    public T showTitle() {
        if (mLlTitle != null) {
            mLlTitle.setVisibility(View.VISIBLE);
        }
        return (T) this;
    }

    /**
     * 显示底部栏
     */
    public T showButton() {
        if (mLlButton != null) {
            mLlButton.setVisibility(View.VISIBLE);
        }
        return (T) this;
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    protected abstract void initView();

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
    }

    public interface OnDialogClickListener<T> {
        void onClick(T dialog, View view);
    }
}
