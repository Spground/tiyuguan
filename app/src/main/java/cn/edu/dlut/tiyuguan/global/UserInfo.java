package cn.edu.dlut.tiyuguan.global;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo extends Application{

	public static HttpClient httpClient;//用户打开程序用到的全局httpclient实例
	private static String sessionName="JSESSIONID";
	private static String cookieName="SESSION_LOGIN_USERNAME";
	private static String cookieValue="";//用来存放
	public  static  Boolean rememberme;
	private static SharedPreferences cookieFile;
	private static Editor editor;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("onCreate Userinfo");
		//只新建一次
		cookieFile=this.getSharedPreferences("cookieFile", 0);
		editor=cookieFile.edit();
		cookieValue = cookieFile.getString("cookieValue","");
		rememberme=cookieFile.getBoolean("rememberme", false);
		httpClient=new DefaultHttpClient();
	}
	public String getcookieName()
	{
		return cookieName;
	}
	//
	public  String getcookieValue()
	{
		cookieValue=cookieFile.getString("cookieValue", "");
		return cookieValue;
	}
	public static String getseesionName()
	{
		return sessionName;
	}
	//
	public  Boolean getIfRememberMe()
	{
		rememberme=cookieFile.getBoolean("rememberme", false);
		return rememberme;
			
	}
	
	public  void  setcookieValue(String cookieValue0)
	{
		if(cookieValue0!="")
		{
		editor.putString("cookieValue", cookieValue0);
		editor.commit();
		}
		cookieValue=cookieValue0;
	}
	//
	public  void  setcookieName(String cookieName)
	{
		if(cookieName!="")
		{
		editor.putString("cookieValue", cookieValue);
		editor.commit();
		}
		UserInfo.cookieName=cookieName;
	}
	public void setseesionName(String sessionName)
	{
		if(sessionName!="")
		{
		editor.putString("sessionName", sessionName);
		editor.commit();
		}
		UserInfo.sessionName=sessionName;
	}
	//写入用户是否记住密码
	public void setIfRememberMe(Boolean rememberme)
	{
		if(editor==null) System.out.println("null");
		 editor.putBoolean("rememberme", rememberme);
		 editor.commit();
			
	}
	
	
}
