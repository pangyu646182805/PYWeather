package com.neuroandroid.pyweather.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/6/2.
 */

public class PYCityStore extends SQLiteOpenHelper {
    @Nullable
    private static PYCityStore sInstance = null;
    private static final String DATABASE_NAME = "py_weather.db";
    private static final String TABLE_NAME = "city_list";

    public PYCityStore(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase, TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 单例模式
     */
    @NonNull
    public static synchronized PYCityStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new PYCityStore(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * 创建表格
     */
    private void createTable(@NonNull final SQLiteDatabase db, final String tableName) {
        String sql = "create table " + tableName + " (city_id varchar(20) primary key, city_name varchar(20) not null, " +
                "weather_code integer not null, max integer not null, min integer not null, weather_desc string not null)";
        db.execSQL(sql);
    }

    /**
     * 添加到数据库
     */
    public synchronized void addItem(String cityId, String cityName, int weatherCode, int max, int min, String weatherDesc) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, getContentValues(cityId, cityName, weatherCode, max, min, weatherDesc));
        db.close();
    }

    /**
     * @param cityId 城市ID
     * @return 删除的记录数量
     */
    public synchronized int deleteByCityId(String cityId) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_NAME, "city_id=?", new String[]{cityId});
        db.close();
        return delete;
    }

    /**
     * @param cityId 城市ID
     * @return 返回找到符合记录的数量
     */
    public synchronized int find(String cityId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "city_id=?", new String[]{cityId},
                null, null, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }

    /**
     * 更新数据库
     */
    public synchronized int update(String cityId, String cityName, int weatherCode, int max, int min, String weatherDesc) {
        SQLiteDatabase db = getWritableDatabase();
        int update = db.update(TABLE_NAME, getContentValues(cityId, cityName, weatherCode, max, min, weatherDesc),
                "city_id=?", new String[]{cityId});
        db.close();
        return update;
    }

    /**
     * 获取数据库里面TABLE_NAME表的所有数据
     */
    public ArrayList<CityBean.CityListBean> getAllCities(String district) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null,
                null, null, null);
        ArrayList<CityBean.CityListBean> dataList = new ArrayList<>();
        CityBean.CityListBean city;
        CityBean.CityListBean locationCity = null;

        while (cursor.moveToNext()) {
            city = new CityBean.CityListBean();
            city.setId(cursor.getString(cursor.getColumnIndex("city_id")));
            String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
            city.setCityZh(cityName);
            city.setWeatherCode(cursor.getInt(cursor.getColumnIndex("weather_code")));
            city.setMax(cursor.getInt(cursor.getColumnIndex("max")));
            city.setMin(cursor.getInt(cursor.getColumnIndex("min")));
            city.setWeatherDesc(cursor.getString(cursor.getColumnIndex("weather_desc")));
            if (!UIUtils.isEmpty(district)) {
                if (district.contains(cityName) || cityName.contains(district)) {
                    locationCity = city;
                } else {
                    dataList.add(city);
                }
            } else {
                dataList.add(city);
            }
        }
        db.close();
        cursor.close();
        if (locationCity != null) {
            dataList.add(0, locationCity);
        }
        return dataList;
    }

    private ContentValues getContentValues(String cityId, String cityName, int weatherCode, int max, int min, String weatherDesc) {
        ContentValues values = new ContentValues();
        values.put("city_id", cityId);
        values.put("city_name", cityName);
        values.put("weather_code", weatherCode);
        values.put("max", max);
        values.put("min", min);
        values.put("weather_desc", weatherDesc);
        return values;
    }
}
