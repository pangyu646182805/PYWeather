package com.neuroandroid.pyweather.event;

/**
 * Created by NeuroAndroid on 2017/5/19.
 */

public class BaseEvent {
    private int eventFlag;

    public int getEventFlag() {
        return eventFlag;
    }

    public BaseEvent setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
        return this;
    }
}
