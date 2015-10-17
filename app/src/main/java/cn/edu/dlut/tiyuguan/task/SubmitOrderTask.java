package cn.edu.dlut.tiyuguan.task;

import java.io.IOException;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.OrderSuccessEvent;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/15.
 */
public class SubmitOrderTask extends BaseTask {
    private String url;
    public SubmitOrderTask(String url){
        this.url = url;
    }
    @Override
    public void start() {
        AppClient appClient = AppClient.getInstance();
        try {
            String response = appClient.get(this.url);
            onCompleted(response);
        } catch (IOException e) {
            e.printStackTrace();
            onNetworkError(e);
        }
    }

    @Override
    public void onCompleted(String response) {
        super.onCompleted(response);
        try {
            AppUtil.debugV("====TAG====","提交订单的response" + response);
            BaseMessage message = AppUtil.getMessage(response);

            if(message.isSuccessful()){
                Record record = (Record)message.getData("Record");
                if(record != null){
                    OrderSuccessEvent orderSuccessEvent = new OrderSuccessEvent();
                    orderSuccessEvent.setOrderId(record.getRecordId());
                    EventBus.getDefault().post(orderSuccessEvent);
                }
                else
                    throw new Exception("订单创建失败！");
            }
            else
                throw new Exception("订单创建失败！");

        } catch (IOException e) {
            e.printStackTrace();
            onNetworkError(e);
        } catch (Exception e) {
            e.printStackTrace();
            onExceptionError(e);
        }
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
        EventBus.getDefault().post(new NetworkErrorEvent());
    }

    @Override
    public void onExceptionError(Exception e) {
        super.onExceptionError(e);
        EventBus.getDefault().post(new ExceptionErrorEvent());
    }
}
