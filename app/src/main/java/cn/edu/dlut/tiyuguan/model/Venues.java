package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/7.
 */
public class Venues extends BaseModel{
    public String getVenuesId() {
        return venuesId;
    }

    public void setVenuesId(String venuesId) {
        this.venuesId = venuesId;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
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

    public String getVenuesNum() {
        return venuesNum;
    }

    public void setVenuesNum(String venuesNum) {
        this.venuesNum = venuesNum;
    }

    public String getVenuesCharge() {
        return venuesCharge;
    }

    public void setVenuesCharge(String venuesCharge) {
        this.venuesCharge = venuesCharge;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    /**数据库字段**/
    private String venuesId;//场馆id
    private String  gymId;
    private String sportId;
    private String venuesName;
    private String openTime;//开放时间
    private String closeTime;//关闭时间

    private String venuesNum;//剩余数量
    private String venuesCharge;//收费

    private String isOpen;



}
