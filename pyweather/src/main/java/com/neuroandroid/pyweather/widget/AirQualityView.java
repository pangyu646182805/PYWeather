package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by NeuroAndroid on 2017/6/6.
 * 显示空气质量的View
 * 0-50    : 空气质量级别为一级，空气质量状况属于优。此时，空气质量令人满意，基本无空气污染，各类人群可正常活动
 * 51-100  : 空气质量级别为二级，空气质量状况属于良。此时空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响，建议极少数异常敏感人群应减少户外活动
 * 101-150 : 空气质量级别为三级，空气质量状况属于轻度污染。此时，易感人群症状有轻度加剧，健康人群出现刺激症状。建议儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼
 * 151-200 : 空气质量级别为四级，空气质量状况属于中度污染。此时，进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响，建议疾病患者避免长时间、高强度的户外锻练，一般人群适量减少户外运动
 * 201-300 : 空气质量级别为五级，空气质量状况属于重度污染。此时，心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状，建议儿童、老年人和心脏病、肺病患者应停留在室内，停止户外运动，一般人群减少户外运动
 * 大于300 : 空气质量级别为六级，空气质量状况属于严重污染。此时，健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病，建议儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动
 */
public class AirQualityView extends View {
    /**
     * 六个级别相对应的颜色
     */
    private static final int[] AIR_QUALITY_LEVEL = {R.color.air_quality_level_1, R.color.air_quality_level_2, R.color.air_quality_level_3,
            R.color.air_quality_level_4, R.color.air_quality_level_5, R.color.air_quality_level_6};

    /**
     * 空气污染指数取值范围0-500
     */
    private static final int AIR_QUALITY_INDEX = 500;

    private Context mContext;

    /**
     * 空气污染指数实体类
     */
    private HeFenWeather.HeWeather5Bean.AqiBean mAqiBean;

    public void setAqiBean(HeFenWeather.HeWeather5Bean.AqiBean aqiBean) {
        mAqiBean = aqiBean;
        mAirQuality = Integer.parseInt(aqiBean.getCity().getAqi());
        invalidate();
    }

    /**
     * 空气污染指数
     */
    private int mAirQuality;

    /**
     * 空气污染指数对应的颜色和等级
     */
    private int mAirQualityLevelAndColor;

    /**
     * 圆环的宽度
     */
    private float mRingWidth;

    /**
     * 圆环的颜色
     */
    private int mRingColor;

    /**
     * 空气质量参数文本大小 (45) -> 优
     */
    private float mAirQualityIndexTextSize;

    /**
     * 文本颜色
     */
    private int mAirQualityTextColor;

    /**
     * 空气质量描述文本大小(优)
     */
    private float mAirQualityDescTextSize;

    private String mAirQualityDesc;

    private Paint mRingPaint;
    private Paint mTextPaint;
    private Paint mAirQualityPaint;
    private Path mRingPath;
    private Path mAirQualityPath;
    private RectF mRingRect = new RectF();
    private Rect mTextRect = new Rect();

    public AirQualityView(Context context) {
        this(context, null);
    }

    public AirQualityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirQualityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mRingWidth = UIUtils.getDimen(R.dimen.x24);
        mRingColor = UIUtils.getColor(R.color.white_3);
        mAirQualityIndexTextSize = UIUtils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_SP, 30);
        mAirQualityDescTextSize = UIUtils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_SP, 14);
        mAirQualityTextColor = Color.WHITE;

        mRingPaint = new Paint();
        mRingPaint.setDither(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);
        mRingPaint.setAntiAlias(true);

        mAirQualityPaint = new Paint(mRingPaint);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mAirQualityTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mRingPath = new Path();
        mAirQualityPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rectParam = mRingWidth * 0.8f;
        mRingRect.set(rectParam, rectParam, getMeasuredWidth() - rectParam, getMeasuredHeight() - rectParam);
        mRingPath.addArc(mRingRect, 135, 270);
        canvas.drawPath(mRingPath, mRingPaint);

        if (mAqiBean != null) {
            judgeAirQuality();
            mAirQualityPaint.setColor(UIUtils.getColor(AIR_QUALITY_LEVEL[mAirQualityLevelAndColor]));
            float airQualitySweepAngle = 270 * (mAirQuality * 1.0f / AIR_QUALITY_INDEX);
            mAirQualityPath.addArc(mRingRect, 135, airQualitySweepAngle);
            canvas.drawPath(mAirQualityPath, mAirQualityPaint);

            float center = getMeasuredWidth() * 0.5f;
            mTextPaint.setTextSize(mAirQualityIndexTextSize);
            UIUtils.getTextBounds(mTextPaint, String.valueOf(mAirQuality), mTextRect);
            float y = center + 0.1f * mTextRect.height();
            canvas.drawText(String.valueOf(mAirQuality), center, y, mTextPaint);

            mTextPaint.setTextSize(mAirQualityDescTextSize);
            UIUtils.getTextBounds(mTextPaint, mAirQualityDesc, mTextRect);
            y = y + 2 * mTextRect.height();
            canvas.drawText(mAirQualityDesc, center, y, mTextPaint);
        }
    }

    /**
     * 判断空气质量指数
     */
    private void judgeAirQuality() {
        if (mAirQuality >= 0 && mAirQuality <= 50) {
            mAirQualityLevelAndColor = 0;
            mAirQualityDesc = "优";
        } else if (mAirQuality > 50 && mAirQuality <= 100) {
            mAirQualityLevelAndColor = 1;
            mAirQualityDesc = "良";
        } else if (mAirQuality > 100 && mAirQuality <= 150) {
            mAirQualityLevelAndColor = 2;
            mAirQualityDesc = "轻度污染";
        } else if (mAirQuality > 150 && mAirQuality <= 200) {
            mAirQualityLevelAndColor = 3;
            mAirQualityDesc = "中度污染";
        } else if (mAirQuality > 200 && mAirQuality <= 300) {
            mAirQualityLevelAndColor = 4;
            mAirQualityDesc = "重度污染";
        } else {
            mAirQualityLevelAndColor = 5;
            mAirQualityDesc = "严重污染";
        }
    }
}
