package com.huae.coolweather.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

}
