package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.global.Img;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenueInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenueInfo;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.util.AppUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends Activity{

	//网络是否正常
	public Boolean internetIsWork = false;
	public Handler parentHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0x1234)
			{
				//交给全局类去处理这个字符串
				String str = msg.obj.toString();
				Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				if(str.equals("false"))
				{
					return;
				}
				else
				{
				   VenueInfo.setVenueInfo(str);
				   internetIsWork = true;
				   Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				}
			}
			if(msg.what == 0X1235)//判断用户的登陆情况
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
			if(msg.what == 0X1236){
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
        init();

	}
/*初始化操作*/
    private void init(){
        final Intent intent = new Intent(this, MainActivity.class);
        //检查网络连接情况
        if(AppUtil.isConnected(this)) {
            //从服务器接受数据初始化场馆信息++++++++++++++++++++++++++++++++++++++++++++++++
            RefreshVenueInfo.doRefreshVenueInfo(UserInfo.httpClient, parentHandler);
            internetIsWork = true;
            //读取用户的设置，是否为记住密码
            if(UserInfo.rememberme)
            {
                //new Thread(new DoPost(UserInfo.httpClient, parentHandler, "http://192.168.0.106:8080/GymBook/login.action", 1)).start();
            }
        }
        else
        {
            AppUtil.setNetworkMethod(this);
        }
        //调用postDelayed方法延时，插入消息队列中执行线程跳转
        parentHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
