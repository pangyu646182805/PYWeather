package com.neuroandroid.pyweather.event;

import com.neuroandroid.pyweather.bean.CityBean;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class CityManageEvent extends BaseEvent {
    private CityBean.CityListBean selectCity;

    public CityManageEvent() {
        setEventFlag(EVENT_CITY_MANAGE);
    }

    public CityBean.CityListBean getSelectCity() {
        return selectCity;
    }

    public CityManageEvent setSelectCity(CityBean.CityListBean selectCity) {
        this.selectCity = selectCity;
        return this;
    }
}
