package cn.edu.dlut.tiyuguan.core.location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.devspark.appmsg.AppMsg;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.widget.CustomProgressDialog;

public class LocateUtil {

    private  Boolean isFirstLoc = true;
	private  LocationClient locationClient;
	private  BaiduMap baiduMap;
	private Dialog progressDialog;
	private Context context;

	public LocateUtil(Context context,BaiduMap baiduMap){

		SDKInitializer.initialize(context.getApplicationContext());
		this.context = context;
		this.locationClient = new LocationClient(context);
		this.baiduMap = baiduMap;
	    progressDialog = CustomProgressDialog.createDialog(context,"正在定位，请稍等。。。",true);

	}
    /*下面是展示我的位置在地图上*/
	public void showMyPositionOnMap(){

        progressDialog.show();//显示加载对话框
		this.locationClient.registerLocationListener(new MyLocationListener());
		locationClient.start();

		if(locationClient != null&&locationClient.isStarted())
			  locationClient.requestLocation();
		else if(!locationClient.isStarted()){
			System.out.println("locationClient未启动");
		}
		else{
			System.out.println("locationClient为空");
		}

		LocationClientOption option = new LocationClientOption();		
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向	
		option.setOpenGps(true);

		this.locationClient.setLocOption(option);
		this.locationClient.setForBaiduMap(true);
		
	}
    /*在地图上显示我的位置*/
	private void show(BDLocation location){

        double latitude,longitude;

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		LatLng mylatLng = new LatLng(latitude, longitude); //经纬度

		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.location);  
		OverlayOptions option = new MarkerOptions()  
		    .position(mylatLng)  
		    .icon(bitmap)
		    .zIndex(9)
		    .draggable(true);

		MyLocationData locData = new MyLocationData.Builder()
		.accuracy(location.getRadius())
		.direction(location.getDirection()).latitude(latitude)
		.longitude(longitude).build();
		baiduMap.setMyLocationData(locData); 
		if (isFirstLoc) {  			
            isFirstLoc = false;  
    		 baiduMap.addOverlay(option);
            LatLng ll = new LatLng(latitude,  
                   longitude);            
            MapStatusUpdate m=MapStatusUpdateFactory.newMapStatus( new MapStatus.Builder().zoom(18).target(ll).build());
            baiduMap.animateMapStatus(m);           
        }  
		locationClient.stop();
		System.out.println("stop----mLocationClient.isStarted:"+locationClient.isStarted());
	}

	//自定以监听器
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
	            return ;
		StringBuffer sb = new StringBuffer(256);
		String toastInfo = "";
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
		} 
        //显示marker
		if(location.getAddrStr()!=null){
			toastInfo=location.getAddrStr();
		}
		else{
			toastInfo="定位失败，请检查你的网络连接或者重新尝试！";
		}

		show(location);
		progressDialog.dismiss();
		AppMsg.makeText((Activity) context, toastInfo, AppMsg.STYLE_INFO).show();
		System.out.println(sb.toString());

		}
		
	}
}
