package cn.edu.dlut.tiyuguan.activity;

import java.util.HashMap;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.global.VenueBasicInfo;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class GotoOrderActivity extends Activity{

	
	private String venuename;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		//取得传来的参数index
		Intent intent = getIntent();
		int index = intent.getIntExtra("index",-1);
		setContentView( chooseView(index) );
	}
	//初始化actionBar
	private void  init(){
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(" ");
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.action_bar_title_view, null);
		actionBar.setDisplayShowCustomEnabled(true);
		ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
		actionBar.setCustomView(actionbarLayout,layout);
	}

	//从Intent中接受到用户选择了哪一个馆，动态加载界面
	public View chooseView(int index) {
			//初始化view
			View temp = null;
			LayoutInflater inflater = getLayoutInflater();
			temp = inflater.inflate(R.layout.gotoorder_activity, null);
			//图片
			ImageView iV = (ImageView)temp.findViewById(R.id.iv);
			//标题，日期今天 明天 后天 开放时间，收费标准
			TextView dateToday,dateTomorrow,dateTheDayAfterTomorrow,openTime,chargeStandar;
			Button btn0,btn1,btn2;//预定按钮，今天，明天，后天
			dateToday = (TextView)temp.findViewById(R.id.dateToday);
			dateTomorrow = (TextView)temp.findViewById(R.id.dateTomorrow);
			dateTheDayAfterTomorrow = (TextView)temp.findViewById(R.id.dateTheDayAfterTomorrow);
			openTime = (TextView)temp.findViewById(R.id.openTime);
			chargeStandar = (TextView)temp.findViewById(R.id.chargeStandar);
			btn0 = (Button)temp.findViewById(R.id.btnToday);
			btn1 = (Button)temp.findViewById(R.id.btnTomorrow);
			btn2 = (Button)temp.findViewById(R.id.btnTheDayAfterTomorrow);
			btn0.setTag(0);
			btn1.setTag(1);
			btn2.setTag(2);
			//获取日期信息
			HashMap<String, String> date = VenueBasicInfo.getOrderDate();
		    HashMap<String, String> detailInfo = null;
			switch(index) {
			//篮球馆
			case 1:{
				     iV.setBackgroundResource(R.drawable.background);
				     detailInfo = VenueBasicInfo.getBasketBallVenueInfo();
				     venuename = "篮球馆";
			       }break;
			
			//游泳馆
			case 2: {
				      iV.setBackgroundResource(R.drawable.swimingbackground);
				      detailInfo = VenueBasicInfo.getSwimmingVenueInfo();
				      venuename = "游泳馆";
			        }
			break;
			
			//台球馆
			case 3:
			{
				iV.setBackgroundResource(R.drawable.taiqiubackground);
				detailInfo = VenueBasicInfo.getTaiQiuVenueInfo();
				venuename = "台球馆";
			}break;
	        //羽毛球馆
			case 4:
			{
				iV.setBackgroundResource(R.drawable.badmintonbackground);
				detailInfo= VenueBasicInfo.getBadmintonVenueInfo();
				venuename = "羽毛球馆";
			}
				break;
			//乒乓球馆
			case 5: 
				{
					iV.setBackgroundResource(R.drawable.pingpangqiubackground);
					detailInfo= VenueBasicInfo.getTableTennisVenueInfo();
					venuename = "乒乓球馆 ";
				}break;
			
			
			}
			//填充信息到视图
			String str = detailInfo.get("title");
			ActionBar actionBar = this.getActionBar(); 
			View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.action_bar_title_view, null);
			((TextView)actionbarLayout).setText(str);
		    actionBar.setDisplayShowCustomEnabled(true);
		    ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
		    actionBar.setCustomView(actionbarLayout,layout);
			dateToday.setText(new StringBuilder(date.get("Today")).delete(0, 5));
			dateTomorrow.setText(new StringBuilder(date.get("Tomorrow")).delete(0, 5));
			dateTheDayAfterTomorrow.setText(new StringBuilder(date.get("TheDayAfterTomorrow")).delete(0, 5));
			openTime.setText(detailInfo.get("openTime"));
			chargeStandar.setText(detailInfo.get("chargeStandar"));
			return temp;
		}
	
	
	//统一按钮响应事件
	public void doClick(View v) {
		int tag = (Integer)v.getTag();//0代表今天 1代表明天 2代表后天
		String date = null;//预定日期
//		Intent intent = new Intent(this,ChooseActivity.class);
		Intent intent = new Intent(this,MakeReserveActivity.class);
		Bundle bundle = new Bundle();
		if(tag == 0) date = VenueBasicInfo.getOrderDate().get("Today");
	    if(tag == 1) date = VenueBasicInfo.getOrderDate().get("Tomorrow");
	    if(tag == 2) date = VenueBasicInfo.getOrderDate().get("TheDayAfterTomorrow");
		//将日期，和场馆名称传到跳转的activity
	    bundle.putString("date", date);
		bundle.putString("venuename", this.venuename);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch ( item.getItemId() ) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
