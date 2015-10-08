package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Record extends BaseModel {
    private String id;//订单号
    private String venues_id;
    private String user_id;
    private String location;

    private String order_start_time;
    private String order_end_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenues_id() {
        return venues_id;
    }

    public void setVenues_id(String venues_id) {
        this.venues_id = venues_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrder_start_time() {
        return order_start_time;
    }

    public void setOrder_start_time(String order_start_time) {
        this.order_start_time = order_start_time;
    }

    public String getOrder_end_time() {
        return order_end_time;
    }

    public void setOrder_end_time(String order_end_time) {
        this.order_end_time = order_end_time;
    }



}
