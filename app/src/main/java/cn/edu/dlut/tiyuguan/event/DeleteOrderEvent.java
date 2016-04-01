package cn.edu.dlut.tiyuguan.event;

import cn.edu.dlut.tiyuguan.model.Record;

/**
 * Created by asus on 2016/4/1.
 */
public class DeleteOrderEvent {
    private String result;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    private Record record;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
