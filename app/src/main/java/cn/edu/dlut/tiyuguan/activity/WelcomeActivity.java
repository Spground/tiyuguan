package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.base.BaseService;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenueInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenueInfo;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.service.AutoLoginService;
import cn.edu.dlut.tiyuguan.task.GetTop5NewsTask;
import cn.edu.dlut.tiyuguan.task.QueryVenuesInfoTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends Activity{

	//网络是否正常
	public Handler parentHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0x1234) {
				String str = msg.obj.toString();
				Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				if(str.equals("false"))
					return;
				else {
				   VenueInfo.setVenueInfo(str);
				   Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				}
			}
			if(msg.what == 0X1235){
				String str = msg.obj.toString();
				if(str.equals("false"))
					Toast.makeText(getBaseContext(),"登录失败", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/**fullscreen**/
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.welcome_activity);
        init();

	}
	/**初始化操作**/
    private void init(){
        final Intent intent = new Intent(this, MainActivity.class);
        /**internet connect is bad**/
        if(!AppUtil.isConnected(this))
			AppUtil.setNetworkMethod(this);
		/**internet connect is ok**/
		else {
			//从服务器接受数据初始化场馆信息
//			RefreshVenueInfo.doRefreshVenueInfo(UserInfo.httpClient, parentHandler);
			//get top 5 news
			BaseTaskPool.getInstance().addTask(new GetTop5NewsTask(NameConstant.api.getTopNews));
			BaseTaskPool.getInstance().addTask(new QueryVenuesInfoTask(NameConstant.api.queryVenuesInfo));
			//读取用户的设置，是否为记住密码
			SharedPreferences sp = AppUtil.getSharedPreferences(this);
			boolean rememberme = sp.getBoolean("rememberme",false);
			/**auto login**/
			if(rememberme) {
				Intent intentLogin = new Intent(this, AutoLoginService.class);
				intentLogin.setAction(AutoLoginService.NAME + BaseService.ACTION_START);
				startService(intentLogin);
			}
		}

        /**delay start**/
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
