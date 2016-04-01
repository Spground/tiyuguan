package cn.edu.dlut.tiyuguan.task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.event.DeleteOrderEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2016/4/1.
 */
public class DeleteOrderTask extends BaseTask {
    private String url = NameConstant.api.deleteReserveRecord;
    private String recordId;

    public DeleteOrderTask(String recordId) {
        this.recordId = recordId;
    }

    @Override
    public void start() {
        this.url += "?recordId=" + this.recordId;
        AppClient appClient = AppClient.getInstance();
        try {
            String response = appClient.get(this.url);
            onCompleted(response);
        } catch (IOException e) {
            onNetworkError(e);
        }
    }

    @Override
    public void onCompleted(String response) {
        super.onCompleted(response);
        try {
            JSONObject jObj = new JSONObject(response);
            String jValue = jObj.getString("message");
            jObj = new JSONObject(jValue);
            jValue = jObj.getString("result");
            DeleteOrderEvent deleteOrderEvent = new DeleteOrderEvent();
            deleteOrderEvent.setRecord(BaseAuth.getUser().getRecordMap().get(this.recordId));
            deleteOrderEvent.setResult(jValue);
            EventBus.getDefault().post(deleteOrderEvent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
        NetworkErrorEvent errorEvent = new NetworkErrorEvent();
        EventBus.getDefault().post(errorEvent);
    }
}
