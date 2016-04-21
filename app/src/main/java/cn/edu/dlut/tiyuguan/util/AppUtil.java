package cn.edu.dlut.tiyuguan.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.fragment.RecordPageFragment;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Record;

/**
 * Created by asus on 2015/10/6.
 */
public class AppUtil {
    private static Map<String,Integer> drawableResourceMap;
    private static int[] drawableIds = {R.drawable.bask,R.drawable.yumao,
            R.drawable.ping,R.drawable.swim,R.drawable.taiqiu};
    private static  String[] venuesNames = new String[]{"篮球馆","羽毛球馆","乒乓球馆","游泳馆","台球馆"};
    static {
        drawableResourceMap = new HashMap<>();
        for(int i = 0 ; i < 5 ; i++) {
            drawableResourceMap.put(venuesNames[i],drawableIds[i]);
        }
    }
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Service service) {
        return service.getSharedPreferences(NameConstant.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    /**SHA加密方法**/
    public static String getSHA256(String strSrc) {
        MessageDigest md;
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
    public static boolean isConnected(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        //检查网络是否可用
        if(network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**打开网络设置界面**/
    public static void setNetworkMethod(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                //用户选择设置网络，跳转到系统设置网络的界面
                if(android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else {
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
    public static BaseMessage getMessage(String jsonStr, String modelName) throws Exception {
        BaseMessage message = new BaseMessage();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            message.setCode(jsonObject.getInt("code"));
            message.setMessage(jsonObject.getString("message"));
            message.setDataStr(jsonObject.getString("data"), modelName);
        } catch (JSONException e) {
            System.err.print(e.toString());
            AppUtil.debugV(e.toString());
            throw new Exception("Json format error");
        } catch (Exception e) {
            AppUtil.debugV(e.toString());
            e.printStackTrace();
        }
        return message;
    }

    /**得到当前时间戳**/
    public static String getCurrentSystemTimeStamp() {
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

    /**
     * 时间戳转时间字符
     * @param timeStampInSeconds
     * @param format
     * @return
     */
    public static String timeStamp2timeStr(long timeStampInSeconds, String format) {
        if(format == null)
            format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(timeStampInSeconds * 1000);
        String result = dateFormat.format(date);
        return result;
    }

    /**Debug Util**/
    public static void debugV(String tag,String content) {
        if(NameConstant.debug.Debug_Mode)
            Log.v(tag,content);
    }
    /**Debug Util**/
    public static void debugV(String content) {
        if(NameConstant.debug.Debug_Mode)
            Log.v("===TAG===",content);
    }

    /**得到指定前几个月的格式的毫秒数的时间格式**/
    public static String getBeforeTime(int months,SimpleDateFormat format,Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH,-months);
        return format.format(calendar.getTime());
    }

    /**计算两个日期之间的天数差值**/
    public static int getDaysBetweenDate(Date lgdate,Date smdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        lgdate = sdf.parse(sdf.format(lgdate));
        Calendar cal = Calendar.getInstance();

        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();

        cal.setTime(lgdate);
        long time2 = cal.getTimeInMillis();

        long between_days = (time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**返回场馆名对应的图标资源id**/
    public static int getDrawableResId(String venuesName) {
        int v_id = -1;
        if (venuesName == null)
            return v_id;
        try {
            v_id = drawableResourceMap.get(venuesName);
        } catch (Exception e) {
            e.printStackTrace();
            return v_id;
        }
        return v_id;
    }

    /**
     * 将Map转为List
     * @param list
     * @param map
     * @param isClear
     * @return
     */
    public static ArrayList<Record> map2List(ArrayList<Record> list, Map<String,Record> map, boolean isClear, RecordPageFragment.RECORD_TYPE record_type) {
        if(map == null || list == null)
            return null;
        if(isClear)
            list.clear();
        for(String key : map.keySet()){
            list.add(map.get(key));
        }
        return list;
    }
}
