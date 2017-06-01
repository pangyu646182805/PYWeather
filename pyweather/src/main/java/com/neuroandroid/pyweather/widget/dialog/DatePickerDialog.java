package com.neuroandroid.pyweather.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;
import com.neuroandroid.pyweather.widget.dialog.base.BaseDialog;
import com.neuroandroid.pyweather.widget.picker.Type;
import com.neuroandroid.pyweather.widget.picker.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/3/9.
 */

public class DatePickerDialog extends BaseDialog<DatePickerDialog> {
    @BindView(R.id.tv_right)
    NoPaddingTextView mTvRight;
    private Type mType;
    private WheelTime mWheelTime;
    @BindView(R.id.ll_picker)
    LinearLayout mLlPicker;

    public DatePickerDialog(@NonNull Context context) {
        super(context);
    }

    public DatePickerDialog(@NonNull Context context, Type type) {
        super(context);
        mType = type;
        initWheelTime();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_date_picker;
    }

    @Override
    protected void initView() {
    }

    private void initWheelTime() {
        mWheelTime = new WheelTime(mLlPicker, mType);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mWheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     */
    public DatePickerDialog setRange(int startYear, int endYear) {
        mWheelTime.setStartYear(startYear);
        mWheelTime.setEndYear(endYear);
        return this;
    }

    /**
     * 设置选中时间
     *
     * @param date 时间
     */
    public DatePickerDialog setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mWheelTime.setPicker(year, month, day, hours, minute);
        return this;
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public DatePickerDialog setCyclic(boolean cyclic) {
        mWheelTime.setCyclic(cyclic);
        return this;
    }

    public DatePickerDialog setConfirmClickListener(OnClickListener onClickListener) {
        mTvRight.setOnClickListener(v -> {
            if (onClickListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(mWheelTime.getTime());
                    onClickListener.onClick(DatePickerDialog.this, v, date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    public interface OnClickListener {
        void onClick(DatePickerDialog pickerDialog, View v, Date date);
    }
}
