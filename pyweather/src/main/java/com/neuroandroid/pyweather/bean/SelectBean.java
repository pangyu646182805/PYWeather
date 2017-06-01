package com.neuroandroid.pyweather.bean;

/**
 * Created by NeuroAndroid on 2017/3/9.
 */

public class SelectBean implements ISelect {
    private boolean isSelected;
    private String text;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
