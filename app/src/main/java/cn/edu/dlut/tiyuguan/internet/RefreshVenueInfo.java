package cn.edu.dlut.tiyuguan.internet;

import org.apache.http.client.HttpClient;

import android.app.Application;
import android.os.Handler;

import cn.edu.dlut.tiyuguan.global.NameConstant;

//刷新场馆信息的类
public class RefreshVenueInfo {
	public static void doRefreshVenueInfo(HttpClient httpClient,Handler parentHandler) {
		 Runnable getVenueInfo = new Thread(new DoPost(httpClient, parentHandler, NameConstant.api.indexAction, 2));
	     new Thread(getVenueInfo).start();
	}
	
}
