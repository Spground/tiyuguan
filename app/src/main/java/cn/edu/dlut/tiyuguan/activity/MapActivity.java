package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.model.Nearby;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import cn.edu.dlut.tiyuguan.widget.CustomProgressDialog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends Activity implements BDLocationListener,
        BaiduMap.OnMarkerClickListener, InfoWindow.OnInfoWindowClickListener, View.OnClickListener {
    //成员变量
    private MapView mMapView = null;
    private Boolean isFirstLoc = true;
    private LocationClient locationClient;
    private BaiduMap mBaiduMap;
    private Dialog progressDialog;
    private LatLng[] nearbys = new LatLng[]{
            new LatLng(38.8851390000, 121.5322730000),
            new LatLng(38.8904610000, 121.5324230000),
            new LatLng(38.8870910000, 121.5333580000)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();
        initBaiduMap();
        showMyPositionOnMap();
        showNearby(nearbys);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        this.mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        this.mMapView.onPause();
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

    private void initBaiduMap() {
        SDKInitializer.initialize(this.getApplicationContext());
        this.mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        this.mBaiduMap.setMyLocationEnabled(true);
        this.mBaiduMap.setOnMarkerClickListener(this);
    }

    private void init() {
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(" ");
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.action_bar_title_view, null);
        ((TextView) actionbarLayout).setText("附近的人");
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(Gravity.CENTER);
        actionBar.setCustomView(actionbarLayout, layout);
        progressDialog = CustomProgressDialog.createDialog(this, "正在定位，请稍等。。。", true);

        this.mMapView = (MapView) findViewById(R.id.bmapView);
        this.mBaiduMap = mMapView.getMap();
        this.locationClient = new LocationClient(this);

    }

    /**
     * 定位
     */
    public void showMyPositionOnMap() {
        progressDialog.show();
        this.locationClient.registerLocationListener(this);
        locationClient.start();
        if (locationClient != null && locationClient.isStarted())
            locationClient.requestLocation();
        else if (!locationClient.isStarted()) {
            System.out.println("locationClient未启动");
        } else {
            System.out.println("locationClient为空");
        }
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        option.setOpenGps(true);
        this.locationClient.setLocOption(option);
        this.locationClient.setForBaiduMap(true);
    }

    /**
     *
     * @param location
     */
    private void show(BDLocation location) {
        double latitude, longitude;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng mLatLng = new LatLng(latitude, longitude); //经纬度
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map_icon);
        OverlayOptions option = new MarkerOptions()
                .position(mLatLng)
                .icon(bitmap)
                .flat(true)
                .zIndex(9)
                .draggable(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection()).latitude(latitude)
                .longitude(longitude).build();
        this.mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            this.mBaiduMap.addOverlay(option);
            LatLng ll = new LatLng(latitude,
                    longitude);
            MapStatusUpdate m = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).target(ll).build());
            this.mBaiduMap.animateMapStatus(m);
        }
        locationClient.stop();
        System.out.println("stop----mLocationClient.isStarted:" + locationClient.isStarted());
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;
        StringBuffer sb = new StringBuffer(256);
        String toastInfo;
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
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
        }
        //显示marker
        if (location.getAddrStr() != null) {
            toastInfo = location.getAddrStr();
        } else {
            toastInfo = "定位失败，请检查你的网络连接或者重新尝试！";
        }

        show(location);
        progressDialog.dismiss();
        ToastUtil.showInfoToast(MapActivity.this, toastInfo);
        System.out.println(sb.toString());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng pt = marker.getPosition();
        Nearby model = findModel(pt);
        InfoWindow infoWindow = generateInfoWindow(pt, model != null ? model.getTitle() : "NULL");
        this.mBaiduMap.showInfoWindow(infoWindow);
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PostDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClick() {
        this.mBaiduMap.hideInfoWindow();
    }

    private InfoWindow generateInfoWindow(LatLng pt, String info) {
        View view = View.inflate(this, R.layout.marker, null);
        ((TextView)view).setText(info);
        view.setOnClickListener(this);
        InfoWindow infoWindow = new InfoWindow(view, pt, -150);
        return infoWindow;
    }


    /**
     * 根据经纬度返回model
     * @param latLng
     * @return
     */
    private Nearby findModel(LatLng latLng) {
        Map<Integer, Nearby> maps = getNearbyModels();
        for(Nearby nearby : maps.values()) {
            LatLng nPt = nearby.getPt();
            if(nPt.latitude == latLng.latitude && nPt.longitude == latLng.longitude)
                return nearby;
        }
        return null;
    }
    /**
     * 手动生成附近的人
     * @return
     */
    private Map<Integer, Nearby> getNearbyModels() {
        Nearby nearby1 = new Nearby();
        nearby1.setId(1);
        nearby1.setPt(nearbys[0]);
        nearby1.setTitle("今晚六点半，西山篮球场组队");
        nearby1.setInfoWindow(generateInfoWindow(nearby1.getPt(), nearby1.getTitle()));

        Nearby nearby2 = new Nearby();
        nearby2.setId(2);
        nearby2.setPt(nearbys[1]);
        nearby2.setTitle("求打球的妹子");
        nearby2.setInfoWindow(generateInfoWindow(nearby2.getPt(), nearby2.getTitle()));

        Nearby nearby3 = new Nearby();
        nearby3.setId(3);
        nearby3.setPt(nearbys[2]);
        nearby3.setTitle("足球组队中。。。");
        nearby3.setInfoWindow(generateInfoWindow(nearby3.getPt(), nearby3.getTitle()));

        HashMap<Integer, Nearby> map = new HashMap<>();
        map.put(1, nearby1);
        map.put(2, nearby2);
        map.put(3, nearby3);
        return map;
    }

    private void showNearby(LatLng[] latLngs) {
        for(LatLng mLatLng : latLngs) {
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.map_icon);
            OverlayOptions option = new MarkerOptions()
                    .position(mLatLng)
                    .flat(true)
                    .icon(bitmap)
                    .zIndex(9);
            this.mBaiduMap.addOverlay(option);
        }
    }
}

