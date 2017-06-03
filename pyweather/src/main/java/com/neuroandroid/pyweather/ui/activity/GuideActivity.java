package com.neuroandroid.pyweather.ui.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itsronald.widget.ViewPagerIndicator;
import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.base.BaseActivity;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.ColorShades;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/3.
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.vp_guide)
    ViewPager mVpGuide;
    @BindView(R.id.view_pager_indicator)
    ViewPagerIndicator mPagerIndicator;
    @BindView(R.id.btn_into)
    NoPaddingTextView mBtnInto;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        mVpGuide.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));
        mVpGuide.setPageTransformer(true, new CustomPageTransformer());
        mBtnInto.setVisibility(View.GONE);
        mBtnInto.setAlpha(0f);
    }

    @Override
    protected void initListener() {
        mVpGuide.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View landingBGView = findViewById(R.id.rl_root);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);

                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());

                if (position == 2) {
                    if (positionOffset == 0) {
                        mBtnInto.setVisibility(View.GONE);
                    } else {
                        mBtnInto.setVisibility(View.VISIBLE);
                    }
                    mBtnInto.setAlpha(positionOffset);
                }
            }
        });
        findViewById(R.id.btn_into).setOnClickListener(v -> {
            SPUtils.putBoolean(this, Constant.APP_GUIDE, true);
            finish();
        });
    }

    class ViewPagerAdapter extends PagerAdapter {
        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {
            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];

            View itemView = getLayoutInflater().inflate(R.layout.page_guide, container, false);

            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);

            if (position == 0) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iconView.getLayoutParams();
                params.width = (int) UIUtils.getDimen(R.dimen.x216);
                params.height = (int) UIUtils.getDimen(R.dimen.y128);
            } else if (position == 1 || position == 2) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iconView.getLayoutParams();
                params.width = (int) UIUtils.getDimen(R.dimen.y128);
                params.height = (int) UIUtils.getDimen(R.dimen.y128);
            } else {
                iconView.setVisibility(View.GONE);
            }

            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    class CustomPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                view.setTranslationX(pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    contentView.setTranslationX(pageWidth * position);
                    txt_title.setTranslationX(pageWidth * position);

                    contentView.setAlpha(1 + position);
                    txt_title.setAlpha(1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    imageView.setAlpha(1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                view.setTranslationX(pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    contentView.setTranslationX(pageWidth * position);
                    txt_title.setTranslationX(pageWidth * position);

                    contentView.setAlpha(1 - position);
                    txt_title.setAlpha(1 - position);
                }
                if (imageView != null) {
                    // Fade the image out
                    imageView.setAlpha(1 - position);
                }
            }
        }
    }
}
