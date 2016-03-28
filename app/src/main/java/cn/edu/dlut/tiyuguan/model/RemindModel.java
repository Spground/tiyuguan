package cn.edu.dlut.tiyuguan.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.edu.dlut.tiyuguan.base.BaseModel;
import cn.edu.dlut.tiyuguan.bean.RemindBean;
import cn.edu.dlut.tiyuguan.dao.RemindBeanDao;
import cn.edu.dlut.tiyuguan.util.DBUtil;

/**
 * Created by WuJie on 2016/3/28.
 */
public class RemindModel extends BaseModel {
    private RemindBean remindBean;
    private RemindBeanDao remindBeanDao;
    public RemindModel(Context ctx) {
        remindBeanDao = DBUtil.getDaoSession(ctx.getApplicationContext()).getRemindBeanDao();
    }

    public List<RemindBean> loadAllReminds() {
        ArrayList<RemindBean> remindList = new ArrayList<>();
        remindList.addAll(remindBeanDao.loadAll());
        return remindList;
    }

    public long addRemind(RemindBean remindBean) {
        return remindBeanDao.insert(remindBean);
    }

    public void deleteRemind(long key) {
        remindBeanDao.deleteByKey(key);
    }

}
