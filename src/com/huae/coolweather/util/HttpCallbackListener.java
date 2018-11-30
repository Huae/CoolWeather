package com.huae.coolweather.util;

public interface HttpCallbackListener {
	/**
	 * 请求成功
	 * @param response 返回内容
	 */
	void onFinish(String response);
	/**
	 * 请求失败
	 * @param e 异常类型
	 */
	void onError(Exception e);
}
