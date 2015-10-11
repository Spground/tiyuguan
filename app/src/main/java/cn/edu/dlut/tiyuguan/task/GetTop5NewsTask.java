package cn.edu.dlut.tiyuguan.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.model.News;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;

/**
 * Created by asus on 2015/10/11.
 * NOTE:dot execute this task in main thread,which may blocks main thread!
 */
public class GetTop5NewsTask extends BaseTask {

    private  String url;
    public GetTop5NewsTask(String url){
        this.url = url;
    }
    @Override
    public void start() {
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
        AppUtil.debugV("====TAG====","GET top 5 news response:" + response);
        try {
            BaseMessage message = AppUtil.getMessage(response);
            if(message.isSuccessful()){
                ArrayList<News> newsList = (ArrayList<News>) message.getDataList("News");
                if(newsList.size() > 0){
                    HashMap<String,News> newsMap = new HashMap();
                    for(int i = 0;i < newsList.size();i++){
                        String newsTitle = newsList.get(i).getTitle();
                        newsMap.put(newsTitle,newsList.get(i));
                    }
                    //set newMap to Sport instance
                    Sport.getInstance().setNewsMap(newsMap);
                }
                else
                    return;
            }
            else{
                throw new Exception("request failed!");
            }
        } catch (Exception e) {
            onExceptionError(e);
        }
    }
}
