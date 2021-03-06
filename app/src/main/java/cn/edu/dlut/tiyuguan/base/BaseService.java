package cn.edu.dlut.tiyuguan.base;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseService extends Service {

    public static final int TASK_COMPLETE = 0;
    public static final int TASK_ERROR = 1;

    public static final String ACTION_START = ".ACTION_START";
    public static final String ACTION_STOP = ".ACTION_STOP";

    protected  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TASK_COMPLETE:
                    int taskId = msg.getData().getInt("task");
                    String data = msg.getData().getString("data");
                    onTaskCompleted(taskId, data);
                    break;
                case TASK_ERROR:
                    onNetworkError(new Exception("Network Error"));
                    break;
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage(int what,int taskId,String data){
        Bundle b = new Bundle();
        b.putInt("task", taskId);
        b.putString("data", data);
        Message m = new Message();
        m.what = what;
        m.setData(b);
        handler.sendMessage(m);
    }

    //TODO:
    protected void doTaskAsyn(final int taskId,final String taskUrl,final HashMap<String,String> taskArgs){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AppClient client = AppClient.getInstance();
                    //block until post() method  return;
                    String httpResult = client.post(taskUrl,taskArgs);
                    sendMessage(TASK_COMPLETE, taskId, httpResult);
                } catch (Exception e) {
                    sendMessage(TASK_ERROR, taskId, null);
                    Log.v("======TAG========","Network Error httpresult" + e);
                    e.printStackTrace();
                }

            }
        });
    }

    /****/
    protected void doTaskAsyn(final BaseTask task){
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
            @Override
            public void run() {
                task.start();
            }
        });
    }
    /*****************Handler 回调方法 BEGIN *********************/
    public void onTaskCompleted(int taskId,String response){

    }

    /**Network Error occur**/
    public void onNetworkError(Exception e){
        e.printStackTrace();
        AppUtil.debugV("====TAG====","Network Error" + e);
        NetworkErrorEvent errorEvent = new NetworkErrorEvent();
        EventBus.getDefault().post(errorEvent);
        stopSelf();
    }

    /**Exception Error occur**/
    public void onExceptionError(Exception e){
        AppUtil.debugV("====TAG====","Exception:" + e.toString());
        ExceptionErrorEvent exceptionErrorEvent = new ExceptionErrorEvent();
        EventBus.getDefault().post(exceptionErrorEvent);
        stopSelf();
    }
    /*****************Handler 回调方法 END*********************/

}
