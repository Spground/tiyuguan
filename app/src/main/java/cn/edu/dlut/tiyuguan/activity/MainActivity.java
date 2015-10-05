package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.fragment.MainTab01Fragment;
import cn.edu.dlut.tiyuguan.fragment.MainTab02Fragment;
import cn.edu.dlut.tiyuguan.fragment.MainTab03Fragment;
import cn.edu.dlut.tiyuguan.global.Img;
import cn.edu.dlut.tiyuguan.global.LoginInfo;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.internet.DoPost;

import com.andreabaccega.widget.FormEditText;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.animation.lib.Effectstype;
import cn.edu.dlut.tiyuguan.animation.lib.NiftyDialogBuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
{
	private long end_time = 0;//按下返回键的上一次时间
	private LayoutInflater inflater;

	private String userName = "";
	private String passWord = "";
	private Boolean rememberme = false;
	//测试Git临时加的注释，用后即删

	private int resImage[] = {R.drawable.tab_home_normal,R.drawable.tab_book_normall,R.drawable.tab_aboutme_normal};
    private int resImagePressed[] = {R.drawable.tab_home_pressed,R.drawable.tab_book_pressed,R.drawable.tab_aboutme_pressed};
    private String[] textView = {"首页","预约","我的"};

    private FragmentTabHost mTabHost;
    private View indicator;
   
    public Handler parentHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		if(msg.what==0X1238){
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
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m0);
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        inflater = LayoutInflater.from(this);
     // 添加tab名称和图标  
        indicator = inflater.inflate(R.layout.tab_item_view, null);
        @SuppressWarnings("rawtypes")
		Class[] fragmentArrary = {MainTab01Fragment.class,MainTab02Fragment.class,MainTab03Fragment.class};
        int resTabItem = R.layout.tab_item_view;//布局资源indicator
        initFragmentTabHost(mTabHost, fragmentArrary,resTabItem );
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				updateTabStyle(mTabHost);
			}
		});

	}
	//动态更新tab的样式
	private void updateTabStyle(FragmentTabHost mTabHost) {
		resetTabStyle(mTabHost);//从新设置
		for(int i = 0;i < mTabHost.getTabWidget().getChildCount();i++){
			if(mTabHost.getCurrentTab() == i){
				System.out.println("被调用");
				((ImageButton)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_imgbtn)).setImageResource(resImagePressed[i]);
				((TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_textview)).setTextColor(getResources().getColor(R.color.tab_pressed_textcolor));
			}
		}
	};
	//重设tab的样式,包括图片按钮，文字颜色
	protected void resetTabStyle(FragmentTabHost mTabHost)
	{
		for(int i = 0;i < mTabHost.getTabWidget().getChildCount();i++){
			((ImageButton)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_imgbtn)).setImageResource(resImage[i]);
			((TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_textview)).setTextColor(getResources().getColor(R.color.tab_normal_textcolor));
			((TextView)mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_textview)).setText(textView[i]);
		}
	}
