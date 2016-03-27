package cn.edu.dlut.tiyuguan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.widget.CustomProgressDialog;

/**
 * Email:958340585@qq.com
 * Author:wujie
 * Last Modified:
 */
public class TiyuguanGuideActivity extends BaseUi {

    private WebView mWebView ;
    private Dialog progressDialog;

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
        initActionBar("体育馆指南");

        progressDialog = CustomProgressDialog.createDialog(this,"正在加载，请稍后...",true);
        progressDialog.show();

        mWebView = (WebView)findViewById(R.id.id_webview_tiyuguanguide);
        if(mWebView != null){
            mWebView.loadUrl("file:///android_asset/html/tiyuguanguide.html");
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    TiyuguanGuideActivity.this.progressDialog.dismiss();
                }
            });
            mWebView.setWebChromeClient(new WebChromeClient());
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tiyuguan_guide, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }
}
