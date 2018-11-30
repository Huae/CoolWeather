package com.huae.coolweather.service;

import java.util.ResourceBundle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.huae.coolweather.receiver.AutoUpdateReceiver;
import com.huae.coolweather.util.HttpCallbackListener;
import com.huae.coolweather.util.HttpUtil;
import com.huae.coolweather.util.JSONUtils;

/**
 * 自动更新天气服务
 * 
 * @author huang
 */
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 更新天气
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateWeather();
			}
		}).start();

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long time = 4 * 60 * 60 * 1000; // 4小时
		//long time = 10 * 1000; // 10s 测试用
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent operation = PendingIntent.getBroadcast(this, 0, i, 0);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + time, operation);
		// alarmManager.cancel(operation); 取消任务
		return super.onStartCommand(intent, flags, startId);
	}

	private void updateWeather() {
		//Log.i(this.getClass().getSimpleName(), "更新天气...");
		ResourceBundle bundle = ResourceBundle.getBundle("key");
		String key = bundle.getString("heWeatherKey");
		final String weatherId = PreferenceManager.getDefaultSharedPreferences(
				this).getString("weatherId", "");
		String address = "http://guolin.tech/api/weather?key=" + key
				+ "&cityid=" + weatherId;

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				JSONUtils.parseWeather(AutoUpdateService.this, response,
						weatherId);
			}

			@Override
			public void onError(Exception e) {

			}
		});
	}
}
