package com.neuroandroid.pyweather.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.github.mmin18.widget.RealtimeBlurView;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.ISelect;
import com.neuroandroid.pyweather.bean.NormalListBean;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.ui.activity.CustomBackgroundActivity;
import com.neuroandroid.pyweather.utils.ColorUtils;
import com.neuroandroid.pyweather.utils.ImageLoader;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.SystemUtils;
import com.neuroandroid.pyweather.utils.TimeUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.dialog.ListDialog;
import com.neuroandroid.pyweather.widget.photo.PhotoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/11.
 */

public class WallPaperFragment extends BaseFragment implements View.OnTouchListener {
    @BindView(R.id.iv_wall_paper)
    PhotoView mIvWallPaper;
    @BindView(R.id.blur_view)
    RealtimeBlurView mBlurView;
    @BindView(R.id.rl_theme_style)
    RelativeLayout mRlThemeStyle;
    @BindView(R.id.sb_transparency)
    SeekBar mSbTransparency;
    @BindView(R.id.sb_blur_level)
    SeekBar mSbBlurLevel;
    @BindView(R.id.ll_bottom_control)
    LinearLayout mLlBottomControl;
    @BindView(R.id.rl_top_bar)
    RelativeLayout mRlTopBar;
    @BindView(R.id.iv_arrow_back)
    ImageView mIvArrowBack;
    @BindView(R.id.iv_done)
    ImageView mIvDone;
    @BindView(R.id.tv_wall_paper)
    NoPaddingTextView mTvWallPaper;
    @BindView(R.id.tv_theme_style_desc)
    NoPaddingTextView mTvThemeStyleDesc;
    @BindView(R.id.tv_theme_style)
    NoPaddingTextView mTvThemeStyle;
    @BindView(R.id.iv_1)
    ImageView mIvThemeStyle;
    @BindView(R.id.tv_transparency)
    NoPaddingTextView mTvTransparency;
    @BindView(R.id.tv_blur_level)
    NoPaddingTextView mTvBlurLevel;
    @BindView(R.id.ll_wall_paper)
    LinearLayout mLlWallPaper;

    /**
     * WallpaperManager.FLAG_SYSTEM : 主屏幕
     * WallpaperManager.FLAG_LOCK : 锁定屏幕
     * WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK : 主屏幕与锁定屏幕
     */
    private int mCurrentWallPaperFlag = WallpaperManager.FLAG_SYSTEM;

    /**
     * 底部控制layout的高度
     */
    private int mBottomControlLayoutHeight;

    /**
     * 顶部bar高度
     */
    private int mTopBarHeight;

