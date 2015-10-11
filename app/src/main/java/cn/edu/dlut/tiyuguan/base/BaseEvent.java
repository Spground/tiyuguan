package cn.edu.dlut.tiyuguan.base;

/**
 * Created by asus on 2015/10/7.
 */
public class BaseEvent {
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    private String eventCode;//事件代码
    private String eventDesc;//事件描述
    private  String data;//事件包含的数据

    public BaseEvent(){
        this("");
    }

    public BaseEvent(String eventCode){
        this(eventCode,"");
    }

    public BaseEvent(String eventCode,String eventDesc){
        this(eventCode,eventDesc,"");
    }

    public BaseEvent(String eventCode,String eventDesc,String data){
        this.eventCode = eventCode;
        this.eventDesc = eventDesc;
        this.data = data;
    }

}
