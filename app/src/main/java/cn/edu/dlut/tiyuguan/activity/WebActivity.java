package cn.edu.dlut.tiyuguan.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.util.AppUtil;

public class WebActivity extends BaseUi {
    private String url, title;
    public static final String URL_EXTRAL = "URL", URL_TITLE = "TITLE";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        this.setSwipeBackEnable(false);
        this.url = getIntent().getStringExtra(URL_EXTRAL);
        AppUtil.debugV("===TAG===", "url is " + url);
        this.title = getIntent().getStringExtra(URL_TITLE);
        if(this.title.length() >= 10) {
            this.title = this.title.substring(0, 3)
                    + "..."
                    + this.title.substring(this.title.length() - 4, this.title.length() - 1);
        }
        initActionBar(this.title == null ? "Web" : this.title);
        init();
    }

    private void init() {
        this.webView = (WebView) findViewById(R.id.webView);
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDlg();
            }
        });
        showProgressDlg();
        this.webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        this.webView.destroy();
        super.onDestroy();
    }
}
