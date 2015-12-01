package cn.edu.dlut.tiyuguan.util;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by asus on 2015/10/6.
 */
public class AppClient extends OkHttpClient{
    private static AppClient instance = new AppClient();

    private long timeout = 3 * 1000;

    /**single instance**/
    private AppClient(){
        initClient();
    }
    /**init the httpclient**/
    private void initClient(){
        setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
    }
    /**get the single client instance**/
    public static AppClient getInstance(){
        if(instance == null)
            instance = new AppClient();
        return instance;
    }

    /**post request to remote server**/
    public String post(String url,Map<String,String> taskArgs) throws IOException {
        if(taskArgs == null) return null;
        AppUtil.debugV("======TAG========","post invoked");
        FormEncodingBuilder fEBuilder = new FormEncodingBuilder();
        for(String key : taskArgs.keySet() ){
            String value = taskArgs.get(key);
            fEBuilder.add(key,value);
        }
        RequestBody formBody = fEBuilder.build();
        //prepare the request
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        //get response
        Response response = newCall(request).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    /**get request to remote server**/
    public String get(String url) throws IOException{
        if(url == null) return null;
        AppUtil.debugV("======TAG========","get invoked");
        //prepare the request
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = newCall(request).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }
}
