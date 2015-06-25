package cn.edu.dlut.tiyuguan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.edu.dlut.tiyuguan.adapterview.MyListView.OnRefreshListener;
import cn.edu.dlut.tiyuguan.activity.GotoOrderActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenusInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenusInfo;

import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainTab02Fragment extends Fragment{
	int[] drawableIds={R.drawable.bask,R.drawable.swim,
			R.drawable.taiqiu,R.drawable.yumao,R.drawable.ping};
	int[] nameIds={R.string.bask,R.string.swim,
			R.string.taiqiu,R.string.yumao,R.string.ping};
	private View messageLayout;
//处理线程消息
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
				//Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				if(str.equals("false"))
				{
					
				}
				else
				{
				   VenusInfo.setVenusInfo(str);
				   //internetIsWork=true;
				   //Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();
				}
			}
			
		}
		
	};
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		messageLayout = inflater.inflate(R.layout.main_tab_02, container, false);
		
	/*	List<Map<String,Object>>listItems  =new ArrayList<Map<String,Object>>();
		//建立容器Adapter
		for(int i =0;i<nameIds.length;i++){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("header", drawableIds[i]);
			listItem.put("personName",getResources().getString(nameIds[i]));
			listItems.add(listItem);
		}
	*/
		
		
		
		
		
		
		
		final MyListView lv;
		lv=(MyListView)messageLayout.findViewById(R.id.ListView01);
		//自定义适配器
		final BaseAdapter ba=new BaseAdapter(){
			public int getCount() {
				return 5;
			}
			public Object getItem(int position) {
				return position;
			}
			public long getItemId(int position) {
				return position;
			}
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				LinearLayout ll=new LinearLayout(messageLayout.getContext());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setPadding(5,5,5,5);
				ImageView  ii=new ImageView(messageLayout.getContext());
				ii.setImageDrawable(getResources().getDrawable(drawableIds[arg0]));
				ii.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				ii.setLayoutParams(new Gallery.LayoutParams(100,100));
				ll.addView(ii);
				//初始化textview
				TextView tv=new TextView(messageLayout.getContext());
				tv.setText(getResources().getText(nameIds[arg0]));//设置内容
				tv.setTextSize(24);	             				//设置字体大小
				tv.setTextColor(messageLayout.getContext().getResources().getColor(R.color.black));
																//设置字体颜色
				tv.setPadding(5,2,5,2);							//设置四周留白
			    tv.setGravity(Gravity.LEFT);
				ll.addView(tv);									//添加到LinearLayout中				
				
				TextView tt=new TextView(messageLayout.getContext());
				tt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				tt.setText(getResources().getText(R.string.next));//设置内容
				tt.setTextSize(34);	             				//设置字体大小
				tt.setGravity(Gravity.RIGHT);
				tt.setTextColor(MainTab02Fragment.this.getResources().getColor(R.color.gray));//设置字体颜色
				
				tt.setPadding(5,2,5,2);							//设置四周留白
				
				
				ll.addView(tt);		
				ll.setBackgroundColor(getResources().getColor(R.color.white));
				ll.setPadding(15, 0, 10, 0);
				return ll;
			}        	
			
		};
		
		lv.setAdapter(ba);
		//设置选项选中的监听器
		//lv设置监听跳转到预约界面,监听用户选择哪一个场馆
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//实例化将要跳转到预定界面的Intent
				Intent intentBasketballOrder=new Intent((MainActivity)getActivity(),GotoOrderActivity.class);
				
				switch(arg2)
				{
				//篮球预约
				
				case 1:
				{
					
					intentBasketballOrder.putExtra("index", 1);
					startActivity(intentBasketballOrder);
					
				}break;
				//游泳
				case 2:
				{
					
					intentBasketballOrder.putExtra("index", 2);
					startActivity(intentBasketballOrder);
				}break;
				//台球
				case 3:
				{
					
					intentBasketballOrder.putExtra("index", 3);
					startActivity(intentBasketballOrder);
				}break;
				//羽毛球
				case 4:
				{
					intentBasketballOrder.putExtra("index", 4);
					startActivity(intentBasketballOrder);
				}break;
				//乒乓球
				case 5:
				{
					intentBasketballOrder.putExtra("index", 5);
					startActivity(intentBasketballOrder);
				}break;
				
				
				
				}
				
				
				
				
			}
		});
		 lv.setonRefreshListener(new OnRefreshListener() {  
			  
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
	                        RefreshVenusInfo.doRefreshVenusInfo(UserInfo.httpClient,parentHandler);
	                        return null;  
	                    }  
	  
	                    @Override  
	                    protected void onPostExecute(Void result) {  
	                      
	                        ba.notifyDataSetChanged();
	                        
	                        lv.onRefreshComplete();  
	                    }  
	                }.execute(null, null, null);  
	            }  
	        }); 
		 return messageLayout;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
		//return messageLayout;
	}
		
		
		
		
	}


