package cn.edu.dlut.tiyuguan.util;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

import cn.edu.dlut.tiyuguan.global.NameConstant;

/**
 * Created by asus on 2015/10/6.
 */
public class AppUtil {

    public static SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Service service){
        return service.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }
}
