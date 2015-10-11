package cn.edu.dlut.tiyuguan.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.global.NameConstant;

/**
 * Created by asus on 2015/10/6.
 */
public class AppUtil {
    /**得到SharedPreferences的名字**/
    public static SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    /**得到SharedPreferences的名字**/
    public static SharedPreferences getSharedPreferences(Service service){
        return service.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    /**SHA加密方法**/
    public static String getSHA256(String strSrc){
        MessageDigest md = null;
        StringBuilder sb = new StringBuilder();

        //将传进来的字符串变成字节数组
        byte[] bt = strSrc.getBytes();
        try {
            //新建一个算法加密实例
            md = MessageDigest.getInstance("SHA-256");
            //加密结果返回一个字节数组
            byte[] result = md.digest(bt);
            //将字节数组以16进制输出字符串
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return sb.toString();
    }
    /** 检查网络连接**/
    public static boolean isConnected(Context context){
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        //检查网络是否可用
        if(network != null){
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**打开网络设置界面**/
    public static void setNetworkMethod(final Context context){
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                //用户选择设置网络，跳转到系统设置网络的界面
                if(android.os.Build.VERSION.SDK_INT > 10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

    /**根据json原始字符串提取合成一个message对象**/
    public static BaseMessage getMessage(String jsonStr) throws Exception{
        BaseMessage message = new BaseMessage();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            message.setCode(jsonObject.getInt("code"));
            message.setMessage(jsonObject.getString("message"));
            message.setDataStr(jsonObject.getString("data"));
        } catch (JSONException e) {
            throw new Exception("Json format error");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**得到当前时间戳**/
    public static String getTimeTag(){
        //取系统当前时间
        String nowtime;
        Time time = new Time();
        time.setToNow();

        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int hour = time.hour; // 0-23
        int minute = time.minute;
        int second = time.second;

        //合成时间字符串字符串
        nowtime = Integer.toString(year)+Integer.toString(month)+Integer.toString(day)+Integer.toString(hour)+Integer.toString(minute)+Integer.toString(second);
        return nowtime;

    }

    /**Debug Util**/
    public static void debugV(String tag,String content){
        if(NameConstant.debug.Debug_Mode)
            Log.v(tag,content);
    }

    /**得到指定前几个月的格式的毫秒数的时间格式**/
    public static String getBeforeTime(int months,SimpleDateFormat format,Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(calendar.MONTH,-months);
        return format.format(calendar.getTime());
    }
}
