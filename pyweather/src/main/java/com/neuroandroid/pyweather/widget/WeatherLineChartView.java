package com.neuroandroid.pyweather.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.utils.SPUtils;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherLineChartView extends View {
    private static final int LINE_TYPE_LINE = 0;  // 折线图
    private static final int LINE_TYPE_CURVE = 1;  // 曲线图
    private final Context mContext;
    private List<HeFenWeather.HeWeather5Bean.DailyForecastBean> mDailyForecastDataList;

    // 当数据为空时的7天天气预报假数据
    private int[] mEmptyMaxTemp = {31, 29, 26, 33, 28, 31, 28};
    private int[] mEmptyMinTemp = {24, 23, 23, 25, 24, 21, 22};

    public void setDailyForecastDataList(List<HeFenWeather.HeWeather5Bean.DailyForecastBean> dataList, int themeStyleColor) {
        this.mDailyForecastDataList = dataList;
        mCurrentLineType = SPUtils.getInt(mContext, Constant.SP_LINE_TYPE, LINE_TYPE_LINE);
        mMaxLinePath.reset();
        mMinLinePath.reset();
        mThemeColor = themeStyleColor;
        mCirclePaint.setColor(mThemeColor);
        mMaxTempLinePaint.setColor(mThemeColor);
        mMinTempLinePaint.setColor(mThemeColor);
        mTextPaint.setColor(mThemeColor);
    }

    private int mCurrentLineType = LINE_TYPE_LINE;

    /**
     * 线的宽度
     */
    private float mLineStrokeWidth;

    /**
     * 线和文本的颜色
     */
    private int mThemeColor;

    /**
     * 文本大小
     */
    private float mTextSize;

    /**
     * 最高温度线的Path
     */
    private Path mMaxLinePath;

    /**
     * 最低温度线的Path
     */
    private Path mMinLinePath;

    /**
     * MaxTempLine线的画笔
     */
    private Paint mMaxTempLinePaint;

    /**
     * MinTempLine线的画笔
     */
    private Paint mMinTempLinePaint;

    /**
     * 圆点的画笔
     */
    private Paint mCirclePaint;

    /**
     * 文本的画笔
     */
    private Paint mTextPaint;

    /**
     * 最高温度
     */
    private int mMaxTemp;

    /**
     * 最低温度
     */
    private int mMinTemp;

    private Rect mTextRect;

    /**
     * 折线图/曲线图动画
     */
    private float mMaxTempLineSrcLength;
    private float mMinTempLineSrcLength;
    private PathMeasure mMaxTempLineMeasure;
    private PathMeasure mMinTempLineMeasure;
    private ObjectAnimator mAnimator;

    public WeatherLineChartView(Context context) {
        this(context, null);
    }

    public WeatherLineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mTextRect = new Rect();
        mLineStrokeWidth = UIUtils.getDimen(R.dimen.x2);
        mThemeColor = UIUtils.getColor(R.color.white);
        mTextSize = UIUtils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_SP, 11f);

        // Path
        mMaxLinePath = new Path();
        mMinLinePath = new Path();

        // Paint
        mMaxTempLinePaint = new Paint();
        // mLinePaint.setPathEffect(effects);
        mMaxTempLinePaint.setDither(true);
        mMaxTempLinePaint.setColor(mThemeColor);
        mMaxTempLinePaint.setStrokeWidth(mLineStrokeWidth);
        mMaxTempLinePaint.setStyle(Paint.Style.STROKE);
        mMaxTempLinePaint.setAntiAlias(true);

        mMinTempLinePaint = new Paint(mMaxTempLinePaint);

        mCirclePaint = new Paint(mMaxTempLinePaint);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mThemeColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mCurrentLineType = SPUtils.getInt(mContext, Constant.SP_LINE_TYPE, LINE_TYPE_LINE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDailyForecastDataList != null && mDailyForecastDataList.size() == 7) {
            mTextPaint.setTextSize(mTextSize);
            mMaxTempLinePaint.setStrokeWidth(mLineStrokeWidth);
            mMinTempLinePaint.setStrokeWidth(mLineStrokeWidth);
            UIUtils.getTextBounds(mTextPaint, "30" + Constant.TEMP, mTextRect);
            // 绘制七日天气预报
            getMaxAndMinTemp(mDailyForecastDataList);
            float distanceOfPointAndPoint = getDistanceOfPointAndPoint();
            float startX = distanceOfPointAndPoint * 0.5f;
            for (int i = 0; i < mDailyForecastDataList.size(); i++) {
                HeFenWeather.HeWeather5Bean.DailyForecastBean dailyForecastBean = mDailyForecastDataList.get(i);
                HeFenWeather.HeWeather5Bean.DailyForecastBean.TmpBean tmp = dailyForecastBean.getTmp();
                int maxTemp = Integer.parseInt(tmp.getMax());
                int minTemp = Integer.parseInt(tmp.getMin());

                processWeatherLogic(canvas, i, maxTemp, minTemp, distanceOfPointAndPoint, startX, false);
                startX += distanceOfPointAndPoint;
            }
        } else {
            mTextPaint.setTextSize(mTextSize * 0.5f);
            mMaxTempLinePaint.setStrokeWidth(mLineStrokeWidth * 0.5f);
            mMinTempLinePaint.setStrokeWidth(mLineStrokeWidth * 0.5f);
            UIUtils.getTextBounds(mTextPaint, "30" + Constant.TEMP, mTextRect);
            getEmptyMaxAndMinTemp();
            float distanceOfPointAndPoint = getDistanceOfPointAndPoint();
            float startX = distanceOfPointAndPoint * 0.5f;
            for (int i = 0; i < 7; i++) {
                int maxTemp = mEmptyMaxTemp[i];
                int minTemp = mEmptyMinTemp[i];

                processWeatherLogic(canvas, i, maxTemp, minTemp, distanceOfPointAndPoint, startX, true);
                startX += distanceOfPointAndPoint;
            }
        }
        startAnim();
        canvas.drawPath(mMaxLinePath, mMaxTempLinePaint);
        canvas.drawPath(mMinLinePath, mMinTempLinePaint);
    }

    /**
     * 开启动画
     */
    private void startAnim() {
        if (mAnimator == null) {
            mMaxTempLineMeasure = new PathMeasure(mMaxLinePath, false);
            mMinTempLineMeasure = new PathMeasure(mMinLinePath, false);
            mMaxTempLineSrcLength = mMaxTempLineMeasure.getLength();
            mMinTempLineSrcLength = mMinTempLineMeasure.getLength();
            mAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f);
            mAnimator.setDuration(800);
            mAnimator.start();
        }
    }

    /**
     * 处理天气预报折线图逻辑
     */
    private void processWeatherLogic(Canvas canvas, int index, int maxTemp, int minTemp,
                                     float distanceOfPointAndPoint, float startX, boolean empty) {
        float maxTempYAxis = calTempYAxis(maxTemp);
        float minTempYAxis = calTempYAxis(minTemp);

        if (index == 0) {
            mMaxLinePath.moveTo(startX, maxTempYAxis);
            mMinLinePath.moveTo(startX, minTempYAxis);
        } else {
            if (mCurrentLineType == LINE_TYPE_LINE) {
                mMaxLinePath.lineTo(startX, maxTempYAxis);
                mMinLinePath.lineTo(startX, minTempYAxis);
            } else {
                float previousStartX = startX - distanceOfPointAndPoint;
                float previousMaxTempYAxis;
                float previousMinTempYAxis;
                if (!empty) {
                    HeFenWeather.HeWeather5Bean.DailyForecastBean.TmpBean previousTmp = mDailyForecastDataList.get(index - 1).getTmp();
                    int previousMaxTemp = Integer.parseInt(previousTmp.getMax());
                    int previousMinTemp = Integer.parseInt(previousTmp.getMin());

                    previousMaxTempYAxis = calTempYAxis(previousMaxTemp);
                    previousMinTempYAxis = calTempYAxis(previousMinTemp);
                } else {
                    int previousMaxTemp = mEmptyMaxTemp[index - 1];
                    int previousMinTemp = mEmptyMinTemp[index - 1];

                    previousMaxTempYAxis = calTempYAxis(previousMaxTemp);
                    previousMinTempYAxis = calTempYAxis(previousMinTemp);
                }

                mMaxLinePath.cubicTo((previousStartX + startX) / 2, getMeasuredHeight() - (getMeasuredHeight() - previousMaxTempYAxis),
                        (previousStartX + startX) / 2, maxTempYAxis, startX, maxTempYAxis);
                mMinLinePath.cubicTo((previousStartX + startX) / 2, getMeasuredHeight() - (getMeasuredHeight() - previousMinTempYAxis),
                        (previousStartX + startX) / 2, minTempYAxis, startX, minTempYAxis);
            }
        }
        canvas.drawText(maxTemp + Constant.TEMP, startX, maxTempYAxis - mTextRect.height(), mTextPaint);
        canvas.drawText(minTemp + Constant.TEMP, startX, minTempYAxis + 2 * mTextRect.height(), mTextPaint);

        if (mCurrentLineType == LINE_TYPE_LINE) {
            if (empty) {
                canvas.drawCircle(startX, maxTempYAxis, 2.5f, mCirclePaint);
                canvas.drawCircle(startX, minTempYAxis, 2.5f, mCirclePaint);
            } else {
                canvas.drawCircle(startX, maxTempYAxis, 5f, mCirclePaint);
                canvas.drawCircle(startX, minTempYAxis, 5f, mCirclePaint);
            }
        }
    }

    /**
     * 获取最高温度和最低温度
     */
    private void getMaxAndMinTemp(List<HeFenWeather.HeWeather5Bean.DailyForecastBean> dataList) {
        mMaxTemp = parseInt(dataList.get(0).getTmp().getMax());
        mMinTemp = parseInt(dataList.get(0).getTmp().getMin());
        for (int i = 1; i < mDailyForecastDataList.size(); i++) {
            int maxTemp = Integer.parseInt(dataList.get(i).getTmp().getMax());
            int minTemp = Integer.parseInt(dataList.get(i).getTmp().getMin());
            if (maxTemp > mMaxTemp) {
                mMaxTemp = maxTemp;
            }
            if (minTemp < mMinTemp) {
                mMinTemp = minTemp;
            }
        }
        // += -= 是为了画出更加好看的折线图
        int diffTemp = mMaxTemp - mMinTemp;
        mMaxTemp += diffTemp / 2;
        mMinTemp -= diffTemp / 2;
    }

    /**
     * 当数据为空时获取最高温度和最低温度
     */
    private void getEmptyMaxAndMinTemp() {
        mMaxTemp = mEmptyMaxTemp[0];
        mMinTemp = mEmptyMinTemp[0];
        for (int i = 1; i < 7; i++) {
            int maxTemp = mEmptyMaxTemp[i];
            int minTemp = mEmptyMinTemp[i];
            if (maxTemp > mMaxTemp) {
                mMaxTemp = maxTemp;
            }
            if (minTemp < mMinTemp) {
                mMinTemp = minTemp;
            }
        }
        int diffTemp = mMaxTemp - mMinTemp;
        mMaxTemp += diffTemp / 2;
        mMinTemp -= diffTemp / 2;
    }

    /**
     * 获取点与点之间的距离
     */
    private float getDistanceOfPointAndPoint() {
        return getMeasuredWidth() / 7;
    }

    /**
     * 线的y坐标
     */
    private float calTempYAxis(int temp) {
        int measuredHeight = getMeasuredHeight();
        int diff = mMaxTemp - temp;
        int diffTemp = mMaxTemp - mMinTemp;
        float percent = diff * 1.0f / diffTemp;
        // float yAxis = (measuredHeight - 2 * mTextRect.height()) * percent + mTextRect.height();
        float yAxis = measuredHeight * percent;
        return yAxis;
    }

    public void setPhase(float phase) {
        mMaxTempLinePaint.setPathEffect(createPathEffect(mMaxTempLineSrcLength, phase));
        mMinTempLinePaint.setPathEffect(createPathEffect(mMinTempLineSrcLength, phase));
        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                pathLength - phase * pathLength);
    }
}
