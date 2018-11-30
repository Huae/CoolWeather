package com.huae.coolweather.activity;

import java.util.ResourceBundle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.huae.coolweather.R;
import com.huae.coolweather.service.AutoUpdateService;
import com.huae.coolweather.util.HttpCallbackListener;
import com.huae.coolweather.util.HttpUtil;
import com.huae.coolweather.util.JSONUtils;

public class WeatherActivity extends Activity implements OnClickListener{
	// 城市名
	private TextView cityName;
	// 发布时间
	private TextView publishTime;
	// 当前时间
	private TextView currentTime;
	// 天气描写信息
	private TextView weatherDescp;
	// 温度
	private TextView temp;
	// 切换城市按钮
	private Button switchBtn;
	// 手动刷新按钮
	private Button refreshBtn;
	// 县对应的天气id
	private String weatherId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		// 实例化控件
		cityName = (TextView) findViewById(R.id.city_name);
		publishTime = (TextView) findViewById(R.id.publish_text);
		currentTime = (TextView) findViewById(R.id.current_date);
		weatherDescp = (TextView) findViewById(R.id.weather_desp);
		temp = (TextView) findViewById(R.id.temp);
		
		// 获取城市id
		weatherId = getIntent().getStringExtra("weatherId");
		if(!TextUtils.isEmpty(weatherId)){
			// 查询天气信息
			publishTime.setText("同步中...");
			queryWeather(weatherId);
		}else{
			// 显示缓存天气
			showWeather();
		}
		
		switchBtn = (Button) findViewById(R.id.switch_city);
		refreshBtn = (Button) findViewById(R.id.refresh_weather);
		
		switchBtn.setOnClickListener(this);
		refreshBtn.setOnClickListener(this);
	
	}


	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityName.setText(prefs.getString("cityName", ""));
		publishTime.setText("今天"+prefs.getString("publishTime", "")+"发布");
		currentTime.setText(prefs.getString("currentTime", ""));
		weatherDescp.setText(prefs.getString("weatherDesc", ""));
		temp.setText(prefs.getString("temp", ""));
		
		// 启动定时更新任务
		Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
		startService(intent);
	}


	/**
	 * 联网获取天气数据
	 * @param weatherId
	 */
	private void queryWeather(final String weatherId) {
		ResourceBundle bundle = ResourceBundle.getBundle("key");
		String key = bundle.getString("heWeatherKey");
		String address = "http://guolin.tech/api/weather?key="+key+"&cityid="+weatherId;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// 解析天气数据
				JSONUtils.parseWeather(WeatherActivity.this,response,weatherId);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// 显示天气数据
						showWeather();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishTime.setText("同步失败...");
					}
				});
			}
		});
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.refresh_weather:
			// 刷新天气
			publishTime.setText("同步中...");
			if(!TextUtils.isEmpty(weatherId)){
				queryWeather(weatherId);
			}
			break;
		case R.id.switch_city:
			// 切换城市
			Intent intent =new Intent(WeatherActivity.this, ChooseAreaActivity.class);
			intent.putExtra("switch_city", true);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
