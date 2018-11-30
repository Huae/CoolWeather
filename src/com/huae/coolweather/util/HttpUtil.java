package com.huae.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP连接工具
 * @author huang
 *
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					// 设置连接属性
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// 处理响应
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader  = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					StringBuilder response = new StringBuilder();
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					if(listener != null){
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if(listener != null){
						listener.onError(e);
					}
				}finally{
					// 断开连接
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
