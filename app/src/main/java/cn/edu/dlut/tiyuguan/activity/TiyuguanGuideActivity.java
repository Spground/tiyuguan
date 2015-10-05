package cn.edu.dlut.tiyuguan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;

/**
 * Email:958340585@qq.com
 * Author:wujie
 * Last Modified:
 */
public class TiyuguanGuideActivity extends Activity {

    private WebView mWebView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiyuguan_guide);
        init();
    }

    /**
     *初始化操作
     */
    private void init(){
        //初始化actionbar
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(" ");
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
        ((TextView)actionbarLayout).setText("体育馆指南");
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
        actionBar.setCustomView(actionbarLayout,layout);
        //actionbar初始化结束

        mWebView = (WebView)findViewById(R.id.id_webview_tiyuguanguide);
        if(mWebView != null){
            mWebView.loadUrl("file:///android_asset/html/tiyuguanguide.html");
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebChromeClient(new WebChromeClient());
            //mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tiyuguan_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
