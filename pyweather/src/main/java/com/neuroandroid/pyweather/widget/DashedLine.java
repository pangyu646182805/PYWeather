package com.neuroandroid.pyweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by py on 2017/3/15.
 * 虚线自定义View
 * 分为水平虚线和竖直虚线
 */
public class DashedLine extends View {
    private Paint paint = null;
    private Path path = null;
    private PathEffect pe = null;
    private int mLineColor;
    private int mDirection;

    public DashedLine(Context paramContext) {
        this(paramContext, null);
    }

    public DashedLine(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DashedLine);
        mLineColor = a.getColor(R.styleable.DashedLine_line_color, 0XFF000000);
        mDirection = a.getInt(R.styleable.DashedLine_line_direction, LinearLayout.HORIZONTAL);
        a.recycle();
        this.paint = new Paint();
        this.path = new Path();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(mLineColor);
        this.paint.setAntiAlias(true);
        float[] arrayOfFloat = new float[4];
        arrayOfFloat[0] = UIUtils.getDimen(R.dimen.x4);
        arrayOfFloat[1] = UIUtils.getDimen(R.dimen.x4);
        arrayOfFloat[2] = UIUtils.getDimen(R.dimen.x4);
        arrayOfFloat[3] = UIUtils.getDimen(R.dimen.x4);
        this.pe = new DashPathEffect(arrayOfFloat, UIUtils.getDimen(R.dimen.x2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mDirection) {
            case LinearLayout.HORIZONTAL:
                this.paint.setStrokeWidth(getMeasuredHeight());
                this.path.moveTo(0.0F, getMeasuredHeight() * 0.5f);
                this.path.lineTo(getMeasuredWidth(), getMeasuredHeight() * 0.5f);
                break;
            case LinearLayout.VERTICAL:
                this.paint.setStrokeWidth(getMeasuredWidth());
                this.path.moveTo(getMeasuredWidth() * 0.5f, 0.0F);
                this.path.lineTo(getMeasuredWidth() * 0.5f, getMeasuredHeight());
                break;
        }
        this.paint.setPathEffect(this.pe);
        canvas.drawPath(this.path, this.paint);
    }
}
