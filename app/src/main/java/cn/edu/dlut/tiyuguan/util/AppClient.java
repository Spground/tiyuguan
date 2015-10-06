package cn.edu.dlut.tiyuguan.util;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by asus on 2015/10/6.
 */
public class AppClient {
    private String url;
    private OkHttpClient client;

    private long timeout = 3 * 1000;

    public AppClient(String url){
        this.url = url;
        initClient();
    }
    /**init the httpclient**/
    private void initClient(){
        client = new OkHttpClient();
        client.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);

    }

    /**post request to remote server**/
    public String post(HashMap<String,String> taskArgs) throws IOException {
        if(taskArgs == null) return null;
        FormEncodingBuilder fEBuilder = new FormEncodingBuilder();
        for(String key : taskArgs.keySet() ){
            String value = taskArgs.get(key);
            fEBuilder.add(key,value);
        }
        RequestBody formBody = fEBuilder.build();
        //prepare the request
        Request request = new Request.Builder()
                .url(this.url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body().toString();
    }

    /**get from remote server**/
    public String get(String url){
        return null;
    }
}
