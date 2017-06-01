package com.neuroandroid.pyweather.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.SystemUtils;
import com.neuroandroid.pyweather.widget.LoadingLayout;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by NeuroAndroid on 2017/3/8.
 */

public abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView<T> {
    /**
     * 把 LoadingLayout 放在基类统一处理，@Nullable 表明 View 可以为 null
     */
    @Nullable
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;

    @Nullable
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    protected T mPresenter;
    private Unbinder mUnbinder;
    /**
     * 是否支持沉浸式状态栏
     */
    public boolean mImmersive;
    protected Gson mGson;
    protected Intent mIntent;
    /**
     * 能不能加载更多
     * 默认可以
     */
    protected boolean mLoadMoreEnable = true;
    protected int mStatusBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        mGson = new Gson();
        mIntent = new Intent();
        if (supportImmersive())
            mImmersive = SystemUtils.setTranslateStatusBar(this);
        if (mImmersive)
            mStatusBarHeight = SystemUtils.getStatusHeight(this);
        if (useEventBus())
            EventBus.getDefault().register(this);
        mUnbinder = ButterKnife.bind(this);
        initPresenter();
        initView();
        initData();
        initListener();
    }

    /**
     * 是否支持沉浸式状态栏(默认支持)
     */
    protected boolean supportImmersive() {
        return true;
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    protected void initPresenter() {
    }

    protected abstract void initView();

    protected void initData() {
    }

    protected void initListener() {
    }

    @Override
    public void showLoading() {
        showPageLoading();
    }

    protected void showPageLoading() {
        if (mLoadingLayout != null) {
            mLoadingLayout.setStatus(LoadingLayout.STATUS_LOADING);
        }
    }

    protected void showLoadingDialog() {
        LoadingDialog.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        hidePageLoading();
    }

    protected void hidePageLoading() {
        if (mLoadingLayout != null) {
            mLoadingLayout.hide();
        }
    }

    protected void dismissLoadingDialog() {
        LoadingDialog.dismissLoadingDialog();
    }

    @Override
    public void showError(LoadingLayout.OnRetryListener onRetryListener) {
        showError(onRetryListener, "");
    }

    @Override
    public void showError(LoadingLayout.OnRetryListener onRetryListener, String statusStr) {
        if (mLoadingLayout != null) {
            mLoadingLayout.setStatus(LoadingLayout.STATUS_NO_NET, statusStr);
            mLoadingLayout.setOnRetryListener(onRetryListener);
        }
    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTip(String tip) {
    }

    protected void initTitleBar(CharSequence title) {
        if (mTitleBar != null) {
            mTitleBar.setImmersive(mImmersive);
            mTitleBar.setTextColor(Color.WHITE);
            mTitleBar.setTitle(title);
        }
    }

    protected void initTitleBar(View title) {
        if (mTitleBar != null) {
            mTitleBar.setImmersive(mImmersive);
            mTitleBar.setTextColor(Color.WHITE);
            mTitleBar.setCustomTitle(title);
        }
    }

    protected void initLeftAction(TitleBar.Action action) {
        if (mTitleBar != null) {
            mTitleBar.addLeftAction(action);
        }
    }

    protected void initRightAction(TitleBar.Action action) {
        if (mTitleBar != null) {
            mTitleBar.addRightAction(action);
        }
    }

    protected void initRightActions(TitleBar.ActionList actionList) {
        if (mTitleBar != null) {
            mTitleBar.addRightActions(actionList);
        }
    }

    protected void setBackgroundColor(int color) {
        if (getTitleBar() != null) {
            getTitleBar().setBackgroundColor(color);
        }
    }

    protected TitleBar getTitleBar() {
        return mTitleBar;
    }

    /**
     * 是否使用EventBus
     */
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (mPresenter != null) mPresenter.onDestroy();
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (useEventBus()) EventBus.getDefault().unregister(this);
        this.mUnbinder = null;
        this.mPresenter = null;
    }
}
