package com.neuroandroid.pyweather.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.utils.ImageLoader;
import com.neuroandroid.pyweather.widget.banner.holder.Holder;

/**
 * Created by NeuroAndroid on 2017/3/13.
 */

public class HeaderHolderView implements Holder<Integer> {
    private ImageView mIv;

    @Override
    public View createView(Context context) {
        mIv = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mIv.setLayoutParams(params);
        mIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return mIv;
    }

    @Override
    public void updateUI(Context context, int position, Integer data) {
        ImageLoader.getInstance().displayImage(context, data, mIv);
    }
}
