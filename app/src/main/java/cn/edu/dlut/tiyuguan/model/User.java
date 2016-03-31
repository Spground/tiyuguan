package cn.edu.dlut.tiyuguan.model;

import java.util.LinkedHashMap;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/6.
 */
public class User extends BaseModel {
    /**
     * http
     **/
    private String userEmail;
    private String face;
    private String department;//学部学院
    private String major;//专业

    /**
     * 以下为数据库的字段名
     **/
    private String userName;
    private String userId;
    private int userRole;
    private int creditWorthiness;

    private LinkedHashMap<String, Record> recordMap;//订单记录 订单号---Record对象

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static User getUserInstance() {
        return userInstance;
    }

    public static void setUserInstance(User userInstance) {
        User.userInstance = userInstance;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getCreditWorthiness() {
        return creditWorthiness;
    }

    public void setCreditWorthiness(int creditWorthiness) {
        this.creditWorthiness = creditWorthiness;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }


    public synchronized LinkedHashMap<String, Record> getRecordMap() {
        return recordMap;
    }

    public void setRecordMap(LinkedHashMap<String, Record> recordMap) {
        this.recordMap = recordMap;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    /*****/
    //login
    //default is no login
    private boolean isLogin = false;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    //single instance pattern
    private volatile static User userInstance;

    private User() {
    }

    public static synchronized User getInstance() {
        if (userInstance == null)
            userInstance = new User();
        return userInstance;
    }
}
