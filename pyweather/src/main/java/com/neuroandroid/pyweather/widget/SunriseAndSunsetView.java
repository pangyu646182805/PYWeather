package com.neuroandroid.pyweather.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.utils.L;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.util.Calendar;

/**
 * Created by NeuroAndroid on 2017/6/6.
 * 天气预报日出日落自定义View
 */
public class SunriseAndSunsetView extends View {
    private static final String STR_SUNRISE = "日出 ";
    private static final String STR_SUNSET = "日落 ";
    private Context mContext;
    /**
     * 日出日落实体类
     */
    private HeFenWeather.HeWeather5Bean.DailyForecastBean.AstroBean mAstroBean;
    private float mPercent;

    public void setAstroBean(HeFenWeather.HeWeather5Bean.DailyForecastBean.AstroBean astroBean, int themeStyleColor) {
        mAstroBean = astroBean;
        mSunriseStr = astroBean.getSr();
        mSunsetStr = astroBean.getSs();

        mThemeColor = themeStyleColor;
        mThemeSubColor = themeStyleColor == Color.WHITE ? Constant.LIGHT_THEME_STYLE_SUB_COLOR : Constant.DARK_THEME_STYLE_SUB_COLOR;
        mLinePaint.setColor(mThemeColor);
        mCirclePaint.setColor(mThemeColor);
        mTextPaint.setColor(mThemeColor);
        mSunriseAndSunsetPaint.setColor(mThemeSubColor);

        mSunriseMillis = timeStrToTimeMill(mSunriseStr);
        mSunsetMillis = timeStrToTimeMill(mSunsetStr);
        // 方便测试
        // long currentMillis = getCurrentMillis(5, 39);
        mPercent = (System.currentTimeMillis() - mSunriseMillis) * 1.0f / (mSunsetMillis - mSunriseMillis);
        if (mPercent < 0f) mPercent = 0f;
        if (mPercent > 1f) mPercent = 1f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPercent);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            mPercent = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 日出的字符串
     */
    private String mSunriseStr = "04:57";

    /**
     * 日落的字符串
     */
    private String mSunsetStr = "18:58";

    /**
     * 日出的时间戳
     */
    private long mSunriseMillis;

    /**
     * 日落的时间戳
     */
    private long mSunsetMillis;

    /**
     * 日出日落文本的字体大小
     */
    private float mSunriseAndSunsetTextSize;

    /**
     * 左右边距
     */
    private float mLeftAndRightPadding;

    private float mCircleRadius;

    /**
     * 主题色
     */
    private int mThemeColor = Constant.LIGHT_THEME_STYLE_COLOR;

    private int mThemeSubColor = Constant.LIGHT_THEME_STYLE_SUB_COLOR;

    /**
     * 虚线的宽度
     */
    private float mDashedLineWidth;

    /**
     * 日出日落所在的矩形区域
     */
    private RectF mSunriseAndSunsetRect;

    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;

    /**
     * 绘制线的画笔
     */
    private Paint mLinePaint;

    private Paint mCirclePaint;

    private Path mSunriseAndSunsetPath;
    private Paint mSunriseAndSunsetPaint;

    private Rect mTextRect = new Rect();

    /**
     * Path效果(虚线)
     */
    private PathEffect mEffect;

    public SunriseAndSunsetView(Context context) {
        this(context, null);
    }

