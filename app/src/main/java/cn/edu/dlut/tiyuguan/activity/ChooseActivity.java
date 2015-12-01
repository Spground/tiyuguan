package cn.edu.dlut.tiyuguan.activity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.adapterview.MyListView.OnRefreshListener;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenueInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenueInfo;

import cn.edu.dlut.tiyuguan.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//用户点击预定按钮之后
public class ChooseActivity extends Activity{
	//处理线程消息
		public Handler parentHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0x1234) {
					//交给全局类去处理这个字符串
					String str=msg.obj.toString();
					//Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
					if(str.equals("false"))
					{
						
					}
					else
					{
					   VenueInfo.setVenueInfo(str);
					   //internetIsWork=true;
					   //Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
					}
				}
				
					
				
			}
			
		};
	//预定的开始时间
	private int startTimeClock;
	private String date;
	private String venuename;//用户选择的球馆

	//根据时间获得场地剩余的数量的集合
	private HashMap<Integer, Integer> info;
	private int[] time;//获得时间数组排好序的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		init();
		//获得intent传来的数据
		Bundle intentBundle = getIntent().getExtras();
		this.venuename = intentBundle.getString("venuename");
		this.date = intentBundle.getString("date");

		ListView listView = (ListView)findViewById(R.id.chooselistview);
		//如果日期可用
		if( isAvailable(date) ){
		    //获得hashmap

			int id = getVenueIdByName(venuename);
			System.out.println(id);

		    info = VenueInfo.getRemainingQuantity(id, date);

		    if(info == null)
				System.out.println("info is null");

		    //获得时间数组排好序的
		    time = getTimeArray(info);
		    startTimeClock = time[0];
		    MyListAdapter myListAdapter = new MyListAdapter(this,time,info);
		    //加刷新监听
		    addRefreshListener((MyListView)listView, myListAdapter,parentHandler);
			listView.setAdapter(myListAdapter);
		}
		else {
			Toast.makeText(this, "没有当前日期的信息", Toast.LENGTH_LONG).show();
			setContentView(R.layout.unusable);
		}
	}

	//初始化actionBar
	private void init(){
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(" ");
		View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
		((TextView)actionbarLayout).setText("选择时段");
		actionBar.setDisplayShowCustomEnabled(true);
		ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
		actionBar.setCustomView(actionbarLayout,layout);
	}
	//给listview家监听
	public void addRefreshListener(MyListView lv,MyListAdapter mylistadapter,Handler activity) {
		final MyListView mylv=lv;
		final MyListAdapter ba=mylistadapter;
		 mylv.setonRefreshListener(new OnRefreshListener() {  
			  
	            @Override  
	            public void onRefresh() {  
	                new AsyncTask<Void, Void, Void>() {  
	                    protected Void doInBackground(Void... params) {  
	                        try {  
	                            Thread.sleep(1000);  
	                        } catch (Exception e) {  
	                            e.printStackTrace();  
	                        }  
	                  //      list.add("刷新后添加的内容");  
	                        RefreshVenueInfo.doRefreshVenueInfo(UserInfo.httpClient,parentHandler);	 
                            try {
								Thread.currentThread();
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                        return null;  
	                    }  
	  
	                    @Override  
	                    protected void onPostExecute(Void result) {  
	                      
	                    	System.out.println("do");
	                    	ba.updateHashMapInfo();
	                    	ba.notifyDataSetChanged();
	                        mylv.onRefreshComplete();  
	                    }  
	                }.execute(null, null, null);  
	            }  
	        }); 
	}
	//判断日期是否可用
	private Boolean isAvailable(String date) {
		Boolean isAvailable = false;
		String[] temp = VenueInfo.getAvailableDate();
		System.out.println(temp[0]);

		for(int i = 0;i < temp.length; i++) {
			if(date.equals( temp[i]) ) {
				  isAvailable = true;
				  break;
				}
		}
		return isAvailable;
	}
	//得到排好序的时间数组
	private int[] getTimeArray(HashMap<Integer, Integer> info)
	{
		int[] time=null;
		if(info!=null)
		{
		   Set<Integer> clock= info.keySet();
		   Object[] c=clock.toArray();
		   time=new int[c.length];
		 for(int i=0;i<c.length;i++)
		 {
			time[i]=(Integer)c[i];
		 }
		  //排序
		  Arrays.sort(time);
		}
		return time;
	}
	//根据场馆id和时间获取hashmap
	private HashMap<Integer, Integer> getHashMapByVenueIdAndDate(int id,String date)
	{
		
		return VenueInfo.getRemainingQuantity(id, date);
	}
	//根据名字获得场馆id
	private int getVenueIdByName(String name) {
		System.out.println(name);
		if(name.equals("乒乓球馆 ")) return 5;
		if(name.equals("篮球馆")) return 1;
		if(name.equals("游泳馆")) return 3;
		if(name.equals("台球馆")) return 4;
		if(name.equals("羽毛球馆")) return 2;
		return -1;//未找到
	}
//提交预定的按钮响应事件
	public void submitOrder(View v) {
		String timeStr=null;
		String dateStr=null;
		int index=(Integer)v.getTag();
		timeStr=(startTimeClock+index)+":00-"+(this.startTimeClock+1+index)+":00";
		dateStr=this.date;
		//弹出对话框
		new AlertDialog.Builder(this)
		.setNeutralButton("取消", null)
        .setTitle("订单确认")  
        .setMessage("预定场馆："+this.venuename+"\n"+"价格:5元/小时"+"\n"+"日期:"+dateStr+"\n时间:"+timeStr)  
        .setPositiveButton("提交", new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
            	 Toast.makeText(getBaseContext(), "预订成功！", Toast.LENGTH_SHORT).show();
            }
            
        }) 
        .show();  
		
	}
	
	
	//自定义ListView适配器内部类
	class  MyListAdapter extends BaseAdapter {
			 LayoutInflater temp ;
			 Context context;
			 int[] time;
			 HashMap<Integer, Integer> info_=null;
			 //更新info
			 public void updateHashMapInfo() {
				 info_= VenueInfo.getRemainingQuantity(getVenueIdByName(venuename), date);
			 }
			 public MyListAdapter(Context context,int[] time, HashMap<Integer, Integer> info) {
				 this.context=context;
				 this.temp=LayoutInflater.from(context);
				 this.time=time;
				 this.info_=info;
				 for(int i=0;i<time.length;i++) {
					 System.out.println(time[i]);
				 }
			 }
			 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return time.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			System.out.println("更新");
			View view =convertView;
			String str=(this.time[position])+":00-"+(this.time[position]+1)+":00";
			if(view==null) view=temp.inflate(R.layout.row_choose, null);
			TextView timeTV=(TextView)view.findViewById(R.id.timeTextView);
			TextView quantityTV=(TextView)view.findViewById(R.id.quantityTextView);
			Button btn=(Button)view.findViewById(R.id.orderbtn);
			//家监听设置按钮的tag
			btn.setTag(position);
			timeTV.setText(str);
			int s= info_.get(time[position]);
			System.out.println(s);
			String tt="剩余"+String.valueOf(s)+"个";
			quantityTV.setText(tt);
			return view;
			
		}
			 
		 }
		 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
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


