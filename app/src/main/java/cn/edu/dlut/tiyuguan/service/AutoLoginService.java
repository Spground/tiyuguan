package cn.edu.dlut.tiyuguan.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseService;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.LoginFailedEvent;
import cn.edu.dlut.tiyuguan.event.LoginSuccessEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.User;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/6.
 */
public class AutoLoginService extends BaseService {
    public static String NAME = AutoLoginService.class.getName();

    //Thread Pool Executors
    private ExecutorService execService;
    private boolean isFirst = true;

    @Override
    public void onCreate() {
        super.onCreate();
        execService = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getAction() == null){
            stopSelf();
        }

        if(intent.getAction().equals(NAME + BaseService.ACTION_START)){
            isFirst = true;
            startService();
        }
        if(intent.getAction().equals(NAME + BaseService.ACTION_STOP)){
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
                String nowtime = AppUtil.getTimeTag();

                urlParams.put("userid",sp.getString("username",null));
                urlParams.put("password",AppUtil.getSHA256(AppUtil.getSHA256(sp.getString("password",null)) + nowtime));
                urlParams.put("nowtime",nowtime);

                    //if user login
                    if(!AppUtil.isConnected(getApplicationContext())){
                        onNetworkError();
                        return;
                    }
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
        });
    }

    @Override
    public void onTaskCompleted(int taskId, String data) {
        /**login请求完成以后**/
        /**ToastUtil.showInfoToast(this,data);**/
        try {
            BaseMessage message = AppUtil.getMessage(data);
            if(message.isSuccessful()){
                User user = (User)message.getData("User");
                Log.v("TAG", "user对象\n:" + ((User) user).getName());
                //login success
                if((user).getName() != null){
                    BaseAuth.setUser(user);
                    BaseAuth.setLogin(true);
                    /**post loginsuccessevent  to who need this**/
                    LoginSuccessEvent event = new LoginSuccessEvent();
                    event.setEventDesc("login success!");
                    EventBus.getDefault().post(event);
                }
            }
            //login failed
            else{
                LoginFailedEvent event = new LoginFailedEvent();
                /**登录失败后，就设置记住密码为false**/
                SharedPreferences sp = AppUtil.getSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("rememberme", false);
                editor.commit();

                event.setEventDesc("login failed!");
                EventBus.getDefault().post(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionErrorEvent event = new ExceptionErrorEvent();
            event.setEventDesc("exception" + e);
            EventBus.getDefault().post(event);
        }
        /**stop when  task is completed**/
        stopSelf();
    }

    @Override
    public void onNetworkError() {
        if(isFirst){
            isFirst = false;
        }
        /**post message to who need this**/
        NetworkErrorEvent event = new NetworkErrorEvent(NameConstant.ErrorCode.Network_Error,"NetWork Error","null");
        EventBus.getDefault().post(event);
        stopSelf();
    }
}