    public SunriseAndSunsetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunriseAndSunsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mSunriseAndSunsetTextSize = UIUtils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_SP, 12);
        mDashedLineWidth = UIUtils.getDimen(R.dimen.x2);
        mLeftAndRightPadding = UIUtils.getDimen(R.dimen.x32);
        mCircleRadius = UIUtils.getDimen(R.dimen.x16);

        float[] arrayOfFloat = new float[4];
        arrayOfFloat[0] = mDashedLineWidth * 4;
        arrayOfFloat[1] = mDashedLineWidth * 4;
        arrayOfFloat[2] = mDashedLineWidth * 4;
        arrayOfFloat[3] = mDashedLineWidth * 4;
        mEffect = new DashPathEffect(arrayOfFloat, mDashedLineWidth * 2);

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setColor(mThemeColor);
        mLinePaint.setStrokeWidth(mDashedLineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(mEffect);
        mLinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mThemeColor);
        mTextPaint.setTextSize(mSunriseAndSunsetTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint(mLinePaint);
        mCirclePaint.setStrokeWidth(mDashedLineWidth * 0.5f);
        mCirclePaint.setPathEffect(null);

        mSunriseAndSunsetPaint = new Paint(mLinePaint);
        mSunriseAndSunsetPaint.setColor(mThemeSubColor);
        mSunriseAndSunsetPaint.setStyle(Paint.Style.FILL);

        mSunriseAndSunsetRect = new RectF();
        mSunriseAndSunsetPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSunriseAndSunsetRect.set(mLeftAndRightPadding, mLeftAndRightPadding,
                getMeasuredWidth() - mLeftAndRightPadding, getMeasuredHeight() * 2 - mLeftAndRightPadding);
        // 绘制圆弧(半圆)
        canvas.drawArc(mSunriseAndSunsetRect, 180, 180, false, mLinePaint);

        if (mAstroBean != null) {
            float sunriseAndSunsetWidth = getMeasuredWidth() - 2 * mLeftAndRightPadding;
            // 太阳圆圈圆心的x坐标
            float sunCircleX = sunriseAndSunsetWidth * mPercent + mLeftAndRightPadding;

            // 太阳圆圈圆心的x坐标减去mLeftAndRightPadding与sunriseAndSunsetWidth的一半的差值
            float diffSunCircleXAndSunriseAndSunsetWidth = (sunCircleX - mLeftAndRightPadding) - 0.5f * sunriseAndSunsetWidth;
            L.e("diffSunCircleXAndSunriseAndSunsetWidth : " + diffSunCircleXAndSunriseAndSunsetWidth);
            float sunCircleY;
            // 圆心角
            float circleAngle;
            if (diffSunCircleXAndSunriseAndSunsetWidth != 0) {
                // 先计算计算圆心角
                if (diffSunCircleXAndSunriseAndSunsetWidth > 0) {
                    // 太阳圆圈在右边
                    circleAngle = (float) Math.acos(diffSunCircleXAndSunriseAndSunsetWidth / (0.5f * sunriseAndSunsetWidth));
                    circleAngle = 180 - (float) (180 / Math.PI * circleAngle);
                } else {
                    // 太阳圆圈在左边
                    circleAngle = (float) Math.acos(-diffSunCircleXAndSunriseAndSunsetWidth / (0.5f * sunriseAndSunsetWidth));
                    circleAngle = (float) (180 / Math.PI * circleAngle);
                }
                // 太阳圆圈在左边或者右边
                diffSunCircleXAndSunriseAndSunsetWidth = Math.abs(diffSunCircleXAndSunriseAndSunsetWidth);
                sunCircleY = (float) (Math.sqrt(Math.pow(0.5f * sunriseAndSunsetWidth, 2) - Math.pow(diffSunCircleXAndSunriseAndSunsetWidth, 2)));
                sunCircleY = getMeasuredHeight() - sunCircleY;
            } else {
                // 太阳圆圈在中间
                sunCircleY = mLeftAndRightPadding;
                circleAngle = 90;
            }
            L.e("sunCircleY : " + sunCircleY);
            L.e("circleAngle : " + circleAngle);

            mSunriseAndSunsetPath.addArc(mSunriseAndSunsetRect, 180, circleAngle);
            mSunriseAndSunsetPath.lineTo(sunCircleX, getMeasuredHeight());
            // 绘制覆盖区域
            canvas.drawPath(mSunriseAndSunsetPath, mSunriseAndSunsetPaint);

            // 绘制小太阳圆圈
            canvas.drawCircle(sunCircleX, sunCircleY, mCircleRadius, mCirclePaint);

            // 绘制日出字符串
            UIUtils.getTextBounds(mTextPaint, STR_SUNRISE + mSunriseStr, mTextRect);
            canvas.drawText(STR_SUNRISE + mSunriseStr, 2 * mLeftAndRightPadding + 0.5f * mTextRect.width(),
                    getMeasuredHeight() - 0.2f * mLeftAndRightPadding, mTextPaint);

            // 绘制日落字符串
            UIUtils.getTextBounds(mTextPaint, STR_SUNSET + mSunsetStr, mTextRect);
            canvas.drawText(STR_SUNSET + mSunsetStr, getMeasuredWidth() - 2 * mLeftAndRightPadding - 0.5f * mTextRect.width(),
                    getMeasuredHeight() - 0.2f * mLeftAndRightPadding, mTextPaint);
        }
    }

    /**
     * @param timeStr 18:58
     * @return 返回18:58的时间戳
     */
    private long timeStrToTimeMill(String timeStr) {
        int hour = getHourOrMinute(timeStr, 0);
        int minute = getHourOrMinute(timeStr, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    /**
     * @param timeStr  18:58
     * @param position 0, 1
     * @return is position == 0 18 else 58
     */
    private int getHourOrMinute(String timeStr, int position) {
        return Integer.parseInt(timeStr.split(":")[position]);
    }

    /**
     * 设置当前时间
     * 方便调试
     */
    private long getCurrentMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }
}
