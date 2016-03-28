package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Record extends BaseModel {


    public String getVenuesId() {
        return venuesId;
    }

    public void setVenuesId(String venuesId) {
        this.venuesId = venuesId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVenuesName() {
        return venuesName;
    }

    public void setVenuesName(String venuesName) {
        this.venuesName = venuesName;
    }

    /**
     * 数据库字段
     **/
    private String recordId;
    private String venuesId;
    private String userId;

    private String venuesName;
    private String location;//位置
    private String startTime;
    private String endTime;

}
