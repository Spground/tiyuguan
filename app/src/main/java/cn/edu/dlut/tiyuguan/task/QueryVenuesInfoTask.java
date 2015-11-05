package cn.edu.dlut.tiyuguan.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.exception.RequestException;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.model.Venues;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;

/**
 * Created by asus on 2015/10/14.
 */
public class QueryVenuesInfoTask extends BaseTask {
    private String url;

    public QueryVenuesInfoTask(String url){
        this.url = url;
    }
    @Override
    public void start() {
        AppClient appClient = AppClient.getInstance();

        try {
            AppUtil.debugV("====TAG====", "QueryVenuesInfo url:" + this.url);
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
        AppUtil.debugV("====TAG====", "QueryVenuesInfo response:" + response);

        try {
            BaseMessage message = AppUtil.getMessage(response);
            if(message.isSuccessful()){
                ArrayList<Venues> listVenues = (ArrayList<Venues>)message.getDataList("Venues");
                if(listVenues != null && listVenues.size() > 0){
                    HashMap<String,Venues> venuesMap = new HashMap();
                    for(int i = 0 ;i < listVenues.size() ; i++){
                        AppUtil.debugV("====Venues Info====","venues_id: " + listVenues.get(i).getVenuesId() + " locationNum: " + listVenues.get(i).getLocationNum());
                        venuesMap.put(listVenues.get(i).getVenuesId(),listVenues.get(i));
                    }
                    Sport sport = Sport.getInstance();
                    sport.setVenuesHashMap(venuesMap);
                }
            }
            else{
                throw new RequestException("request failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            onExceptionError(e);
        }

    }
}
