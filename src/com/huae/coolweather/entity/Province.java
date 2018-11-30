package com.huae.coolweather.entity;

/**
 * province实体类
 * 
 * @author huang
 */
public class Province {
	private int provineId;
	private String provinceName;
	public int getProvineId() {
		return provineId;
	}
	public void setProvineId(int provineId) {
		this.provineId = provineId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Province(int provineId, String provinceName) {
		super();
		this.provineId = provineId;
		this.provinceName = provinceName;
	}
}
