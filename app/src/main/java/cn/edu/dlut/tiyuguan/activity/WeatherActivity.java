package cn.edu.dlut.tiyuguan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.service.WeatherService;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

public class WeatherActivity extends Activity {

	public static final String STORE_WEATHER = "store_weather";
	public int[] a = new int[10];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);

		SharedPreferences sp = getSharedPreferences(STORE_WEATHER,
				WeatherActivity.MODE_PRIVATE);// 得到保存的天气

		long currentTime = System.currentTimeMillis();
		long vaildTime = sp.getLong("validTime", currentTime);

		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		// 判断是否联网
		if (info != null && info.isAvailable()) {
			// 比较天气缓存文件中的有效期，如果超时了，则访问网络更新天气，否则从缓存文件中得到天气情况
			if (currentTime >= vaildTime) {
				new LoadWeatherInfoTask().execute("http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101070201","http://m.weather.com.cn/atad/101070201.html");
				ToastUtil.showInfoToast(this,"1");
			} else {
				setWeatherSituation(sp);
				ToastUtil.showInfoToast(this, "2");
			}
		} else {
			ToastUtil.showInfoToast(this, "无互联网连接！");
			setWeatherSituation(sp);
		}
	}

	// 更新天气情况,并将得到的信息保存在文件中
	public int[] setWeatherSituation(String jsonStr, String jsonStr2) {
		try {
			SharedPreferences sp = getSharedPreferences(STORE_WEATHER,
					WeatherActivity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			final JSONObject forecast = new JSONObject(jsonStr)
					.getJSONObject("forecast");
			final JSONObject weatherinfo = new JSONObject(jsonStr2)
					.getJSONObject("weatherinfo");
			final JSONArray alert = new JSONObject(jsonStr)
					.getJSONArray("alert");
			// 设置背景图片
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
			layout.setBackgroundResource(setBackgroudImage(forecast
					.optString("weather1")));

			// 设置当天天气状况
			TextView weather = (TextView) this.findViewById(R.id.weather);
			weather.setText(forecast.optString("weather1"));
			editor.putString("weather1", forecast.optString("weather1"));

			// 设置温度
			TextView temperature = (TextView) this
					.findViewById(R.id.temperature);
			TextView temp1 = (TextView) this.findViewById(R.id.temp1);
			TextView temp2 = (TextView) this.findViewById(R.id.temp2);
			TextView temp3 = (TextView) this.findViewById(R.id.temp3);
			TextView temp4 = (TextView) this.findViewById(R.id.temp4);
			TextView temp5 = (TextView) this.findViewById(R.id.temp5);

			temp1.setText(forecast.optString("temp1"));
			temp2.setText(forecast.optString("temp2"));
			temp3.setText(forecast.optString("temp3"));
			temp4.setText(forecast.optString("temp4"));
			temp5.setText(forecast.optString("temp5"));
			editor.putString("temp1", forecast.optString("temp1"));
			editor.putString("temp2", forecast.optString("temp2"));
			editor.putString("temp3", forecast.optString("temp3"));
			editor.putString("temp4", forecast.optString("temp4"));
			editor.putString("temp5", forecast.optString("temp5"));

			String[] str1 = forecast.optString("temp1").split("[^0-9]+");
			a[0] = Integer.parseInt(str1[0]);
			a[5] = Integer.parseInt(str1[1]);
			String[] str2 = forecast.optString("temp2").split("[^0-9]+");
			a[1] = Integer.parseInt(str2[0]);
			a[6] = Integer.parseInt(str2[1]);
			String[] str3 = forecast.optString("temp3").split("[^0-9]+");
			a[2] = Integer.parseInt(str3[0]);
			a[7] = Integer.parseInt(str3[1]);
			String[] str4 = forecast.optString("temp4").split("[^0-9]+");
			a[3] = Integer.parseInt(str4[0]);
			a[8] = Integer.parseInt(str4[1]);
			String[] str5 = forecast.optString("temp5").split("[^0-9]+");
			a[4] = Integer.parseInt(str5[0]);
			a[9] = Integer.parseInt(str5[1]);
			temperature.setText(Integer.toString((a[0] + a[5]) / 2) + "℃");
			lineView(a);

			JSONArray jsonArray = new JSONArray();
			for (int i : a) {
				jsonArray.put(i);
			}
			editor.putString("a", jsonArray.toString());

			// 设置天气图标
			ImageView imageView1 = (ImageView) findViewById(R.id.weather_icon01);
			ImageView imageView2 = (ImageView) findViewById(R.id.weather_icon02);
			ImageView imageView3 = (ImageView) findViewById(R.id.weather_icon03);
			ImageView imageView4 = (ImageView) findViewById(R.id.weather_icon04);
			ImageView imageView5 = (ImageView) findViewById(R.id.weather_icon05);
			imageView1.setImageResource(getWeatherImg(forecast
					.optString("weather1")));
			imageView2.setImageResource(getWeatherImg(forecast
					.optString("weather2")));
			imageView3.setImageResource(getWeatherImg(forecast
					.optString("weather3")));
			imageView4.setImageResource(getWeatherImg(forecast
					.optString("weather4")));
			imageView5.setImageResource(getWeatherImg(forecast
					.optString("weather5")));
			editor.putString("weather1", forecast.optString("weather1"));
			editor.putString("weather2", forecast.optString("weather2"));
			editor.putString("weather3", forecast.optString("weather3"));
			editor.putString("weather4", forecast.optString("weather4"));
			editor.putString("weather5", forecast.optString("weather5"));

			// 设置风力
			TextView wind = (TextView) this.findViewById(R.id.wind);
			wind.setText(forecast.optString("wind1"));
			editor.putString("wind1", forecast.optString("wind1"));

			// 设置日期
			TextView week3 = (TextView) this.findViewById(R.id.week3);
			TextView week4 = (TextView) this.findViewById(R.id.week4);
			TextView week5 = (TextView) this.findViewById(R.id.week5);
			switch (weatherinfo.optString("week")) {
			case "星期一":
				week3.setText("星期三");
				week4.setText("星期四");
				week5.setText("星期五");
				break;
			case "星期二":
				week3.setText("星期四");
				week4.setText("星期五");
				week5.setText("星期六");
				break;
			case "星期三":
				week3.setText("星期五");
				week4.setText("星期六");
				week5.setText("星期日");
				break;
			case "星期四":
				week3.setText("星期六");
				week4.setText("星期日");
				week5.setText("星期一");
				break;
			case "星期五":
				week3.setText("星期日");
				week4.setText("星期一");
				week5.setText("星期二");
				break;
			case "星期六":
				week3.setText("星期一");
				week4.setText("星期二");
				week5.setText("星期三");
				break;
			case "星期日":
				week3.setText("星期二");
				week4.setText("星期三");
				week5.setText("星期四");
				break;
			}
			editor.putString("week3", week3.getText().toString());
			editor.putString("week4", week4.getText().toString());
			editor.putString("week5", week5.getText().toString());

			// 设置预警内容
			TextView detail = (TextView) this.findViewById(R.id.detail);
			detail.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							WeatherActivity.this).setTitle("天气预警").create();
					try {
						alertDialog.setMessage(alert.getJSONObject(0)
								.optString("detail"));
						Window window = alertDialog.getWindow();
						WindowManager.LayoutParams lp = window.getAttributes();
						// 设置透明度
						lp.alpha = 0.9f;
						window.setAttributes(lp);
						alertDialog.show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			editor.putString("detail",
					alert.getJSONObject(0).optString("detail"));

			long validTime = System.currentTimeMillis();
			// 四个小时跟新一次天气
			validTime = validTime + 4 * 60 * 60 * 1000;
			editor.putLong("validTime", validTime);

			editor.commit();
			return a;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 从缓存文件中得到天气情况
	public void setWeatherSituation(final SharedPreferences shared) {

		// 设置背景图片
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		layout.setBackgroundResource(setBackgroudImage(shared.getString(
				"weather1", "")));

		// 设置当天天气状况
		TextView weather = (TextView) this.findViewById(R.id.weather);
		weather.setText(shared.getString("weather1", ""));

		// 设置温度
		TextView temperature = (TextView) this.findViewById(R.id.temperature);
		TextView temp1 = (TextView) this.findViewById(R.id.temp1);
		TextView temp2 = (TextView) this.findViewById(R.id.temp2);
		TextView temp3 = (TextView) this.findViewById(R.id.temp3);
		TextView temp4 = (TextView) this.findViewById(R.id.temp4);
		TextView temp5 = (TextView) this.findViewById(R.id.temp5);
		temp1.setText(shared.getString("temp1", ""));
		temp2.setText(shared.getString("temp2", ""));
		temp3.setText(shared.getString("temp3", ""));
		temp4.setText(shared.getString("temp4", ""));
		temp5.setText(shared.getString("temp5", ""));

		try {
			JSONArray jsonArray = new JSONArray(shared.getString("a", "[]"));// 读取数组
			for (int i = 0; i < jsonArray.length(); i++) {
				a[i] = jsonArray.getInt(i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		lineView(a);
		temperature.setText(Integer.toString((a[0] + a[5]) / 2) + "℃");

		// 设置天气图标
		ImageView imageView1 = (ImageView) findViewById(R.id.weather_icon01);
		ImageView imageView2 = (ImageView) findViewById(R.id.weather_icon02);
		ImageView imageView3 = (ImageView) findViewById(R.id.weather_icon03);
		ImageView imageView4 = (ImageView) findViewById(R.id.weather_icon04);
		ImageView imageView5 = (ImageView) findViewById(R.id.weather_icon05);
		imageView1.setImageResource(getWeatherImg(shared.getString("weather1",
				"")));
		imageView2.setImageResource(getWeatherImg(shared.getString("weather2",
				"")));
		imageView3.setImageResource(getWeatherImg(shared.getString("weather3",
				"")));
		imageView4.setImageResource(getWeatherImg(shared.getString("weather4",
				"")));
		imageView5.setImageResource(getWeatherImg(shared.getString("weather5",
				"")));

		// 设置风力
		TextView wind = (TextView) this.findViewById(R.id.wind);
		wind.setText(shared.getString("wind1", ""));

		// 设置日期
		TextView week3 = (TextView) this.findViewById(R.id.week3);
		TextView week4 = (TextView) this.findViewById(R.id.week4);
		TextView week5 = (TextView) this.findViewById(R.id.week5);
		week3.setText(shared.getString("week3", ""));
		week4.setText(shared.getString("week4", ""));
		week5.setText(shared.getString("week5", ""));

		// 设置预警内容
		TextView detail = (TextView) this.findViewById(R.id.detail);
		detail.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						WeatherActivity.this).setTitle("天气预警").create();
				alertDialog.setMessage(shared.getString("detail", ""));
				Window window = alertDialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				// 设置透明度
				lp.alpha = 1.0f;
				window.setAttributes(lp);
				alertDialog.show();
			}
		});
	}

	// 由天气情况得到图片
	private static int getWeatherImg(String weather) {

		if (weather.matches(".*多云")) {
			return R.drawable.weather_sunny_to_overcast;
		} else if (weather.matches("^晴")) {
			return R.drawable.weather_sunny;
		} else if (weather.matches("^阴")) {
			return R.drawable.weather_rainy;
		} else if (weather.matches("^小雨")) {
			return R.drawable.weather_rainy;
		} else if (weather.matches("^大雨")) {
			return R.drawable.weather_heavy_rain;
		} else if (weather.equals(".*雨")) {
			return R.drawable.weather_media_rain;
		} else if (weather.equals(".*雪")) {
			return R.drawable.weather_snow;
		} else {
			return R.drawable.weather_sunny;
		}
	}

	// 根据天气状况设置背景图片
	private int setBackgroudImage(String weather) {

		if (weather.matches(".*多云")) {
			return R.drawable.cloudy;
		} else if (weather.matches("^晴")) {
			return R.drawable.sunny;
		} else if (weather.matches("^阴")) {
			return R.drawable.overcast;
		} else if (weather.matches(".*雨")) {
			return R.drawable.rain;
		} else if (weather.equals(".*雪")) {
			return R.drawable.snow;
		} else {
			return R.drawable.sunny;
		}
	}

	/**
	 * 画线
	 * @param a
	 */
	public void lineView(int[] a) {
		// 包含一系列XYSeries，是最终的数据结构
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		System.out.println(a);
		XYSeries series = new XYSeries("");
		series.add(1, a[0]);
		series.add(2, a[1]);
		series.add(3, a[2]);
		series.add(4, a[3]);
		series.add(5, a[4]);
		mDataset.addSeries(series);

		XYSeries seriesTwo = new XYSeries("");
		seriesTwo.add(1, a[5]);
		seriesTwo.add(2, a[6]);
		seriesTwo.add(3, a[7]);
		seriesTwo.add(4, a[8]);
		seriesTwo.add(5, a[9]);
		mDataset.addSeries(seriesTwo);

		// 主要用来定义一个图的整体风格，设置xTitle,yTitle,chartName等等整体性的风格
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		// 设置图表的X轴的当前方向
		mRenderer
				.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		mRenderer.setYAxisMin(-20);// 设置y轴最小值是0
		mRenderer.setYAxisMax(40);
		mRenderer.setXAxisMax(5);
		mRenderer.setShowAxes(false);
		mRenderer.setShowLegend(false); // 设置是否显示图例.
		mRenderer.setShowCustomTextGrid(false);
		mRenderer.setZoomEnabled(false, false);// 禁止放大缩小
		mRenderer.setPanEnabled(false, false); // 禁止左右拖动
		mRenderer.setMargins(new int[] { 0, 0, -150, 0 });// //设置图表的外边框(上/左/下/右)

		// 主要是用来设置一条线条的风格，颜色啊，粗细之类的
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);// 设置颜色
		r.setDisplayChartValues(false);// 将点的值显示出来
		r.setChartValuesSpacing(10);// 显示的点的值与图的距离
		r.setLineWidth(3);// 设置线宽
		mRenderer.addSeriesRenderer(r);

		XYSeriesRenderer rTwo = new XYSeriesRenderer();
		rTwo.setColor(Color.WHITE);// 设置颜色
		rTwo.setDisplayChartValues(false);// 将点的值显示出来
		rTwo.setChartValuesSpacing(10);// 显示的点的值与图的距离
		rTwo.setLineWidth(3);// 设置线宽
		mRenderer.addSeriesRenderer(rTwo);

		GraphicalView view = ChartFactory.getCubeLineChartView(this, mDataset,
				mRenderer, 0.3f);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart);
		layout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/**
	 * 异步加载的任务
	 * 
	 * @author asus
	 * 
	 */
	class LoadWeatherInfoTask extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			String uri = params[0];
			String uri0 = params[1];
			String jsonStr = WeatherService
					.getHtml(uri);
			System.out.println(jsonStr);
			String jsonStr2 = WeatherService
					.getHtml(uri0);
			String[] results=new String[2];
			results[0]=jsonStr;
			results[1]=jsonStr2;
			return results;
		}

		@Override
		protected void onPostExecute(String result[]) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setWeatherSituation(result[0], result[1]);
			System.out.println(result[0]);
			System.out.println(result[1]);

		}

	}

}
