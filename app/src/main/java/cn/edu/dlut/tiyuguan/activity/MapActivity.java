package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.util.LocateUtil;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MapActivity extends Activity {
	//成员变量
	MapView mMapView = null;
	BaiduMap mBaiduMap=null;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	SDKInitializer.initialize(getApplicationContext());//在调用任何百度地图组件的时候都得初始化
	setContentView(R.layout.activity_map);
	//设置actionbar
	ActionBar actionBar = this.getActionBar();    
	actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
	actionBar.setDisplayShowHomeEnabled(false);
	actionBar.setTitle(" ");
	View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.action_bar_title_view, null);
	((TextView)actionbarLayout).setText("附近的人");
	actionBar.setDisplayShowCustomEnabled(true);
	ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
	actionBar.setCustomView(actionbarLayout,layout);
	//actionbar结束

	//百度地图开始
	mMapView=(MapView)findViewById(R.id.bmapView);
	mBaiduMap=mMapView.getMap();
	mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    mBaiduMap.setMyLocationEnabled(true);

    LocateUtil localUtil=new LocateUtil(this, mBaiduMap);//定位
    localUtil.showMyPositionOnMap();
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	mMapView.onResume();
}
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	mMapView.onDestroy();
}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	mMapView.onPause();
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case android.R.id.home:
		finish();
		break;

	default:
		break;
	}
	return super.onOptionsItemSelected(item);
}

}
