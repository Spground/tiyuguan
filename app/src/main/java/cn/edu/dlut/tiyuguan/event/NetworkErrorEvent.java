package cn.edu.dlut.tiyuguan.event;

import cn.edu.dlut.tiyuguan.base.BaseEvent;

/**
 * Created by asus on 2015/10/8.
 */
public class NetworkErrorEvent extends BaseEvent {
    public NetworkErrorEvent(){
        super();
    }
    public NetworkErrorEvent(String eventCode){
        super(eventCode);
    }
    public NetworkErrorEvent(String eventCode, String eventDes){
        super(eventCode,eventDes);
    }
    public NetworkErrorEvent(String eventCode, String eventDes, String data){
        super(eventCode,eventDes,data);
    }

}
