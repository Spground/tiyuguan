package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Record extends BaseModel {

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
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
    private int venuesId;
    private String userId;

    private String venuesName;
    private int locationId;//位置
    private long startTime;
    private long endTime;

    public int getVenuesId() {
        return venuesId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setVenuesId(int venuesId) {
        this.venuesId = venuesId;
    }
}
