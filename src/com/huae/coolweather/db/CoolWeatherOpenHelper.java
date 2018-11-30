package com.huae.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * CoolWeather数据库访问类
 * 
 * @author huang
 * 
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	// 建表语句
	private static final String CREATE_PROVINCE = "create table Province ("
			+ "province_id integer," 
			+ "province_name text)";
	
	private static final String CREATE_CITY =  "create table City ("
			+ "city_id integer, "
			+ "city_name text, "
			+ "province_id integer)";
	
	private static final String CREATE_COUNTY = "create table County ("
			+ "county_id integer, "
			+ "county_name text, "
			+ "city_id integer, "
			+ "weather_id text)";

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建province表
		db.execSQL(CREATE_PROVINCE);
		// 创建city表
		db.execSQL(CREATE_CITY);
		// 创建county表
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
