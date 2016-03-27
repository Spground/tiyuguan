package cn.edu.dlut.tiyuguan.global;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baidu.mapapi.SDKInitializer;

public class MApplication extends Application{

	public static HttpClient httpClient;//用户打开程序用到的全局httpclient实例
	private static String sessionName = "JSESSIONID";
	private static String cookieName = "SESSION_LOGIN_USERNAME";
	private static String cookieValue = "";//用来存放
	public  static  Boolean rememberme;
	private static SharedPreferences cookieFile;
	private static Editor editor;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("onCreate Userinfo");
		//只新建一次
		cookieFile = this.getSharedPreferences("cookieFile", 0);
		editor = cookieFile.edit();
		cookieValue = cookieFile.getString("cookieValue", "");
		rememberme = cookieFile.getBoolean("rememberme", false);
		httpClient = new DefaultHttpClient();
		SDKInitializer.initialize(getApplicationContext());//在调用任何百度地图组件的时候都得初始化
	}
	public String getcookieName() {
		return cookieName;
	}
	//
	public  String getcookieValue() {
		cookieValue=cookieFile.getString("cookieValue", "");
		return cookieValue;
	}
	
	public  void  setcookieValue(String cookieValue0) {
		if(cookieValue0 != "")
		{
		editor.putString("cookieValue", cookieValue0);
		editor.commit();
		}
		cookieValue = cookieValue0;
	}
}
