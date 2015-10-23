package cn.edu.dlut.tiyuguan.fragment;

import cn.edu.dlut.tiyuguan.activity.AccountInfoActivity;
import cn.edu.dlut.tiyuguan.activity.FeedBackActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.activity.RecordListActivity;
import cn.edu.dlut.tiyuguan.base.BaseAuth;

import com.devspark.appmsg.AppMsg;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.animation.lib.Effectstype;
import cn.edu.dlut.tiyuguan.animation.lib.NiftyDialogBuilder;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.LoginFailedEvent;
import cn.edu.dlut.tiyuguan.event.LoginSuccessEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * third fragment UI
 */
public class MainTab03Fragment extends Fragment {

	private LayoutInflater inflater;
	private View fragmentView;

	private String titleShowNum = "预约订单";
	private boolean eventBusRgister = false;

	private MyListAdapter myListAdapter,myListAdapter1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.debugV("=====TAG=====","TAG Main03 onCreate");
		inflater = LayoutInflater.from(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtil.debugV("=====TAG=====", "TAG Main03 onCreateView");
		fragmentView = inflater.inflate(R.layout.main_tab_03, container, false);
		return fragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		AppUtil.debugV("=====TAG=====", "TAG Main03 onActivityCreated");
	}
	@Override
	public void onStart() {
		super.onStart();
		AppUtil.debugV("=====TAG=====", "TAG Main03 onStart");
		updateTopView();
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		AppUtil.debugV("=====TAG=====", "TAG Main03 onResume");
		if(!eventBusRgister){
			EventBus.getDefault().register(this);
			eventBusRgister = true;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		AppUtil.debugV("=====TAG=====", "TAG Main03 onStop");
		if(eventBusRgister){
			EventBus.getDefault().unregister(this);
			eventBusRgister = false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppUtil.debugV("=====TAG=====", "TAG Main03 onDestroy");
		if(eventBusRgister){
			EventBus.getDefault().unregister(this);
			eventBusRgister = false;
		}
	}
	/**初始化视图**/
	private void init(){
		final Button loginBtn = (Button)fragmentView.findViewById(R.id.login_btn);
		//other info items
		ListView listview = (ListView)fragmentView.findViewById(R.id.listview_aboutbook);
		ListView listview1 = (ListView)fragmentView.findViewById(R.id.listview1_aboutbook);

		//set adapter
		myListAdapter = new MyListAdapter(fragmentView.getContext(),new String[]{"预约订单","账号信息"}, new int[]{R.drawable.ic_action_cart,R.drawable.ic_action_user});
		listview.setAdapter(myListAdapter);
		myListAdapter1 = new MyListAdapter(fragmentView.getContext(),new String[]{"意见反馈","咨询体育馆",
				"检查更新","关于"},new int[]{R.drawable.ic_action_monolog,R.drawable.ic_action_phone_start,R.drawable.ic_action_reload,R.drawable.ic_action_info});
		listview1.setAdapter(myListAdapter1);

		//add event listener
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch(arg2) {
					//查看预约信息
					case 0:
						if(!BaseAuth.isLogin()){
							ToastUtil.showToast(getActivity(),"请登录！");
							if(loginBtn != null){
								loginBtn.performClick();
							}
						}
						else {
							Intent intentRecordList = new Intent(getActivity(), RecordListActivity.class);
							startActivity(intentRecordList);
						}
						break;
					//查看账号信息
					case 1:
						if(!BaseAuth.isLogin()) {
							ToastUtil.showToast(getActivity(), "请登录！");
							if(loginBtn != null){
								loginBtn.performClick();
							}
						}
						else {
							//TODO:
							Intent intent = new Intent(getActivity(),AccountInfoActivity.class);
							startActivity(intent);
						}
						break;
				}
			}});

