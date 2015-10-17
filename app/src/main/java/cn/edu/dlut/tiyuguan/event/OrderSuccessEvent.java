package cn.edu.dlut.tiyuguan.event;

import cn.edu.dlut.tiyuguan.base.BaseEvent;

/**
 * Created by asus on 2015/10/15.
 */
public class OrderSuccessEvent extends BaseEvent {
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String orderId;
    public OrderSuccessEvent(){
    }

}
