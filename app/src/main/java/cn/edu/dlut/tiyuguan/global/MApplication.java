package cn.edu.dlut.tiyuguan.global;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baidu.mapapi.SDKInitializer;

import cn.edu.dlut.tiyuguan.bean.OrderBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.OrderBeanDao;
import cn.edu.dlut.tiyuguan.util.DBUtil;

public class MApplication extends Application{

	public static HttpClient httpClient;//用户打开程序用到的全局httpclient实例
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
		rememberme = cookieFile.getBoolean("rememberme", false);
		httpClient = new DefaultHttpClient();
		SDKInitializer.initialize(getApplicationContext());//在调用任何百度地图组件的时候都得初始化
		//initDB();
	}

	private void initDB() {
		DaoSession session = DBUtil.getDaoSession(this);
		OrderBeanDao orderBeanDao = session.getOrderBeanDao();
		OrderBean orderBean = new OrderBean("123456", System.currentTimeMillis() / 1000,
				System.currentTimeMillis() / 1000 + 5000, "201203126");
		orderBeanDao.insert(orderBean);
	}


}
