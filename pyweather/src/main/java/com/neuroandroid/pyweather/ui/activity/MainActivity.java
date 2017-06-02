package com.neuroandroid.pyweather.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.ui.fragment.CityManageFragment;
import com.neuroandroid.pyweather.ui.fragment.WeatherFragment;
import com.neuroandroid.pyweather.utils.FragmentUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.UIUtils;

import butterknife.BindView;
import didikee.com.permissionshelper.PermissionsHelper;
import didikee.com.permissionshelper.permission.DangerousPermissions;

public class MainActivity extends BaseActivity {
    private static final int FRAGMENT_WEATHER = 0;
    private static final int FRAGMENT_CITY_MANAGE = 1;

    /**
     * 需要动态申请的权限
     */
    private static final String[] PERMISSIONS = new String[]{
            DangerousPermissions.LOCATION,
            DangerousPermissions.STORAGE
    };

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_layout)
    NavigationView mNavLayout;

    private PermissionsHelper mPermissionsHelper;
    private MainActivityFragmentCallbacks mCurrentFragment;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPermissionsHelper = new PermissionsHelper(this, PERMISSIONS);
        checkPermission();
    }

    @Override
    protected void initListener() {
        mNavLayout.setNavigationItemSelectedListener(item -> {
            mDrawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_city_manage:
                    UIUtils.getHandler().postDelayed(() -> setChooser(FRAGMENT_CITY_MANAGE), 150);
                    break;
            }
            return true;
        });
    }

    /**
     * 检查有没有权限
     * 没有则去动态申请权限
     */
    private void checkPermission() {
        if (mPermissionsHelper.checkAllPermissions(PERMISSIONS)) {
            setChooser(FRAGMENT_WEATHER);
            mPermissionsHelper.onDestroy();
        } else {
            // 申请权限
            mPermissionsHelper.startRequestNeedPermissions();
        }
        mPermissionsHelper.setonAllNeedPermissionsGrantedListener(new PermissionsHelper.onAllNeedPermissionsGrantedListener() {
            @Override
            public void onAllNeedPermissionsGranted() {
                ShowUtils.showToast("权限申请成功");
                setChooser(FRAGMENT_WEATHER);
            }

            @Override
            public void onPermissionsDenied() {
                ShowUtils.showToast("权限申请失败");
                finish();
            }
        });
    }

    /**
     * 跳转到Fragment
     * {@link MainActivity#FRAGMENT_WEATHER}
     * {@link MainActivity#FRAGMENT_CITY_MANAGE}
     */
    private void setChooser(int key) {
        switch (key) {
            case FRAGMENT_WEATHER:
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                setCurrentFragment(WeatherFragment.newInstance());
                break;
            case FRAGMENT_CITY_MANAGE:
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                setCurrentFragment(CityManageFragment.newInstance());
                break;
        }
    }

    /**
     * 设置当前Fragment
     */
    private void setCurrentFragment(@SuppressWarnings("NullableProblems") Fragment fragment) {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.fl_drawer_container, false);
        // getSupportFragmentManager().beginTransaction().replace(R.id.fl_drawer_container, fragment, null).commit();
        mCurrentFragment = (MainActivityFragmentCallbacks) fragment;
    }

    /**
     * 打开侧滑菜单
     */
    public void openDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * 处理fragment返回事件
     */
    public interface MainActivityFragmentCallbacks {
        boolean handleBackPress();
    }

    @Override
    public void onBackPressed() {
        if (!handleBackPress())
            super.onBackPressed();
    }

    public boolean handleBackPress() {
        if (mCurrentFragment instanceof CityManageFragment) {
            setChooser(FRAGMENT_WEATHER);
            return true;
        }
        if (mDrawerLayout.isDrawerOpen(mNavLayout)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        return mCurrentFragment != null && mCurrentFragment.handleBackPress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionsHelper.onActivityResult(requestCode, resultCode, data);
    }
}
