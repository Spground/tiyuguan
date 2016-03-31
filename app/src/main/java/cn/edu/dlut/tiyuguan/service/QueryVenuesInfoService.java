package cn.edu.dlut.tiyuguan.service;

import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseService;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/10.
 */
public class QueryVenuesInfoService extends BaseService {
    //Thread Pool Executors
    private ExecutorService execService;
    public static String NAME = QueryVenuesInfoService.class.getName();

    private String queryUrl = "";
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
            queryUrl = intent.getStringExtra("queryUrl");
            startService();
        }
        if(intent.getAction().equals(NAME + BaseService.ACTION_STOP)){
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void startService(){
        execService.execute(new Runnable() {
            @Override
            public void run() {
                AppUtil.debugV("====TAG====", "QueryVenuesInfoService查询线程已经启动");
                AppClient client = AppClient.getInstance();
                //block until post() method  return;
                try {
                    String httpResult = client.get(queryUrl);
                    BaseMessage message = AppUtil.getMessage(httpResult, "Record");
                    ArrayList<Record> arrayList = (ArrayList<Record>) message.getDataList("Record");
                    /**没有查找到记录**/
                    LinkedHashMap<String,Record> map = new LinkedHashMap<>();
                    for(int i = 0;i < arrayList.size();i++){
                        Record record = arrayList.get(i);
                        map.put(record.getRecordId(),record);
                    }
                    if(BaseAuth.isLogin()){
                        AppUtil.debugV("====TAG====","QueryRecordService里面，User已经setRecordMap()成功");
                        BaseAuth.getUser().setRecordMap(map);
                    }
                    RefreshCompletedEvent refreshCompletedEvent = new RefreshCompletedEvent();
                    EventBus.getDefault().post(refreshCompletedEvent);
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                    AppUtil.debugV("====TAG====","QueryRecordService里面出现IO异常" + e);
//                    NetworkErrorEvent errorEvent = new NetworkErrorEvent();
//                    EventBus.getDefault().post(errorEvent);
//                    stopSelf();
                    onNetworkError(e);
                }
                catch (Exception e){
                    e.printStackTrace();
                    AppUtil.debugV("====TAG====","QueryRecordService里面出现异常" + e);
//                    ExceptionErrorEvent exceptionErrorEvent = new ExceptionErrorEvent();
//                    EventBus.getDefault().post(exceptionErrorEvent);
//                    stopSelf();
                    onExceptionError(e);
                }
            }
        });
    }
}
