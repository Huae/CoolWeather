package com.huae.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huae.coolweather.entity.City;
import com.huae.coolweather.entity.County;
import com.huae.coolweather.entity.Province;

/**
 * 数据库常用操作
 * 
 * @author huang
 * 
 */
public class CoolWeatherDB {
	// 数据库名
	public static final String BD_NAME = "cool_weather";
	// 数据库版本
	public static final int DB_VERSION = 1;
	// 单例
	private static CoolWeatherDB coolWeatherDB;
	// 数据库实例
	private SQLiteDatabase db;

	/**
	 * 构造函数私有化
	 */
	private CoolWeatherDB(Context context) {
		// 创建数据库
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				BD_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 将province存储到数据库
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_id", province.getProvineId());
			// 插入数据
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库读取province
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		// 遍历数据
		if (cursor.moveToFirst()) {
			do {
				int provinceId = cursor.getInt(cursor.getColumnIndex("province_id"));
				String provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
				// 添加到集合
				Province province = new Province(provinceId, provinceName);
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将city保存到数据库
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_id", city.getCityId());
			values.put("city_name", city.getCityName());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * 从数据库读取某省下所有的城市信息。
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCityId((cursor.getInt(cursor.getColumnIndex("city_id"))));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将County实例存储到数据库。
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_id", county.getCountyId());
			values.put("county_name", county.getCountyName());
			values.put("city_id", county.getCityId());
			values.put("weather_id", county.getWeatherId());
			db.insert("County", null, values);
		}
	}

	/**
	 * 从数据库读取某城市下所有的县信息。
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setCityId((cursor.getInt(cursor.getColumnIndex("county_id"))));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setWeatherId((cursor.getString(cursor.getColumnIndex("weather_id"))));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
}
