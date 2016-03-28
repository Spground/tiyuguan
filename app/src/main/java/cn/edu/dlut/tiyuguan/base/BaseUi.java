package cn.edu.dlut.tiyuguan.base;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.util.DBUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import cn.edu.dlut.tiyuguan.widget.CustomProgressDialog;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by asus on 2015/10/6.
 */
public class BaseUi extends SwipeBackActivity {

    protected MHandler handler;
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
    protected void initActionBar(String title) {
        //初始化actionbar
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(" ");
        View actionbarView = LayoutInflater.from(this).inflate(R.layout.action_bar_title_view, null);
        ((TextView)actionbarView).setText(title);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
        actionBar.setCustomView(actionbarView, layout);
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
    public void toastNormal(String msg) {
        ToastUtil.showNormalToast(this, msg);
    }

    public void toastError(String msg) {
        ToastUtil.showErrorToast(this, msg);
    }

    public void toastInfo(String msg) {
        ToastUtil.showInfoToast(this, msg);
    }

    public void toastSuccess(String msg) {
        ToastUtil.showSuccessToast(this, msg);
    }

    public void toastWarning(String msg) {
        ToastUtil.showWarningToast(this, msg);
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

    public interface AsyncLoadDataFromDBCallback {
        void asyncLoadDataFromDBCompleted(SQLiteDatabase database);
    }

    AsyncLoadDataFromDBCallback callback;

    public void asynLoadDataFromDB(AsyncLoadDataFromDBCallback callback, String dbName) {
        this.callback = callback;
        new Thread(new AsyncLoadDataFromDB(this, dbName, new MHandler(this))).start();
    }

    static class MHandler extends Handler {
        WeakReference<Activity> activityWeakReference;
        MHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SQLiteDatabase database = (SQLiteDatabase)msg.obj;
            if(activityWeakReference.get() != null) {
                if(((BaseUi)activityWeakReference.get()).callback != null) {
                    ((BaseUi)activityWeakReference.get()).callback
                            .asyncLoadDataFromDBCompleted(database);
                }
            }
        }
    }

    class AsyncLoadDataFromDB implements Runnable {
        Context ctx;
        String dbName;
        Handler handler;
        AsyncLoadDataFromDB(Activity ctx, String dbName, Handler handler) {
            this.ctx = ctx;
            this.dbName = dbName;
            this.handler = handler;
        }

        @Override
        public void run() {
            SQLiteDatabase database = DBUtil.getDatabase(this.dbName, this.ctx.getApplicationContext());
            Message message = Message.obtain();
            message.obj = database;
            this.handler.sendMessage(message);
        }
    }
}
