package cn.edu.dlut.tiyuguan.service;

import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseService;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.util.AppUtil;

/**
 * Created by asus on 2015/10/6.
 */
public class AutoLoginService extends BaseService {
    public static String NAME = AutoLoginService.class.getName();

    //Thread Pool Executors
    private ExecutorService execService;
    private boolean runLoop = true;
    private boolean isFirst = true;

    private BaseMessage message;

    @Override
    public void onCreate() {
        super.onCreate();
        execService = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getAction() == null){
            runLoop = false;
            stopSelf();
        }

        if(intent.getAction().equals(NAME + BaseService.ACTION_START)){
            isFirst = true;
            startService();
        }
        if(intent.getAction().equals(NAME + BaseService.ACTION_STOP)){
            runLoop = false;
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);

    }


    //begin login operation in background
    private void startService(){
        execService.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = AppUtil.getSharedPreferences(AutoLoginService.this);
                HashMap<String,String> urlParams = new HashMap<>();

                urlParams.put("username",sp.getString("username",null));
                urlParams.put("password",sp.getString("password",null));

                while(runLoop){
                    //if user login
                    if(BaseAuth.isLogin()){
                        stopSelf();
                        return;
                    }
                    else{
                    //begin  remote login operation & query some important info about venus
                        try {
                            doTaskAsyn(NameConstant.task.login,NameConstant.api.login,urlParams);
                            Thread.sleep(30 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }



            }
        });
    }

    @Override
    public void onTaskCompleted(int taskId, String data) {

    }

    @Override
    public void onNetworkError() {
        if(isFirst){
            isFirst = false;
        }
        stopSelf();
    }
}
