package com.huae.coolweather.entity;

public class County {
	private int countyId;
	private String countyName;
	private int cityId;
	private String weatherId;
	
	public County() {
	}
	public int getCountyId() {
		return countyId;
	}
	public void setCountyId(int countyId) {
		this.countyId = countyId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(String weatherId) {
		this.weatherId = weatherId;
	}
	public County(int countyId, String countyName, int cityId, String weatherId) {
		super();
		this.countyId = countyId;
		this.countyName = countyName;
		this.cityId = cityId;
		this.weatherId = weatherId;
	}
	
}