    private ValueAnimator mAnimator;
    private int mThemeStyle;
    private int mTransparency;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_wall_paper;
    }

    @Override
    protected void initView() {
        mTopBarHeight = (int) UIUtils.getDimen(R.dimen.y92);
        mBottomControlLayoutHeight = (int) UIUtils.getDimen(R.dimen.y324);
        mRootView.setOnTouchListener(this);
        mSbTransparency.setMax(70);
        mSbBlurLevel.setMax(100);
        setCustomBackground();
        showOrHideLayout(true, false);
    }

    @Override
    protected void initListener() {
        mIvWallPaper.setOnPhotoTapListener((view, x, y) ->
                showOrHideLayout(mLlBottomControl.getLayoutParams().height == 0, false));
        mIvArrowBack.setOnClickListener(v -> {
            if (mActivity instanceof CustomBackgroundActivity) {
                CustomBackgroundActivity customBackgroundActivity = (CustomBackgroundActivity) mActivity;
                customBackgroundActivity.onBackPressed();
            }
        });
        mRlThemeStyle.setOnClickListener(view -> {
            ListDialog listDialog = new ListDialog(mContext);
            listDialog.setNormalListAdapter(generateDataList(mThemeStyle),
                    (v, position, isSelected, normalListBean) -> {
                        if (isSelected) {
                            switch (position) {
                                case 0:  // 亮色
                                    mThemeStyle = Color.WHITE;
                                    break;
                                case 1:  // 暗色
                                    mThemeStyle = Color.BLACK;
                                    break;
                            }
                            setThemeStyle();
                            listDialog.dismiss();
                        }
                    }).setSelectMode(ISelect.SINGLE_MODE);
            listDialog.show();
        });
        mSbTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mTransparency = progress;
                    mBlurView.setOverlayColor(ColorUtils.adjustAlpha(mThemeStyle == Color.WHITE ?
                            Color.BLACK : Color.WHITE, progress * 1.0f / 100));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBlurView.setBlurRadius(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mLlWallPaper.setOnClickListener(v -> {
            showOrHideLayout(false, false);
            ListDialog listDialog = new ListDialog(mContext);
            listDialog.setNormalListAdapter(generateWallPaperDataList(mCurrentWallPaperFlag), (view, position, isSelected, normalListBean) -> {
                if (isSelected) {
                    switch (position) {
                        case 0:
                            mCurrentWallPaperFlag = WallpaperManager.FLAG_SYSTEM;
                            break;
                        case 1:
                            mCurrentWallPaperFlag = WallpaperManager.FLAG_LOCK;
                            break;
                        case 2:
                            mCurrentWallPaperFlag = WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK;
                            break;
                    }
                    listDialog.dismiss();
                    // UIUtils.getHandler().postDelayed(() -> showOrHideLayout(false, true), 250);
                    UIUtils.getHandler().postDelayed(() ->
                            setWallPaper(getScreenShot(mActivity), mActivity), 250);
                }
            }).setSelectMode(ISelect.SINGLE_MODE).setAdapterCheckedPos();
            listDialog.show();
        });
    }

    private List<NormalListBean> generateDataList(int color) {
        List<NormalListBean> dataList = new ArrayList<>();
        NormalListBean normalListBean = new NormalListBean();
        normalListBean.setText("亮色");
        normalListBean.setSelected(color == Color.WHITE);
        dataList.add(normalListBean);
        normalListBean = new NormalListBean();
        normalListBean.setText("暗色");
        normalListBean.setSelected(color == Color.BLACK);
        dataList.add(normalListBean);
        return dataList;
    }

    private List<NormalListBean> generateWallPaperDataList(int wallPaperFlag) {
        List<NormalListBean> dataList = new ArrayList<>();
        NormalListBean normalListBean = new NormalListBean();
        normalListBean.setText("主屏幕");
        normalListBean.setSelected(wallPaperFlag == WallpaperManager.FLAG_SYSTEM);
        dataList.add(normalListBean);
        normalListBean = new NormalListBean();
        normalListBean.setText("锁定屏幕");
        normalListBean.setSelected(wallPaperFlag == WallpaperManager.FLAG_LOCK);
        dataList.add(normalListBean);
        normalListBean = new NormalListBean();
        normalListBean.setText("主屏幕与锁定屏幕");
        normalListBean.setSelected(wallPaperFlag == (WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK));
        dataList.add(normalListBean);
        return dataList;
    }

    /**
     * 设置底部控制layout的高度
     */
    private void setBottomControlLayoutHeight(int height) {
        mLlBottomControl.getLayoutParams().height = height;
        mLlBottomControl.requestLayout();
    }

    /**
     * 设置底部控制layout的高度
     */
    private void setTopBarHeight(int height) {
        mRlTopBar.getLayoutParams().height = height;
        mRlTopBar.requestLayout();
    }

    /**
     * 控制显示或者隐藏
     *
     * @param show true : 显示
     */
    private void showOrHideLayout(boolean show, boolean wallPaper) {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }
        mAnimator = ValueAnimator.ofInt(show ? 0 : mBottomControlLayoutHeight, show ? mBottomControlLayoutHeight : 0);
        mAnimator.addUpdateListener(animation -> {
            int height = (int) animation.getAnimatedValue();
            float percent = height * 1.0f / mBottomControlLayoutHeight;
            setTopBarHeight((int) (percent * mTopBarHeight));
            setBottomControlLayoutHeight(height);
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (wallPaper) {
                    setWallPaper(getScreenShot(mActivity), mActivity);
                }
            }
        });
        mAnimator.setDuration(200);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.start();
    }

    public void setWallPaper(Bitmap bitmap, Activity activity) {
        WallpaperManager manager = WallpaperManager.getInstance(activity);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.setBitmap(bitmap, null, true, mCurrentWallPaperFlag);
                ShowUtils.showToast("设置成功");
            } else {
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("mimeType", "image*//*");
                Uri uri = Uri.parse(MediaStore.Images.Media
                        .insertImage(getActivity().getContentResolver(),
                                bitmap, null, null));
                intent.setData(uri);
                startActivityForResult(intent, 10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCustomBackground() {
        String customBackground = SPUtils.getString(mContext, Constant.SP_CUSTOM_BACKGROUND, null);
        mThemeStyle = Color.WHITE;
        ImageLoader.getInstance().displayImage(mContext, customBackground, getBackground(), mIvWallPaper);
        mBlurView.setBlurRadius(0);
    }

    private void setThemeStyle() {
        int backgroundColor = mThemeStyle == Color.WHITE ? UIUtils.getColor(R.color.white_a) : UIUtils.getColor(R.color.black_a);
        int iconAndTextColor = mThemeStyle == Color.WHITE ? UIUtils.getColor(R.color.colorGray333) : UIUtils.getColor(R.color.white);
        mRlTopBar.setBackgroundColor(backgroundColor);
        mLlBottomControl.setBackgroundColor(backgroundColor);
        mIvArrowBack.setColorFilter(iconAndTextColor);
        mIvDone.setColorFilter(iconAndTextColor);
        mTvWallPaper.setTextColor(iconAndTextColor);
        mTvThemeStyleDesc.setTextColor(iconAndTextColor);
        mTvThemeStyle.setTextColor(iconAndTextColor);
        mIvThemeStyle.setColorFilter(iconAndTextColor);
        mTvTransparency.setTextColor(iconAndTextColor);
        mTvBlurLevel.setTextColor(iconAndTextColor);
        mBlurView.setOverlayColor(ColorUtils.adjustAlpha(mThemeStyle == Color.WHITE ?
                Color.BLACK : Color.WHITE, mTransparency * 1.0f / 100));
    }

    private int getBackground() {
        if (TimeUtils.judgeDayOrNight()) {
            return R.mipmap.img_day;
        } else {
            return R.mipmap.img_night;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public Bitmap getScreenShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取屏幕宽和高
        int widths = SystemUtils.getScreenWidth(activity);
        int heights = SystemUtils.getScreenHeight(activity);

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        // Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights, widths, heights - statusBarHeights);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, widths, heights);

        // 销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bmp;
    }
}
