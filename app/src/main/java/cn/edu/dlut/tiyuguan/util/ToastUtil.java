package cn.edu.dlut.tiyuguan.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by asus on 2015/10/6.
 */

/**
 * TODO:自定义Toast的样式
 */
public class ToastUtil {

    public static void showErrorToast(Context ctx,String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showWarningToast(Context ctx,String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showInfoToast(Context ctx,String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context ctx,String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

}
