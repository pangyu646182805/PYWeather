package com.neuroandroid.pyweather.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.model.response.User;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.utils.StateUtils;
import com.neuroandroid.pyweather.utils.SystemUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.LoadingLayout;
import com.neuroandroid.pyweather.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by NeuroAndroid on 2017/3/8.
 */
public abstract class BaseFragment<T extends IBasePresenter> extends Fragment implements IBaseView<T> {
    @Nullable
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;

    @Nullable
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;

    protected T mPresenter;
    protected Activity mActivity;
    protected Context mContext;
    protected View mRootView;
    private Unbinder mUnbinder;
    protected Intent mIntent;
    protected Gson mGson;
    private boolean mIsMulti = false;
    protected User mUser;

    /**
     * 能不能加载更多
     * 默认可以
     */
    protected boolean mLoadMoreEnable = true;

    /**
     * 获得全局的，防止使用getActivity()为空
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mIntent = new Intent();
        mGson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), null);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initPresenter();
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus())
            EventBus.getDefault().register(this);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            // mIsMulti = true;
        }
        initData();
        initListener();
    }

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            initData();
            initListener();
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }*/

    @Override
    public void showLoading() {
        showPageLoading();
    }

    protected void showPageLoading() {
        showPageLoading(Color.TRANSPARENT);
    }

    protected void showPageLoading(int color) {
        if (mLoadingLayout != null) {
            mLoadingLayout.setStatus(LoadingLayout.STATUS_LOADING);
            mLoadingLayout.setBackgroundColor(color);
        }
    }

    protected void showLoadingDialog() {
        BaseActivity baseActivity = (BaseActivity) mActivity;
        baseActivity.showLoadingDialog();
    }

    @Override
    public void showTip(String tip) {
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
        BaseActivity baseActivity = (BaseActivity) mActivity;
        baseActivity.dismissLoadingDialog();
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

    protected void initTitleBar(CharSequence title) {
        initTitleBar(title, true);
    }

    protected void initTitleBar(CharSequence title, boolean immersive) {
        if (mTitleBar != null) {
            if (immersive) {
                if (mActivity instanceof BaseActivity) {
                    BaseActivity baseActivity = (BaseActivity) mActivity;
                    mTitleBar.setImmersive(baseActivity.mImmersive);
                }
            }
            mTitleBar.setTextColor(Color.WHITE);
            mTitleBar.setTitle(title);
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

    protected void setBackgroundColor(int color) {
        if (getTitleBar() != null) {
            getTitleBar().setBackgroundColor(color);
        }
    }

    protected TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }

    /**
     * 是否使用EventBus
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 绑定布局文件
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void initView();

    protected void initPresenter() {
    }

    protected void initData() {
        mUser = StateUtils.getUser();
    }

    protected void initListener() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
    }

    protected MainActivity getMainActivity() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            return (MainActivity) mActivity;
        }
        return null;
    }

    /**
     * 设置状态栏的颜色
     *
     * @param statusBar 状态栏的颜色
     */
    protected void setStatusBar(View statusBar) {
        if (getMainActivity() != null) {
            if (getMainActivity().mImmersive) {
                statusBar.getLayoutParams().height = SystemUtils.getStatusHeight(mActivity);
                statusBar.setBackgroundColor(UIUtils.getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (useEventBus()) EventBus.getDefault().unregister(this);
        if (mPresenter != null) mPresenter.onDestroy();
        this.mPresenter = null;
        this.mUnbinder = null;
        this.mActivity = null;
        this.mContext = null;
        this.mRootView = null;
    }
}
