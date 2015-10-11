package cn.edu.dlut.tiyuguan.base;



/**
 * Created by asus on 2015/10/6.
 */
public  abstract class BaseTask {
    private String url;
    public BaseTask(){

    }
    public abstract void start();
    public  void onNetworkError(Exception e){
        e.printStackTrace();
    }
    public void onExceptionError(Exception e){
        e.printStackTrace();
    }
    public  void onCompleted(String response){

    }
}
