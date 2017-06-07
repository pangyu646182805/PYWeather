package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by NeuroAndroid on 2017/3/8.
 */

public class LoadingLayout extends FrameLayout {
    public static final int STATUS_HIDE = 1001;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_NO_NET = 2;
    public static final int STATUS_NO_DATA = 3;
    private int mCurrentStatus = STATUS_HIDE;
    @BindView(R.id.fl_error)
    FrameLayout mFlError;
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;
    @BindView(R.id.tv_status)
    NoPaddingTextView mTvStatus;
    @BindView(R.id.btn_reload)
    StateButton mBtnReload;
    private Context mContext;

    public LoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View.inflate(mContext, R.layout.layout_loading, this);
        ButterKnife.bind(this);
        switchView();
    }

    /**
     * 隐藏视图
     */
    public void hide() {
        mCurrentStatus = STATUS_HIDE;
        switchView();
    }

    /**
     * 设置状态
     */
    public void setStatus(@LoadingState int status) {
        mCurrentStatus = status;
        switchView();
    }

    /**
     * 设置状态
     */
    public void setStatus(@LoadingState int status, String statusStr) {
        mCurrentStatus = status;
        if (UIUtils.isEmpty(statusStr.trim())) {
            mTvStatus.setVisibility(View.GONE);
        } else {
            mTvStatus.setText(statusStr);
        }
        switchView();
    }

    public void setReloadBtnText(String reloadText) {
        mBtnReload.setText(reloadText);
    }

    public void setStatusTextColor(int color) {
        mTvStatus.setTextColor(color);
    }

    /**
     * 获取状态
     */
    public int getStatus() {
        return mCurrentStatus;
    }

    private void switchView() {
        switch (mCurrentStatus) {
            case STATUS_LOADING:
                setVisibility(VISIBLE);
                mFlError.setVisibility(GONE);
                mRlLoading.setVisibility(View.VISIBLE);
                break;
            case STATUS_NO_DATA:
            case STATUS_NO_NET:
                setVisibility(VISIBLE);
                mFlError.setVisibility(VISIBLE);
                mRlLoading.setVisibility(View.GONE);
                break;
            case STATUS_HIDE:
                setVisibility(GONE);
                break;
        }
    }

    @OnClick(R.id.btn_reload)
    public void onClick() {
        if (mOnRetryListener != null) {
            mOnRetryListener.onRetry();
        }
    }

    private OnRetryListener mOnRetryListener;

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        mOnRetryListener = onRetryListener;
    }

    /**
     * 点击重试监听器
     */
    public interface OnRetryListener {
        void onRetry();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_LOADING, STATUS_NO_NET, STATUS_NO_DATA})
    public @interface LoadingState {}
}
