package cn.edu.dlut.tiyuguan.fragment;

import cn.edu.dlut.tiyuguan.activity.AccountInfoActivity;
import cn.edu.dlut.tiyuguan.activity.FeedBackActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseMessageEvent;
import cn.edu.dlut.tiyuguan.base.BaseService;

import com.devspark.appmsg.AppMsg;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.animation.lib.Effectstype;
import cn.edu.dlut.tiyuguan.animation.lib.NiftyDialogBuilder;
import cn.edu.dlut.tiyuguan.model.User;
import cn.edu.dlut.tiyuguan.service.AutoLoginService;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(getActivity());
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateTopView();
		init();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentView = inflater.inflate(R.layout.main_tab_03, container, false);
		return fragmentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	/**初始化视图**/
	private void init(){
		final Button loginBtn = (Button)fragmentView.findViewById(R.id.login_btn);
		//other info items
		ListView listview = (ListView)fragmentView.findViewById(R.id.listview_aboutbook);
		ListView listview1 = (ListView)fragmentView.findViewById(R.id.listview1_aboutbook);

		//set adapter
		MyListAdapter myListAdapter = new MyListAdapter(fragmentView.getContext(),new String[]{"预约信息","账号信息"}, new int[]{R.drawable.my_bookinfo,R.drawable.my_bookinfo1});
		listview.setAdapter(myListAdapter);
		MyListAdapter myListAdapter1 = new MyListAdapter(fragmentView.getContext(),new String[]{"意见反馈","咨询体育馆",
				"检查更新","关于"},new int[]{R.drawable.user_feedback,R.drawable.tel_question,R.drawable.check_update,R.drawable.about});
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
							ToastUtil.showToast(getActivity(),"您已经登录成功！！");
						}
						break;
					//查看账号信息
					case 1:
						if(!BaseAuth.isLogin()) {
							ToastUtil.showToast(getActivity(), "请登录！");
						}
						else {
							//TODO:
							ToastUtil.showToast(getActivity(),"登录成功！");
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

	/**EventBus回调的方法**/
	public void onEventMainThread(BaseMessageEvent event) {
		ToastUtil.showToast(getActivity(),"我在UI线程中得到了登录返回的消息:" + event.data);
		Log.v("TAG", "我在UI线程中得到了登录返回的消息\n:" + event.data);
		try {
			BaseMessage message = AppUtil.getMessage(event.data);
			User user = (User)message.getData("User");
			Log.v("TAG", "user对象\n:" + ((User)user).getName());
			if((user).getName() != null || message.getMessage().equals("success")){
				BaseAuth.setUser(user);
				BaseAuth.setLogin(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.showErrorToast(getActivity(),"error msg code:0000" + e);
		}
		finally {
			updateTopView();
			((MainActivity)getActivity()).hideProgressDlg();
		}

		/**stop登录服务**/
		Intent loginIntent = new Intent(getActivity(), AutoLoginService.class);
		loginIntent.setAction(AutoLoginService.NAME + BaseService.ACTION_STOP);
		getActivity().startService(loginIntent);

	}

	/**根据用户是否登录更新TopView视图**/
	private void updateTopView(){
		LinearLayout topLinearLayout = (LinearLayout)getActivity().findViewById(R.id.topLinearLayout);
		View topView = null;
		/**移除所有子view**/
		topLinearLayout.removeAllViews();
		if(BaseAuth.isLogin()){
			topView = inflater.inflate(R.layout.toplinearlayout1, null);
			((TextView)topView.findViewById(R.id.show_userid_textview)).setText(BaseAuth.getUser().getName()+",欢迎你！");
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
			  viewholder.textview2 = (TextView)convertView.findViewById(R.id.tv2_mybookinfo);
			  convertView.setTag(viewholder);
			}
			else {
			  viewholder = (ViewHolder)convertView.getTag();
			}

			viewholder.imageview.setBackgroundResource(img[position]);
			viewholder.textview1.setText(title[position]);
			viewholder.textview2.setText(">");

			return convertView;
		}
		/**inner class**/
		private class ViewHolder {
			ImageView imageview;
			TextView textview1,textview2;
		}
	   }
	}

