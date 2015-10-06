package cn.edu.dlut.tiyuguan.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.edu.dlut.tiyuguan.util.ToastUtil;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseUi extends FragmentActivity {

    protected BaseHandler handler;
    protected BaseTaskPool taskPool;
    protected BaseSqlite sqlite;

    protected boolean shownLoadBar = false;
    protected boolean isPaused = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /********************各种弹出消息 BEGIN************************/
    public void toast(String msg){
        ToastUtil.showToast(this,msg);
    }
    public void toastError(String msg){
        ToastUtil.showErrorToast(this,msg);
    }
    public void toastInfo(String msg){
        ToastUtil.showInfoToast(this,msg);
    }
    public void toastWarning(String msg){
        ToastUtil.showWarningToast(this,msg);
    }

    //TODO:
    public void showLoadingDlg(){

    }
    //TODO:
    public void hideLoadingDlg(){

    }
    /********************各种弹出消息 END***************************/

    /***********************UI跳转 BEGIN********************************/
    public void forward(Class<?> classobj){
        Intent intent = new Intent(this,classobj);
        intent.setClass(this,classobj);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }
    public void forward(Class<?> classObj, Bundle params) {
        Intent intent = new Intent();
        intent.setClass(this, classObj);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(params);
        this.startActivity(intent);
    }
    /************************UI跳转 END*******************************/

    /************************Handler回调的方法*************************/
    public void onTaskCompleted(){
        if(isPaused){
            return;
        }
    }

    public void onTaskCompleted(int taskid){
        if(isPaused){
            return;
        }
    }

    public void onNetworkErr(int taskid){
        if(isPaused){
            return;
        }
        toastError(taskid + "Network Eror");
    }

    public void onDbReadComplete(int taskId) {
        if(isPaused){
            return;
        }
    }


}
