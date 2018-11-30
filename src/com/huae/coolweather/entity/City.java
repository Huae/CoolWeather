package com.huae.coolweather.entity;

public class City {
	private int cityId;
	private String cityName;
	private int provinceId;
	
	public City() {
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public City(int cityId, String cityName, int provinceId) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.provinceId = provinceId;
	}
}