		//add event listener
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				switch(arg2) {
					//user feedback
					case 0:
						Intent intentFeedBackActivity = new Intent(getActivity(),FeedBackActivity.class);
						startActivity(intentFeedBackActivity);
						break;
					//call the service phone
					case 1:
						//TODO:待简化
						final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
						dialogBuilder
								.withTitle("咨询体育馆")//.withTitle(null)  no title
								.withTitleColor("#FFFFFF")//def
								.withDividerColor("#11000000")//def
								.withMessage(null)
										//.withMessage(null)  no Msg
								.withMessageColor("#FFFFFFFF")//def  | withMessageColor(int resid)
								.withDialogColor("#17b3ed")//def  | withDialogColor(int resid)
								.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
								.withDuration(300)//def
								.withEffect(Effectstype.RotateBottom)//def Effectstype.Slidetop
								.withButton1Text("继续")//def gone
								.withButton2Text("取消")//def gone
								.isCancelableOnTouchOutside(false)//def  | isCancelable(true)
								.setCustomView(R.layout.question_dialog_layout,arg1.getContext())//.setCustomView(View or ResId,context)
								.setButton1Click(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//用户选择拨打电话继续咨询
										Uri uri = Uri.parse(getString(R.string.question_tel));
										Intent intent = new Intent(Intent.ACTION_DIAL, uri);
										startActivity(intent);
									}
								})
								.setButton2Click(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										dialogBuilder.dismiss();
									}
								})
								.show();
						break;
					//check for update
					case 2:
						AppMsg.cancelAll(getActivity());
						AppMsg.makeText(getActivity(), "已经是最新版本了", AppMsg.STYLE_INFO).setLayoutGravity(Gravity.BOTTOM).setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right).show();
						break;
					//about
					case 3:
						final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance((MainActivity)getActivity());
						dialogBuilder1
								.withTitle("关于我们")//.withTitle(null)  no title
								.withTitleColor("#FFFFFF")
								.withDividerColor("#11000000")
								.withMessage(null)
										//.withMessage(null)  no Msg
								.withMessageColor("#FFFFFFFF")//def  | withMessageColor(int resid)
								.withDialogColor("#17b3ed")//def  | withDialogColor(int resid)
								.withIcon(getResources().getDrawable(R.drawable.about_icon))
								.withDuration(700)
								.withEffect(Effectstype.RotateBottom)                                         //def Effectstype.Slidetop
								.withButton1Text("知道了")//def gone
										//.withButton2Text("Cancel")//def gone
								.isCancelableOnTouchOutside(false)//def    | isCancelable(true)
								.setCustomView(R.layout.custome_dialog_layout,arg1.getContext())//.setCustomView(View or ResId,context)
								.setButton1Click(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										dialogBuilder1.dismiss();
									}
								}).show();
						break;
				}//switch end
			}
		});
	}

	/**登录成功 EventBus回调的方法**/
	public void onEventMainThread(LoginSuccessEvent event) {
		AppUtil.debugV("=====TAG=====", "我在UI线程中得到了登录返回的消息\n:" + event.getData());
		rereshListView();
		updateTopView();
		((MainActivity)getActivity()).hideProgressDlg();
	}
	/**登录失败 EventBus回调的方法**/
	public void onEventMainThread(LoginFailedEvent event){
		ToastUtil.showErrorToast(getActivity(),"用户名或密码错误！");
		((MainActivity) getActivity()).hideProgressDlg();
		rereshListView();

	}
	/**网络错误 EventBus回调的方法**/
	public void onEventMainThread(NetworkErrorEvent errorEvent){
		ToastUtil.showErrorToast(getActivity(),"网络未连接或出现错误！");
		((MainActivity) getActivity()).hideProgressDlg();
		rereshListView();
	}
	/**异常发生 EventBus回调的方法**/
	public void onEventMainThread(ExceptionErrorEvent excepttonErrorEvent){
		ToastUtil.showErrorToast(getActivity(),excepttonErrorEvent.getEventDesc());
		((MainActivity) getActivity()).hideProgressDlg();
		rereshListView();
	}

	/**刷新完成 EventBus回调的方法**/
	public void onEventMainThread(RefreshCompletedEvent refreshCompletedEvent){
		AppUtil.debugV("=====TAG=====", "订单数据刷新完成");
		((MainActivity) getActivity()).hideProgressDlg();
		rereshListView();
	}
	/***刷新listview**/
	private void rereshListView(){
		if(myListAdapter != null){
			myListAdapter.notifyDataSetChanged();
			AppUtil.debugV("====TAG====","rereshListView() invoked");
		}

		if(myListAdapter1 != null){
			myListAdapter1.notifyDataSetChanged();
		}
	}
	/**根据用户是否登录更新TopView视图**/
	private void updateTopView(){
		LinearLayout topLinearLayout = (LinearLayout)getActivity().findViewById(R.id.topLinearLayout);
		View topView = null;
		/**移除所有子view**/
		topLinearLayout.removeAllViews();
		if(BaseAuth.isLogin()){
			topView = inflater.inflate(R.layout.toplinearlayout1, null);
			((TextView)topView.findViewById(R.id.show_userid_textview)).setText(BaseAuth.getUser().getUserName()+",欢迎你！");
		}
		else{
			topView = inflater.inflate(R.layout.toplinearlayout0, null);
		}
		topLinearLayout.addView(topView);
	}
	/**custom ListViewAdapter**/
	 class  MyListAdapter extends BaseAdapter {
	    private String[] title;
	    private int[] img;
	    private Context context;
		private LayoutInflater mInflater;

		MyListAdapter(Context context,String[] title,int[] img) {
			   this.context = context;
			   this.title = title;
			   this.img = img;
			   }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
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

		@SuppressWarnings("unused")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewholder = null;

			if(convertView == null) {
			  viewholder = new ViewHolder();
			  this.mInflater = LayoutInflater.from(context);
			  convertView = this.mInflater.inflate(R.layout.row_aboutbook, null);
			  viewholder.imageview = (ImageView)convertView.findViewById(R.id.iv_mybookinfo);
			  viewholder.textview1 = (TextView)convertView.findViewById(R.id.tv1_mybookinfo);
			  viewholder.numTextView = (TextView)convertView.findViewById(R.id.id_main_tab_03_istview_item_num_textview);
			  convertView.setTag(viewholder);
			}
			else {
			  viewholder = (ViewHolder)convertView.getTag();
			}
			viewholder.imageview.setImageResource(img[position]);
			viewholder.textview1.setText(title[position]);
			//show num of records
			if(title[position].equals(titleShowNum)
					&& BaseAuth.isLogin()
					&& BaseAuth.getUser().getRecordMap() != null
					&& BaseAuth.getUser().getRecordMap().size() != 0){
				AppUtil.debugV("====TAG====","refresh listView 1");
				if(BaseAuth.getUser().getRecordMap().size() > 99){
					viewholder.numTextView.setText("99+");
				}
				else{
					viewholder.numTextView.setText(BaseAuth.getUser().getRecordMap().size() + "");
				}
				viewholder.numTextView.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
		/**inner class**/
		private class ViewHolder {
			ImageView imageview;
			TextView textview1,numTextView;
		}
	   }
	}