//初始化fragmenttanhost
	private void initFragmentTabHost(FragmentTabHost mTabHost,@SuppressWarnings("rawtypes") Class fragmentArrary[],int resTabItem)
	{
		LayoutInflater inflater = getLayoutInflater();
		int size = fragmentArrary.length;
		for(int i = 0;i < size;i++){
			indicator=inflater.inflate(resTabItem, null);		
	        resetTabStyle(mTabHost);
	        mTabHost.addTab(mTabHost.newTabSpec(textView[i]).setIndicator(indicator), fragmentArrary[i], null); 
	        updateTabStyle(mTabHost);
		}
		
	}
	//响应MainTab03的登陆按钮
	public void logInBtn(View view)
	{
		//采用的好看的开源控件
	    final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
		dialogBuilder
	      .withTitle("登陆")
	      .withTitleColor("#FFFFFF")
	      .withDividerColor("#11000000")
	      .withMessage(null) 
	      .withMessageColor("#FFFFFFFF")
	      .withDialogColor("#17b3ed")
	      .withIcon(getResources().getDrawable(R.drawable.ic_launcher))
	      .withDuration(700)
	      .withEffect(Effectstype.Shake)
	      .withButton1Text("登陆")
	      .withButton2Text("取消")
	      .setCustomView(R.layout.popup_login,view.getContext())
	      .setButton1Click(new View.OnClickListener() {
	          @Override
	          public void onClick(View v) {
	        	  //登陆代码在这里写      	
	        	  View parent = (View) v.getParent().getParent();//获取当前dialog的根视图
	        	  View customPanel = (FrameLayout) parent.findViewById(R.id.customPanel);//获取Dialog_Layout布局中的FramLayout的视图
	        	  if(customPanel == null){
	        		  return;	        		 	        		  
	        	  }	
	        	 ViewGroup viewGroup = (ViewGroup) customPanel;//将FrameLayout视图装换位ViewGroup
	        	 View childView = viewGroup.getChildAt(0);
	        	 if(childView != null){//获取输入框的内容
	        		 System.out.println("childView非空");
	        		 FormEditText  userNameEdt = ((FormEditText)childView.findViewById(R.id.account_edittext));
	        		 userNameEdt.testValidity();
	        		 FormEditText passWordEdt = ((FormEditText)childView.findViewById(R.id.password_edittext));
	        		 passWordEdt.testValidity();
	        		 CheckBox isrememberme = (CheckBox)childView.findViewById(R.id.rememberme);
		        	 userName = userNameEdt.getText().toString();
		        	 passWord = passWordEdt.getText().toString();
		        	 rememberme = isrememberme.isChecked();
	        	 }
	        	 else
	        	 {
	        		 return;
	        	 }
	        	    
	        	    System.out.println("用户名为"+userName);
					System.out.println(userName+" "+passWord+""+rememberme);
					if(!verifyInput(userName, passWord)){
						
						//验证用户失败，然后弹出对话框
						YoYo.with(Techniques.Shake).playOn(childView);
					     //AppMsg.cancelAll(currentActivity);	//防止用户点击很多次				
						//AppMsg.makeText(currentActivity, "用户名或密码不能为空", AppMsg.STYLE_ALERT).setLayoutGravity(Gravity.TOP).setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right).show();
						
						
		
					}
					else//验证用户输入成功
					{									
					    //登陆了调用验证方法validateAccess()
						//用户名密码验证合法后，将信息写入LoginInfo的全局变量
						(new UserInfo()).setIfRememberMe(rememberme);//将信息写入文件中
						LoginInfo.settLoginInfo(userName, passWord, rememberme);
						//post用户名和密码
					   validateAccess();
					   dialogBuilder.dismiss();//关闭对话框
						
					}
	                      }
	      })
	      .setButton2Click(new View.OnClickListener() {
	          @Override
	          public void onClick(View v) {
	              //Toast.makeText(v.getContext(),"i'm btn2",Toast.LENGTH_SHORT).show();
	              dialogBuilder.dismiss();
	          }
	      })
	      .show();
		
		
		
	}
	protected Activity getActivity() {
		// 
		return null;
	}
	//验证用户权限
	private Boolean validateAccess()
	{
		
		
		Handler handler = new Handler()
		{
			@SuppressLint("InflateParams") @Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0x123)
				{
					String result=msg.obj.toString();
					if(!result.equals("false"))//登陆成功
					{
						//将用户是否登陆成功的信息写入全局变量
						LoginInfo.settLoginState(true);
						//顶部的布局
						LinearLayout topLinearLayout=(LinearLayout)findViewById(R.id.topLinearLayout);
						View temp = null;
						topLinearLayout.removeAllViews();
						//选择性添加顶部视图
						if(!LoginInfo.getLoginState())//如果登陆失败
						{
							       temp = (View)inflater.inflate(R.layout.toplinearlayout0, null);
							       
						}
						else//如果登陆成功
						{
							       temp = (View)inflater.inflate(R.layout.toplinearlayout1, null);
						}          ((TextView)temp.findViewById(R.id.show_userid_textview)).setText(LoginInfo.userID+",欢迎你！");
						
						topLinearLayout.addView(temp);
					    Toast.makeText(getBaseContext(), result,Toast.LENGTH_LONG).show();
					    Toast.makeText(getBaseContext(), result,Toast.LENGTH_LONG).show();
					}
					else//登录失败
						Toast.makeText(getBaseContext(), "登录失败！",Toast.LENGTH_LONG).show();
				}
			}
		};
		//新建post线程
		new Thread(new DoPost(UserInfo.httpClient,handler, "http://dutgymbook.sinaapp.com/login.action", userName, passWord,LoginInfo.rememberme,0)).start();
		
		
		return true;
	}
/*下面是验证用户的输入是否合法*/
	private Boolean verifyInput(String userName,String passWord)
	{
		
		if((userName.trim()).length() == 0||passWord.trim().length() == 0) return false;
		else return true;
	}
	//按两次按钮退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - this.end_time >= 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				this.end_time = System.currentTimeMillis();
			} else {
			
				this.finish();
				System.exit(0);
				return true;
			}
			return true;
		}
		
		
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTabHost = null;  
	}
	
		
}
