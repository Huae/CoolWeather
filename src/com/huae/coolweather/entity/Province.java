package com.huae.coolweather.entity;

/**
 * province实体类
 * 
 * @author huang
 */
public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;

	public Province() {

	}

	public Province(int id, String provinceName, String provinceCode) {
		this.id = id;
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
}
