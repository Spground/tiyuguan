package cn.edu.dlut.tiyuguan.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/21.
 */
public class QueryUserOrderRecordTask extends BaseTask {
    private String queryUrl = "";
    public QueryUserOrderRecordTask(String queryUrl){
        this.queryUrl = queryUrl;
    }
    @Override
    public void start() {
        AppUtil.debugV("====TAG====", "QueryUserOrderRecordTask查询任务已经启动");
        AppClient client = AppClient.getInstance();
        //block until post() method  return;
        try {
            String httpResult = client.get(queryUrl);
            onCompleted(httpResult);
        } catch (IOException e) {
            e.printStackTrace();
            AppUtil.debugV("====TAG====","QueryUserOrderRecordTask里面出现IO异常" + e);
            onNetworkError(e);
        }
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
        NetworkErrorEvent errorEvent = new NetworkErrorEvent();
        EventBus.getDefault().post(errorEvent);
    }

    @Override
    public void onExceptionError(Exception e) {
        super.onExceptionError(e);
        ExceptionErrorEvent exceptionErrorEvent = new ExceptionErrorEvent();
        EventBus.getDefault().post(exceptionErrorEvent);
    }

    @Override
    public void onCompleted(String response) {
        super.onCompleted(response);
        BaseMessage message = null;
        try {
            message = AppUtil.getMessage(response);
        } catch (Exception e) {
            e.printStackTrace();
            onExceptionError(e);
            return;
        }
        ArrayList<Record> arrayList = (ArrayList<Record>) message.getDataList("Record");
        /**没有查找到记录**/
        LinkedHashMap<String,Record> map = new LinkedHashMap<>();
        for(int i = 0;i < arrayList.size();i++){
            Record record = arrayList.get(i);
            map.put(record.getRecordId(),record);
        }
        if(BaseAuth.isLogin()){
            AppUtil.debugV("====TAG====","QueryUserOrderRecordTask里面，User已经setRecordMap()成功");
            BaseAuth.getUser().setRecordMap(map);
        }
        RefreshCompletedEvent refreshCompletedEvent = new RefreshCompletedEvent();
        EventBus.getDefault().post(refreshCompletedEvent);
    }
}
