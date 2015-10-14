package cn.edu.dlut.tiyuguan.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.exception.RequestException;
import cn.edu.dlut.tiyuguan.model.Location;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/10/14.
 */
public class QueryLocationInfoTask extends BaseTask {
    private String url;
    private String venues_id;
    public QueryLocationInfoTask(String url,String venues_id){
        this.url = url;
        this.venues_id = venues_id;
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
        AppUtil.debugV("====TAG====", "QueryLocationInfo response:" + response);
        //置空
        Sport.getInstance().getVenuesHashMap().get(venues_id).setLocationMap(null);
        EventBus.getDefault().post(new RefreshCompletedEvent());

        try {
            BaseMessage message = AppUtil.getMessage(response);
            if(message.isSuccessful()){
                ArrayList<Location> listLocation =  (ArrayList<Location>)message.getDataList("Location");
                if(listLocation != null && listLocation.size() > 0){
                    HashMap<String,Location> map = new HashMap<>();
                    for(int i = 0; i < listLocation.size();i++){
                        map.put(listLocation.get(i).getLocation(),listLocation.get(i));
                    }
                    Sport sport = Sport.getInstance();
                    if(sport.getVenuesHashMap() != null){
                        if(sport.getVenuesHashMap().get(venues_id) != null){
                            //刷新之前，将对应场馆的LocationMap置空
                            sport.getVenuesHashMap().get(venues_id).setLocationMap(map);
                            AppUtil.debugV("====TAG====","setLocationMap()完成");
                            EventBus.getDefault().post(new RefreshCompletedEvent());
                        }
                    }
                }
            }
            else{
                RequestException e = new RequestException("request return failed!");
                e.printStackTrace();
                onExceptionError(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onExceptionError(e);
        }
    }

    @Override
    public void onExceptionError(Exception e) {
        super.onExceptionError(e);
        EventBus.getDefault().post(new ExceptionErrorEvent());
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
        EventBus.getDefault().post(new NetworkErrorEvent());
    }
}
