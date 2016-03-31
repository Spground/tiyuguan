package cn.edu.dlut.tiyuguan.event;

import cn.edu.dlut.tiyuguan.base.BaseEvent;
import cn.edu.dlut.tiyuguan.model.Record;

/**
 * Created by asus on 2015/10/15.
 */
public class OrderSuccessEvent extends BaseEvent {
    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Record record;

}
