package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.config.Constant;
import com.neuroandroid.pyweather.model.response.HeFenWeather;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by NeuroAndroid on 2017/6/5.
 */

public class WeatherLineChartView extends View {
    private final Context mContext;
    private List<HeFenWeather.HeWeather5Bean.DailyForecastBean> mDailyForecastDataList;

    public void setDailyForecastDataList(List<HeFenWeather.HeWeather5Bean.DailyForecastBean> dataList) {
        this.mDailyForecastDataList = dataList;
        invalidate();
    }

    /**
     * 线的宽度
     */
    private float mLineStrokeWidth;

    /**
     * 线和文本的颜色
     */
    private int mLineColor;

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
     * 线的画笔
     */
    private Paint mLinePaint;

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
        mLineColor = UIUtils.getColor(R.color.white);
        mTextSize = UIUtils.getRawSize(mContext, TypedValue.COMPLEX_UNIT_SP, 11f);

        // Path
        mMaxLinePath = new Path();
        mMinLinePath = new Path();

        // Paint
        mLinePaint = new Paint();
        // mLinePaint.setPathEffect(effects);
        mLinePaint.setDither(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineStrokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mCirclePaint = new Paint(mLinePaint);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mLineColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        UIUtils.getTextBounds(mTextPaint, "30" + Constant.TEMP, mTextRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDailyForecastDataList != null && mDailyForecastDataList.size() == 7) {
            // 绘制七日天气预报
            getMaxAndMinTemp(mDailyForecastDataList);
            float distanceOfPointAndPoint = getDistanceOfPointAndPoint();
            float startX = distanceOfPointAndPoint * 0.5f;
            for (int i = 0; i < mDailyForecastDataList.size(); i++) {
                HeFenWeather.HeWeather5Bean.DailyForecastBean dailyForecastBean = mDailyForecastDataList.get(i);
                HeFenWeather.HeWeather5Bean.DailyForecastBean.TmpBean tmp = dailyForecastBean.getTmp();
                int maxTemp = Integer.parseInt(tmp.getMax());
                int minTemp = Integer.parseInt(tmp.getMin());

                float maxTempYAxis = calTempYAxis(maxTemp);
                float minTempYAxis = calTempYAxis(minTemp);

                if (i == 0) {
                    mMaxLinePath.moveTo(startX, maxTempYAxis);
                    mMinLinePath.moveTo(startX, minTempYAxis);
                } else {
                    mMaxLinePath.lineTo(startX, maxTempYAxis);
                    mMinLinePath.lineTo(startX, minTempYAxis);
                }
                canvas.drawText(maxTemp + Constant.TEMP, startX, maxTempYAxis - mTextRect.height(), mTextPaint);
                canvas.drawText(minTemp + Constant.TEMP, startX, minTempYAxis + 2 * mTextRect.height(), mTextPaint);
                canvas.drawCircle(startX, maxTempYAxis, 5, mCirclePaint);
                canvas.drawCircle(startX, minTempYAxis, 5, mCirclePaint);
                startX += distanceOfPointAndPoint;
            }
            canvas.drawPath(mMaxLinePath, mLinePaint);
            canvas.drawPath(mMinLinePath, mLinePaint);
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
        mMaxTemp += 5;
        mMinTemp -= 5;
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
}
