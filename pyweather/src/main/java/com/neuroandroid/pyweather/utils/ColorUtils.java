package com.neuroandroid.pyweather.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.content.ContextCompat;

import com.neuroandroid.pyweather.R;

/**
 * Created by NeuroAndroid on 2017/5/9.
 */

public class ColorUtils {
    public static int stripAlpha(@ColorInt int color) {
        return -16777216 | color;
    }

    @ColorInt
    public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0D, to = 2.0D) float by) {
        if (by == 1.0F) {
            return color;
        } else {
            int alpha = Color.alpha(color);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= by;
            return (alpha << 24) + (16777215 & Color.HSVToColor(hsv));
        }
    }

    @ColorInt
    public static int darkenColor(@ColorInt int color) {
        return shiftColor(color, 0.9F);
    }

    @ColorInt
    public static int lightenColor(@ColorInt int color) {
        return shiftColor(color, 1.1F);
    }

    public static boolean isColorLight(@ColorInt int color) {
        double darkness = 1.0D - (0.299D * (double) Color.red(color) + 0.587D * (double) Color.green(color) + 0.114D * (double) Color.blue(color)) / 255.0D;
        return darkness < 0.4D;
    }

    @ColorInt
    public static int invertColor(@ColorInt int color) {
        int r = 255 - Color.red(color);
        int g = 255 - Color.green(color);
        int b = 255 - Color.blue(color);
        return Color.argb(Color.alpha(color), r, g, b);
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, @FloatRange(from = 0.0D, to = 1.0D) float factor) {
        int alpha = Math.round((float) Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @ColorInt
    public static int withAlpha(@ColorInt int baseColor, @FloatRange(from = 0.0D, to = 1.0D) float alpha) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255.0F))) << 24;
        int rgb = 16777215 & baseColor;
        return a + rgb;
    }

    public static int blendColors(int color1, int color2, @FloatRange(from = 0.0D, to = 1.0D) float ratio) {
        float inverseRatio = 1.0F - ratio;
        float a = (float) Color.alpha(color1) * inverseRatio + (float) Color.alpha(color2) * ratio;
        float r = (float) Color.red(color1) * inverseRatio + (float) Color.red(color2) * ratio;
        float g = (float) Color.green(color1) * inverseRatio + (float) Color.green(color2) * ratio;
        float b = (float) Color.blue(color1) * inverseRatio + (float) Color.blue(color2) * ratio;
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    private ColorUtils() {
    }

    public static int getPrimaryTextColor(Context context, boolean dark) {
        return dark ? ContextCompat.getColor(context, R.color.colorGray333) : ContextCompat.getColor(context, R.color.white);
    }

    public static int getSecondaryTextColor(Context context, boolean dark) {
        return dark ? ContextCompat.getColor(context, R.color.colorGray666) : ContextCompat.getColor(context, R.color.white_a);
    }
}
