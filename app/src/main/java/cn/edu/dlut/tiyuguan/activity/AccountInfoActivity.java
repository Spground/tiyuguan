package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.model.User;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import cn.edu.dlut.tiyuguan.widget.pull2zoomview.PullToZoomListViewEx;
import cn.edu.dlut.tiyuguan.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AccountInfoActivity extends BaseUi {

	private Button logout_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountinfo);
		/**init actionbar**/
		initActionBar("我的账号信息");
		init();
	}

	/**init everything**/
	private void init(){
		/**logout btn event init**/
		logout_btn = (Button)findViewById(R.id.logout_btn);
		logout_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseAuth.getUser().setRecordMap(null);
				BaseAuth.setLogin(false);
				/**update the preferences file**/
				SharedPreferences sp = AppUtil.getSharedPreferences(AccountInfoActivity.this);
				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("rememberme",false);
				editor.commit();

				ToastUtil.showInfoToast(AccountInfoActivity.this,"注销成功！");
				AccountInfoActivity.this.finish();
			}
		});

		PullToZoomListViewEx list = (PullToZoomListViewEx) findViewById(R.id.accountinfo_pulltozoomlistview);
		MyListAdapter adapter = new MyListAdapter(this);
		list.setAdapter(adapter);

		//pulltozoomlistview usage
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		int mScreenHeight = localDisplayMetrics.heightPixels;
		int mScreenWidth = localDisplayMetrics.widthPixels;
		AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (1.7F * (mScreenHeight / 5.0F)));

		/**设置ListView顶部的布局**/
		list.setHeaderLayoutParams(localObject);

		/**设置用户profile**/
		View headerView = this.getLayoutInflater().inflate(R.layout.profile_head_view, null);
		((TextView)(headerView.findViewById(R.id.tv_user_name))).setText(User.getInstance().getUserName());
		list.setHeaderView(headerView);

		/**设置头像背景**/
		View zoomView = this.getLayoutInflater().inflate(R.layout.profile_head_zoom_view, null);
		list.setZoomView(zoomView);

		list.setZoomEnabled(true);
		list.setParallax(true);
	}

	/**Adapter Inner Class**/
	class  MyListAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public MyListAdapter(Context context){
			mInflater = LayoutInflater.from(context);
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
			convertView = mInflater.inflate(R.layout.accountinfo_row, null);
			TextView tvLeft=(TextView)(convertView.findViewById(R.id.accountinfo_row_left));
			switch (position) {
				case 0:
					tvLeft.setText("用户ID:" + User.getInstance().getUserId());
					break;
				case 1:
					tvLeft.setText("用户名:" + User.getInstance().getUserName());
					break;
				case 2:
					tvLeft.setText("信用等级:" + User.getInstance().getCreditWorthiness());
					break;
				default:
					break;
			}
			return convertView;
		}

		//内部类
	}
}
