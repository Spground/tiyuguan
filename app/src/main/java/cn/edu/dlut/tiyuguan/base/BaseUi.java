package cn.edu.dlut.tiyuguan.base;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import cn.edu.dlut.tiyuguan.widget.CustomProgressDialog;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseUi extends SwipeBackActivity {

    protected BaseHandler handler;
    protected BaseTaskPool taskPool;
    protected BaseSqlite sqlite;

    protected boolean shownLoadBar = false;
    protected boolean isPaused = true;

    private Dialog progressDlg;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDlg = CustomProgressDialog.createDialog(this, "正在处理请稍后...", true);
        mSwipeBackLayout = this.getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    /**显示的ProgressDlg的内容**/
    protected String getProgressDlgTxt(){
        return "正在处理请稍后...";
    }

    /**返回是否可以返回键取消**/
    protected boolean getProgressDlgCanceable(){
        return false;
    }
    protected void initActionBar(String title){
        //初始化actionbar
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(" ");

        View actionbarView = LayoutInflater.from(this).inflate(R.layout.tv, null);
        ((TextView)actionbarView).setText(title);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
        actionBar.setCustomView(actionbarView,layout);
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
        hideProgressDlg();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /********************各种弹出消息 BEGIN************************/
    public void toast(String msg){
        ToastUtil.showToast(this, msg);
    }
    public void toastError(String msg){
        ToastUtil.showErrorToast(this, msg);
    }
    public void toastInfo(String msg){
        ToastUtil.showInfoToast(this, msg);
    }
    public void toastWarning(String msg){
        ToastUtil.showWarningToast(this,msg);
    }

    /**显示加载进度dlg**/
    public void showProgressDlg(){
        if(progressDlg == null || progressDlg.isShowing())
            return ;
        progressDlg.show();
        return ;
    }

    /**隐藏加载进度dlg**/
    public void hideProgressDlg(){
        if(progressDlg != null && progressDlg.isShowing()){
            progressDlg.dismiss();
        }
        return;
    }
    /********************各种弹出消息 END***************************/

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

    public void registerEventBus(){

    }

    public void unregisterEventBus(){

    }

    public SwipeBackLayout getmSwipeBackLayout(){
        return mSwipeBackLayout;
    }
}
