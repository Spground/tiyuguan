package cn.edu.dlut.tiyuguan.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.global.NameConstant;

/**
 * Created by WuJie on 2016/3/28.
 */
public class DBUtil {
    //比较耗时，建议在非UI线程使用
    public static DaoSession getDaoSession(Context ctx) {
        SQLiteDatabase db = getDatabase(NameConstant.dbName, ctx.getApplicationContext());
        DaoMaster master = new DaoMaster(db);
        return master.newSession();
    }

    public static DaoSession getDaoSession(SQLiteDatabase db) {
        DaoMaster master = new DaoMaster(db);
        return master.newSession();
    }

    //比较耗时，建议在非UI线程使用
    public static SQLiteDatabase getDatabase(String dbName, Context ctx) {
        if(dbName == null)
            throw new RuntimeException("dbName should not be null");
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(ctx.getApplicationContext(),
                dbName, null);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
}
