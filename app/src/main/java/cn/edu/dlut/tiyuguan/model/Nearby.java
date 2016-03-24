package cn.edu.dlut.tiyuguan.model;

import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by WuJie on 2016/3/24.
 */
public class Nearby extends BaseModel {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getPt() {
        return pt;
    }

    public void setPt(LatLng pt) {
        this.pt = pt;
    }

    private String title;
    private LatLng pt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public InfoWindow getInfoWindow() {
        return infoWindow;
    }

    public void setInfoWindow(InfoWindow infoWindow) {
        this.infoWindow = infoWindow;
    }

    private InfoWindow infoWindow;

}
