package com.neuroandroid.pyweather.loader;

import android.content.Context;

import com.google.gson.Gson;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.utils.CloseUtils;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class CityLoader {
    public static CityBean getAllCities(Context context) {
        InputStream is = null;
        try {
            is = context.getAssets().open("city.json");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            String json = new String(buf, 0, buf.length, "utf-8");
            CityBean cityBean = new Gson().fromJson(json, CityBean.class);
            ArrayList<CityBean.CityListBean> dataList = cityBean.getDataList();
            ArrayList<String> provinceList = new ArrayList<>();
            for (int i = 0, size = dataList.size(); i < size; i++) {
                CityBean.CityListBean cityListBean = dataList.get(i);
                if (i == 0) {
                    provinceList.add(cityListBean.getProvinceZh());
                } else {
                    if (!provinceList.contains(cityListBean.getProvinceZh())) {
                        provinceList.add(cityListBean.getProvinceZh());
                    }
                }
            }
            cityBean.setProvinceList(provinceList);
            return cityBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(is);
        }
    }
}
