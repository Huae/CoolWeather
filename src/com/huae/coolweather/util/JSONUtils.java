package com.huae.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.huae.coolweather.db.CoolWeatherDB;
import com.huae.coolweather.entity.City;
import com.huae.coolweather.entity.County;
import com.huae.coolweather.entity.Province;

/**
 * json解析工具类
 * @author huang
 *
 */
public class JSONUtils {

	public static boolean parseProvince(CoolWeatherDB coolWeatherDB, String response) {
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int provinceId = jsonObject.getInt("id");
				String provinceName = jsonObject.getString("name");

				Province province = new Province(provinceId, provinceName);
				coolWeatherDB.saveProvince(province);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean parseCity(CoolWeatherDB coolWeatherDB, String response, int provinceId){
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int cityId = jsonObject.getInt("id");
				String cityName = jsonObject.getString("name");

				City city = new City(cityId, cityName, provinceId);
				coolWeatherDB.saveCity(city);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean parseCounty(CoolWeatherDB coolWeatherDB, String response,int cityId) {
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int countyId = jsonObject.getInt("id");
				String countyName = jsonObject.getString("name");
				String weatherId = jsonObject.getString("weather_id");
				
				County county = new County(countyId, countyName, cityId, weatherId);
				coolWeatherDB.saveCounty(county);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解析天气数据
	 * @param response
	 */
	public static void parseWeather(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject heWeather0 = jsonObject.getJSONArray("HeWeather").getJSONObject(0);
			// 获取城市名
			JSONObject basic = heWeather0.getJSONObject("basic");
			String cityName = basic.getString("city");
			// 获取发布时间
			String publishTime = heWeather0.getJSONObject("update").getString("loc").split(" ")[1];
			// 当前时间
			String currentTime = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA).format(new Date());
			// 天气描述
			String weatherDesc = heWeather0.getJSONObject("now").getString("cond_txt");
			// 天气
			String temp = heWeather0.getJSONObject("now").getString("tmp")+"℃";
			
			// 保存到本地
			saveWeather(context, cityName,publishTime,currentTime,weatherDesc,temp);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static void saveWeather(Context context, String cityName, String publishTime,
			String currentTime, String weatherDesc, String temp) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean("city_selected", true);
		editor.putString("cityName", cityName);
		editor.putString("publishTime", publishTime);
		editor.putString("currentTime", currentTime);
		editor.putString("weatherDesc", weatherDesc);
		editor.putString("temp", temp);
		editor.commit();
	}

}
