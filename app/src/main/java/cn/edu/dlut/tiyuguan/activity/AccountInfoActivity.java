package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.widget.pull2zoomview.PullToZoomListViewEx;
import cn.edu.dlut.tiyuguan.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountInfoActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinfo);
		//设置actionbar
		ActionBar actionBar = this.getActionBar();    
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setTitle(" ");
	    View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
	    ((TextView)actionbarLayout).setText("我的账号信息");
	     actionBar.setDisplayShowCustomEnabled(true);
	     ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
	     //actionbar结束
	     actionBar.setCustomView(actionbarLayout,layout);
	     //actionBar.hide();
		//ListView list = (ListView) findViewById(R.id.accountinfo_listview);
		PullToZoomListViewEx list = (PullToZoomListViewEx) findViewById(R.id.accountinfo_pulltozoomlistview);
		/*String[] leftTiltle=new String[]{"破草鞋dd","修改密码dd","绑定手机账号"};
		String[] rightTiltle=new String[]{"修改 >","修改 >","修改 >"};
		List<HashMap<String, Object>> listHashMap = new ArrayList<HashMap<String, Object>>();
		for(int i=0;i<3;i++)
		{
		 HashMap<String, Object> hashMap=new HashMap<String, Object>();
		 hashMap.put("left", leftTiltle[i]);
		 hashMap.put("right", rightTiltle[i]);
		 listHashMap.add(hashMap);
		}
		
	
		SimpleAdapter adapter=new SimpleAdapter(this, listHashMap, R.layout.accountinfo_row, new String[]{"left","right"},new int[]{R.id.accountinfo_row_left,R.id.accountinfo_row_right});*/
		MyListAdapter adapter=new MyListAdapter(this);
		list.setAdapter(adapter);
		//下面是对pulltozoomlistview的用法
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (1.7F * (mScreenHeight / 5.0F)));
        list.setHeaderLayoutParams(localObject);
        View headerView=this.getLayoutInflater().inflate(R.layout.profile_head_view, null);
        list.setHeaderView(headerView);
        View zoomView=this.getLayoutInflater().inflate(R.layout.profile_head_zoom_view, null);
        list.setZoomView(zoomView);
        list.setZoomEnabled(true);
        list.setParallax(true);
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
class  MyListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	public MyListAdapter(Context context){

		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
		
		convertView=mInflater.inflate(R.layout.accountinfo_row, null);
		TextView tvLeft=(TextView)(convertView.findViewById(R.id.accountinfo_row_left));
		switch (position) {
		case 0:
			tvLeft.setText("用户名");
			break;
		case 1:
			tvLeft.setText("修改密码");
			break;
		case 2:
			tvLeft.setText("绑定手机号码:");
			break;

		default:
			break;
		}
		return convertView;
	}
	
	//内部类
}
