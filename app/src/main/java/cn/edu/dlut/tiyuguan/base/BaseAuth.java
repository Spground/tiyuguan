package cn.edu.dlut.tiyuguan.base;

import cn.edu.dlut.tiyuguan.model.User;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseAuth {
    static public boolean isLogin () {
        User user = User.getInstance();
        if (user.isLogin() == true) {
            return true;
        }
        return false;
    }

    static public void setLogin (Boolean status) {
        User user = User.getInstance();
        user.setLogin(status);
    }

    static public void setUser (User mc) {
        User user = User.getInstance();
        user.setUserName(mc.getUserName());
        user.setUserEmail(mc.getUserEmail());
        user.setCreditRating(mc.getCreditRating());
        user.setFace(mc.getFace());
        user.setDepartment(mc.getDepartment());
        user.setMajor(mc.getMajor());
        /**实际的字段**/
        user.setId(mc.getId());
        user.setName(mc.getName());
        user.setCreditworthiness(mc.getCreditworthiness());

    }

    static public User getUser () {
        return User.getInstance();
    }
}
