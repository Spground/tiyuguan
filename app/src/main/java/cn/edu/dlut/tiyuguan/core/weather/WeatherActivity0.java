package cn.edu.dlut.tiyuguan.core.weather;

import cn.edu.dlut.tiyuguan.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.TextView;

public class WeatherActivity0 extends Activity{
       @SuppressLint("SetJavaScriptEnabled") @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_webview);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setTitle(" ");
	    View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
	    ((TextView)actionbarLayout).setText("浏览器");
	     actionBar.setDisplayShowCustomEnabled(true);
	     ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
	     //actionbar结束
	     actionBar.setCustomView(actionbarLayout,layout);
	     WebView mWebView=(WebView) findViewById(R.id.webview);
	     mWebView.loadUrl("http://weather.news.qq.com/");
	     //
	     int screenDensity = this.getResources().getDisplayMetrics().densityDpi;  
	             switch (screenDensity){   
	              case DisplayMetrics.DENSITY_LOW :  
	                  mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);  
	                  break;  
	              case DisplayMetrics.DENSITY_MEDIUM:  
	                 mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);  
	                 break;  
	             case DisplayMetrics.DENSITY_HIGH:  
	                 mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);  
	                 break ;
	             case DisplayMetrics.DENSITY_XHIGH:  
	                 mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR); 
	                 break ;  
	             case DisplayMetrics.DENSITY_TV:  
	                 mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);  
	                break ; 
	             }
	     //
	             mWebView.getSettings().setSupportZoom(true);
	            // mWebView.setInitialScale(25);
	             mWebView.getSettings().setUseWideViewPort(true);
	     //webview.getSettings().setLoadWithOverviewMode(true);
	             mWebView.getSettings().setJavaScriptEnabled(true); 
    }
       @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	   switch (item.getItemId()) {
   		case android.R.id.home:
   			//Toast.makeText(this, "返回",Toast.LENGTH_LONG).show();
   			finish();
   			break;

   		default:
   			break;
   		}
    	return super.onOptionsItemSelected(item);
    }
}
