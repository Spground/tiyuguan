package cn.edu.dlut.tiyuguan.activity;

import java.io.IOException;
import java.util.LinkedHashMap;

import cn.edu.dlut.tiyuguan.global.Img;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenusInfo;
import cn.edu.dlut.tiyuguan.internet.CheckInternet;
import cn.edu.dlut.tiyuguan.internet.ParseHtmlFromTYGSite;
import cn.edu.dlut.tiyuguan.internet.RefreshVenusInfo;

import cn.edu.dlut.tiyuguan.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends Activity{

	//网络是否正常
	public Boolean internetIsWork=false;
	public Handler parentHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==0x1234)
			{
				//交给全局类去处理这个字符串
				String str=msg.obj.toString();
				Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				if(str.equals("false"))
				{
					
				}
				else
				{
				   VenusInfo.setVenusInfo(str);
				   internetIsWork=true;
				   //Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				}
			}
			if(msg.what==0X1235)//判断用户的登陆情况
			{
				String str=msg.obj.toString();
				if(str.equals("false"))
				{
					Toast.makeText(getBaseContext(),"登录失败", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				}
			}
			if(msg.what==0X1236){
				String str=msg.obj.toString();
				if(str!=null)
					Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				
				for(String key:Img.img.keySet()){
					System.out.println(key+"----"+Img.img.get(key));
				}
			}
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.welcome_activity);
		//定时执行下一个活动
        final Intent it = new Intent(this, MainActivity.class); //你要转向的Activity
        //检查网络连接情况
        if(CheckInternet.isConn(this))
        {
        //从服务器接受数据初始化场馆信息++++++++++++++++++++++++++++++++++++++++++++++++
        
           RefreshVenusInfo.doRefreshVenusInfo(UserInfo.httpClient,parentHandler);
           //加载图片
          new Thread(){
  			 public void run() {
  				 ParseHtmlFromTYGSite parsehtml = null;
  					try {
  						parsehtml = new ParseHtmlFromTYGSite("http://tyg.dlut.edu.cn/", 6);
  					} catch (IOException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
  					//不为空
  					if(parsehtml!=null){
  						LinkedHashMap<String, String> newsLink=parsehtml.getNewsLinks();
  						Img.img=parsehtml.getNewsImgSrcByLink(newsLink);
  						Message msg=new Message();
  						msg.what=0x1236;
  						msg.obj="图片加载成功了!";
  						Looper.prepare();
  						parentHandler.sendMessage(msg);
  						Looper.loop();
  					}
  				 
  			 };
  		 }.start();
           internetIsWork=true;
         //读取用户的设置，是否为记住密码
       	if(UserInfo.rememberme)
       	{
       		//new Thread(new DoPost(UserInfo.httpClient, parentHandler, "http://192.168.0.106:8080/GymBook/login.action", 1)).start();
       	}
       	
        }
        else
        {
        	CheckInternet.setNetworkMethod(this);
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         //
        //调用postDelayed方法延时，插入消息队列中执行线程跳转
		parentHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(it);
				finish();
			}
		}, 4000);
        //定时器
        /*Timer timer = new Timer();
    	TimerTask task = new TimerTask() {
    		@Override
    		public void run() {
    			
    			if(true)
    			{
   		        startActivity(it); //执行
    			finish();//销毁此activity
    			}
    			else
    			{
    				Toast.makeText(getBaseContext(), "网络连接异常！", Toast.LENGTH_LONG).show();
    			}
    			
    		}
    	};
    	timer.schedule(task, 1000 * 5);//三秒后执行task*/
		
		
	}
}
