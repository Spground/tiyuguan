package cn.edu.dlut.tiyuguan.internet;

import org.apache.http.client.HttpClient;

import android.os.Handler;
//刷新场馆信息的类
public class RefreshVenusInfo {

	
	public static void doRefreshVenusInfo(HttpClient httpClient,Handler parentHandler)
	{		
	    	
		Runnable getVenuseInfo=new Thread(new DoPost(httpClient, parentHandler, "http://dutgymbook.sinaapp.com/indexinfo.action", 2));       
	     new Thread(getVenuseInfo).start();
	}
	
}
