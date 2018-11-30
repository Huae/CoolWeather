package com.huae.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huae.coolweather.R;
import com.huae.coolweather.db.CoolWeatherDB;
import com.huae.coolweather.entity.City;
import com.huae.coolweather.entity.County;
import com.huae.coolweather.entity.Province;
import com.huae.coolweather.util.HttpCallbackListener;
import com.huae.coolweather.util.HttpUtil;
import com.huae.coolweather.util.JSONUtils;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	// 组件
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	// 列表适配器
	private ArrayAdapter<String> adapter;
	// 数据库操作类
	private CoolWeatherDB coolWeatherDB;
	// 列表显示数据
	private List<String> dataList = new ArrayList<String>();

	/** 省列表. */
	private List<Province> provinceList;
	/** 市列表. */
	private List<City> cityList;
	/** 县列表. */
	private List<County> countyList;

	// 选中的省
	private Province selectProvince;
	// 选中的市
	private City selectCity;
	// 当前选中的级别
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		// 初始化控件
		titleText = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);
		// 列表绑定适配器
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);

		coolWeatherDB = CoolWeatherDB.getInstance(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectProvince = provinceList.get(index);
					// 加载市数据
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectCity = cityList.get(index);
					// 加载县数据
					queryCounties();
				}
			}

		});
		// 加载省数据
		queryProvinces();
	}

	/**
	 * 查询省数据,先查数据库,没有则从网络获取
	 */
	private void queryProvinces() {
		// 数据库查询
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else { // 从网络查询
			queryFromServer(0, "province");
		}
	}

	/**
	 * 查询县数据
	 */
	private void queryCounties() {
		// 数据库查
		countyList = coolWeatherDB.loadCounties(selectCity.getCityId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else { // 网络查
			queryFromServer(selectCity.getCityId(), "county");
		}
	}

	/**
	 * 查询市数据
	 */
	private void queryCities() {
		// 数据库查
		cityList = coolWeatherDB.loadCities(selectProvince.getProvineId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else { // 网络查
			queryFromServer(selectProvince.getProvineId(), "city");
		}
	}

	/**
	 * 从网络查询数据
	 */
	private void queryFromServer(final int code, final String type) {
		// 拼接地址
		StringBuilder address = new StringBuilder(
				"http://guolin.tech/api/china/");
		if ("city".equals(type)) {
			address.append(code);
		} else if ("county".equals(type)) {
			address.append(selectProvince.getProvineId() + "/" + code);
		}
		// 显示加载界面
		showProgressDialog();
		HttpUtil.sendHttpRequest(address.toString(),
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						boolean result = false;
						if ("province".equals(type)) {
							result = JSONUtils.parseProvince(coolWeatherDB, response);
						} else if ("city".equals(type)) {
							result = JSONUtils.parseCity(coolWeatherDB, response,code);
						} else if ("county".equals(type)) {
							result = JSONUtils.parseCounty(coolWeatherDB, response,code);
						}
						if (result) {
							// 成功后更新列表
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									closeProgressDialog();
									if ("province".equals(type)) {
										queryProvinces();
									} else if ("city".equals(type)) {
										queryCities();
									} else if ("county".equals(type)) {
										queryCounties();
									}
								}
							});
						}
					}

					@Override
					public void onError(Exception e) {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								closeProgressDialog();
								Toast.makeText(ChooseAreaActivity.this, "数据加载失败...", Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
	}

	private void showProgressDialog() {
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("加载中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	// 返回键事件处理
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
}
