package com.neuroandroid.pyweather.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.mmin18.widget.RealtimeBlurView;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.event.BaseEvent;
import com.neuroandroid.pyweather.ui.fragment.CityManageFragment;
import com.neuroandroid.pyweather.ui.fragment.WeatherFragment;
import com.neuroandroid.pyweather.utils.ColorUtils;
import com.neuroandroid.pyweather.utils.FragmentUtils;
import com.neuroandroid.pyweather.utils.ImageLoader;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.SystemUtils;
import com.neuroandroid.pyweather.utils.TimeUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.weather.DynamicWeatherView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import didikee.com.permissionshelper.PermissionsHelper;
import didikee.com.permissionshelper.permission.DangerousPermissions;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_INTO_GUIDE = 2;
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
    @BindView(R.id.fl_background)
    FrameLayout mFlBackground;
    @BindView(R.id.iv_background)
    ImageView mIvBackground;
    @BindView(R.id.blur_view)
    RealtimeBlurView mBlurView;
    private DynamicWeatherView mDynamicWeatherView;

    private PermissionsHelper mPermissionsHelper;
    private MainActivityFragmentCallbacks mCurrentFragment;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        /*mDynamicWeatherView = new DynamicWeatherView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mDynamicWeatherView.setLayoutParams(params);
        mFlBackground.addView(mDynamicWeatherView);
        mDynamicWeatherView.setDrawerType(BaseDrawer.Type.CLOUDY_D);*/
        setCustomBackground();
    }

    @Override
    protected void initData() {
        mPermissionsHelper = new PermissionsHelper(this, PERMISSIONS);
        boolean appGuide = SPUtils.getBoolean(this, Constant.APP_GUIDE, false);
        if (!appGuide) {
            // 如果没有显示过引导页面则显示
            mIntent.setClass(this, GuideActivity.class);
            startActivityForResult(mIntent, REQUEST_CODE_INTO_GUIDE);
        } else {
            checkPermission();
        }
    }

    @Override
    protected void initListener() {
        mNavLayout.setNavigationItemSelectedListener(item -> {
            mDrawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.nav_city_manage:
                    UIUtils.getHandler().postDelayed(() -> setChooser(FRAGMENT_CITY_MANAGE), 250);
                    break;
                case R.id.nav_settings:
                    UIUtils.getHandler().postDelayed(() -> {
                        mIntent.setClass(this, SettingActivity.class);
                        UIUtils.toLayout(mIntent);
                    }, 250);
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
     * 跳转到城市管理页面
     */
    public void toCityManagerFragment() {
        setChooser(FRAGMENT_CITY_MANAGE);
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

    private void setCustomBackground() {
        String customBackground = SPUtils.getString(this, Constant.SP_CUSTOM_BACKGROUND, null);
        int transparency = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, 0);
        int blurLevel = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_BLUR_LEVEL, 0);
        int themeStyle = SPUtils.getInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
        ImageLoader.getInstance().displayImage(this, customBackground, getBackground(), mIvBackground);
        mBlurView.setOverlayColor(ColorUtils.adjustAlpha(themeStyle == Color.WHITE ?
                Color.BLACK : Color.WHITE, transparency * 1f / 100));
        mBlurView.setBlurRadius(blurLevel);
        setSystemStatusBarStyle(themeStyle == Color.WHITE);
    }

    /**
     * 设置图标字体的主题风格
     */
    private void setThemeStyle() {
        int themeStyle = SPUtils.getInt(this, Constant.SP_APP_FONT_ICON_THEME_STYLE, Color.WHITE);
        int transparency = SPUtils.getInt(this, Constant.SP_CUSTOM_BACKGROUND_TRANSPARENCY, 0);
        if (mCurrentFragment instanceof WeatherFragment) {
            WeatherFragment weatherFragment = (WeatherFragment) mCurrentFragment;
            boolean lightThemeStyle = themeStyle == Color.WHITE;
            weatherFragment.setThemeStyle(lightThemeStyle);
            weatherFragment.onThemeStyleChange(lightThemeStyle);
            mBlurView.setOverlayColor(ColorUtils.adjustAlpha(themeStyle == Color.WHITE ?
                    Color.BLACK : Color.WHITE, transparency * 1f / 100));
            setSystemStatusBarStyle(lightThemeStyle);
        }
    }

    /**
     * 设置系统状态栏样式
     */
    private void setSystemStatusBarStyle(boolean lightThemeStyle) {
        if (lightThemeStyle) {
            SystemUtils.setTranslateStatusBar(this);
        } else {
            SystemUtils.myStatusBar(this);
        }
    }

    private int getBackground() {
        if (TimeUtils.judgeDayOrNight()) {
            return R.mipmap.img_day;
        } else {
            return R.mipmap.img_night;
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mDynamicWeatherView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDynamicWeatherView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDynamicWeatherView.onDestroy();
    }*/

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent != null) {
            switch (baseEvent.getEventFlag()) {
                case BaseEvent.EVENT_CUSTOM_BACKGROUND:
                    setCustomBackground();
                    break;
                case BaseEvent.EVENT_LINE_TYPE:
                    if (mCurrentFragment instanceof WeatherFragment) {
                        WeatherFragment weatherFragment = (WeatherFragment) mCurrentFragment;
                        weatherFragment.onLineTypeChange();
                    }
                    break;
                case BaseEvent.EVENT_THEME_STYLE:
                    setThemeStyle();
                    break;
            }
        }
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
        if (requestCode == REQUEST_CODE_INTO_GUIDE) {
            checkPermission();
        }
    }
}
