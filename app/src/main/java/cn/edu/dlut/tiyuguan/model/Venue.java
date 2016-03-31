package cn.edu.dlut.tiyuguan.model;

import java.util.HashMap;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Venue extends BaseModel {
    public int getVenuesId() {
        return venuesId;
    }

    public void setVenuesId(int venuesId) {
        this.venuesId = venuesId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getVenuesName() {
        return venuesName;
    }

    public void setVenuesName(String venuesName) {
        this.venuesName = venuesName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public float getVenuesCharge() {
        return venuesCharge;
    }

    public void setVenuesCharge(float venuesCharge) {
        this.venuesCharge = venuesCharge;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 数据库字段
     **/
    private int gymId;
    private int sportId;

    private int venuesId;//场馆id
    private String venuesName;
    private String openTime;//开放时间
    private String closeTime;//关闭时间
    private int locationNum;//
    private float venuesCharge;//收费
    private int isOpen;

    private int availableLocationNum;//可用的剩余数量
    public int getAvailableLocationNum() {
        return availableLocationNum;
    }

    public void setAvailableLocationNum(int availableLocationNum) {
        this.availableLocationNum = availableLocationNum;
    }

    public int getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(int locationNum) {
        this.locationNum = locationNum;
    }

    public HashMap<Integer, Location> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(HashMap<Integer, Location> locationMap) {
        this.locationMap = locationMap;
    }

    private HashMap<Integer, Location> locationMap;//场馆的位置集合

}
