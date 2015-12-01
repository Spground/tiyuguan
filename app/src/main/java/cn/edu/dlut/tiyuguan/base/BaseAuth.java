package cn.edu.dlut.tiyuguan.base;

import cn.edu.dlut.tiyuguan.model.User;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseAuth {
    static public boolean isLogin () {
        User user = User.getInstance();
        return user.isLogin() == true;
    }

    static public void setLogin (Boolean status) {
        User user = User.getInstance();
        user.setLogin(status);
    }

    static public void setUser (User mc) {
        User user = User.getInstance();
        user.setUserName(mc.getUserName());
        user.setUserEmail(mc.getUserEmail());
        user.setFace(mc.getFace());
        user.setDepartment(mc.getDepartment());
        user.setMajor(mc.getMajor());

        /**实际的数据库字段**/
        user.setUserId(mc.getUserId());
        user.setUserName(mc.getUserName());
        user.setCreditWorthiness(mc.getCreditWorthiness());
        user.setUserRole(mc.getUserRole());

    }

    static public User getUser () {
        return User.getInstance();
    }
}
