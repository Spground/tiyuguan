package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Venues extends BaseModel{
    private String id;
    private String  v_name;

    private String open_time;
    private String close_time;

    private String num;
    private String charge;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

}
