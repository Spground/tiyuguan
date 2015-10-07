package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/6.
 */
public class User extends BaseModel {
    private String userName;
    private String userEmail;
    private String creditRating;//信用等级

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditworthiness() {
        return creditworthiness;
    }

    public void setCreditworthiness(String creditworthiness) {
        this.creditworthiness = creditworthiness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String id;
    private String creditworthiness;

    private String face;
    private String department;//学部学院
    private String major;//专业

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

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
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
    private static User userInstance = new User();
    private User(){
    }
    public static User getInstance(){
        return userInstance;
    }
}
