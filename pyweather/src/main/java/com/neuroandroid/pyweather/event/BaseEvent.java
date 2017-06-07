package com.neuroandroid.pyweather.event;

/**
 * Created by NeuroAndroid on 2017/5/19.
 */

public class BaseEvent {
    public static final int EVENT_CITY_MANAGE = 200;
    public static final int EVENT_CUSTOM_BACKGROUND = 300;
    public static final int EVENT_LINE_TYPE = 400;
    private int eventFlag;

    public int getEventFlag() {
        return eventFlag;
    }

    public BaseEvent setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
        return this;
    }
}
