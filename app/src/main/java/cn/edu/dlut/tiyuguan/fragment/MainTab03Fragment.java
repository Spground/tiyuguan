package cn.edu.dlut.tiyuguan.fragment;



import cn.edu.dlut.tiyuguan.activity.AccountInfoActivity;
import cn.edu.dlut.tiyuguan.activity.FeedBackActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.global.LoginInfo;

import com.devspark.appmsg.AppMsg;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.animation.lib.Effectstype;
import cn.edu.dlut.tiyuguan.animation.lib.NiftyDialogBuilder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

public class MainTab03Fragment extends Fragment
{

	String str;
	Handler parentHandler=new Handler()
			{
		
		          @Override
		        public void handleMessage(Message msg) {
		        	// TODO Auto-generated method stub
		        	super.handleMessage(msg);
		        	if(msg.what==0x123)
		        	{
		        		str=msg.obj.toString();
		        		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
		        	}
		        }
			};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		View newsLayout = inflater.inflate(R.layout.main_tab_03, container, false);
		//顶部的布局
		LinearLayout topLinearLayout=(LinearLayout)newsLayout.findViewById(R.id.topLinearLayout);
		View temp=null;
		//选择性添加顶部视图
		if(!LoginInfo.getLoginState())//如果已经登陆了
		{
			       temp=(View)inflater.inflate(R.layout.toplinearlayout0, null);
		}
		else
		{
			       temp=(View)inflater.inflate(R.layout.toplinearlayout1, null);
			       ((TextView)temp.findViewById(R.id.show_userid_textview)).setText(LoginInfo.getLoginUserId()+",欢迎你！");
		}      
		topLinearLayout.addView(temp);
	    //找到布局文件的listview
		final Button loginBtn=(Button)newsLayout.findViewById(R.id.login_btn);
        //显示用户信息与用户预约信息的listview
        ListView listview=(ListView)newsLayout.findViewById(R.id.listview_aboutbook);
        //显示其他相关项的listview
        ListView listview1=(ListView)newsLayout.findViewById(R.id.listview1_aboutbook);
        //分别设置各自的适配器
        MyListAdapter my=new MyListAdapter(newsLayout.getContext(),new String[]{"预约信息","账号信息"},
        				new int[]{R.drawable.my_bookinfo,R.drawable.my_bookinfo1});
        listview.setAdapter(my);
        MyListAdapter my1=new MyListAdapter(newsLayout.getContext(),new String[]{"意见反馈","咨询体育馆",
        
        "检查更新","关于"},new int[]{R.drawable.user_feedback,R.drawable.tel_question,R.drawable.check_update,R.drawable.about});
        //
        listview1.setAdapter(my1);
        //为listview添加监听器
        listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2)
				{
				
				case 0://选择预约信息
				{
					if(!LoginInfo.getLoginState())//未登录
					{
						Toast.makeText(getActivity(), "请登陆！", Toast.LENGTH_SHORT).show();
						loginBtn.performClick();//弹出对话框
						
						
						
					}
					else
					{
						Toast.makeText(getActivity(), "你已经登陆成功！", Toast.LENGTH_SHORT).show();
						
					}
					
					
					
				}break;
				case 1://账号信息
				{
					if(!LoginInfo.getLoginState())//未登录
					{
						Toast.makeText(getActivity(), "请登陆！", Toast.LENGTH_SHORT).show();
						//loginBtn.performClick();//弹出对话框
						Intent intent=new Intent((MainActivity)getActivity(),AccountInfoActivity.class);
						startActivity(intent);					}
					else
					{
						Toast.makeText(getActivity(), "你已经登陆成功！",Toast.LENGTH_SHORT).show();
					}
					
					
					
				}break;
				
				}
				
				
				
				
			}});
        //为listview加监听器
        listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2)
				{
				//意见反馈
				case 0:
				{
					//Intent intentBasketballOrder=new Intent((M0Activity)getActivity(),GotoOrder.class);
					//startActivity(intentBasketballOrder);
					Intent intentFeedBackActivity =new Intent((MainActivity)getActivity(),FeedBackActivity.class);
					startActivity(intentFeedBackActivity);
					
					
					
					
				}break;
				//电话咨询体育馆
				case 1:
				{
					/*new AlertDialog.Builder((M0Activity)getActivity())
	                  .setTitle("咨询体育馆")
					  .setIcon(R.drawable.about_icon)
					  .setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
				      .setPositiveButton("继续", new OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//Toast.makeText(getActivity().getBaseContext(), "大连理工大学移动互联网实验室出品！", Toast.LENGTH_LONG).show();
			                Uri uri = Uri.parse("tel:0411-84708978");   
						     Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
						     startActivity(intent);  
						    
					        
					        }   
					           })      
					  .setMessage( "你将拨打体育馆服务热线，是否继续？")
					  .create()
					  .show();*/
					final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance((MainActivity)getActivity());
				      dialogBuilder
				      .withTitle("咨询体育馆")                                  //.withTitle(null)  no title
				      .withTitleColor("#FFFFFF")                                  //def
				      .withDividerColor("#11000000")                              //def
				      .withMessage(null) 
				      //.withMessage(null)  no Msg
				      .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
				      .withDialogColor("#17b3ed")                               //def  | withDialogColor(int resid)
				      .withIcon(getResources().getDrawable(R.drawable.ic_launcher))
				      .withDuration(300)                                          //def
				      .withEffect(Effectstype.RotateBottom)                                         //def Effectstype.Slidetop
				      .withButton1Text("继续")                                      //def gone
				      .withButton2Text("取消")                                  //def gone
				      .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
				     .setCustomView(R.layout.question_dialog_layout,arg1.getContext())         //.setCustomView(View or ResId,context)
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
					
					
				}break;
				//检查更新版本
				case 2:
				{
					
					//Toast.makeText(getActivity().getBaseContext(), "已经是最新版本了！", Toast.LENGTH_LONG).show();
					 AppMsg.cancelAll(getActivity());
					AppMsg.makeText(getActivity(), "已经是最新版本了", AppMsg.STYLE_INFO).setLayoutGravity(Gravity.BOTTOM).setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right).show();
				} break;
      			//关于
				case 3:
				 {
	             /*new AlertDialog.Builder((M0Activity)getActivity())
                  .setTitle("关于我们")
				  .setIcon(R.drawable.about_icon)
			      .setNeutralButton("知道了", new OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//Toast.makeText(getActivity().getBaseContext(), "大连理工大学移动互联网实验室出品！", Toast.LENGTH_LONG).show();
					            }   
				           })
				  .setMessage( "DUT移动互联网实验室出品")
				  .create()
				  .show();*/
					 
		      final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance((MainActivity)getActivity());
		      dialogBuilder
		      .withTitle("关于我们")                                  //.withTitle(null)  no title
		      .withTitleColor("#FFFFFF")                                  //def
		      .withDividerColor("#11000000")                              //def
		      .withMessage(null) 
		      //.withMessage(null)  no Msg
		      .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
		      .withDialogColor("#17b3ed")                               //def  | withDialogColor(int resid)
		      .withIcon(getResources().getDrawable(R.drawable.about_icon))
		      .withDuration(700)                                          //def
		      .withEffect(Effectstype.RotateBottom)                                         //def Effectstype.Slidetop
		      .withButton1Text("知道了")                                      //def gone
		      //.withButton2Text("Cancel")                                  //def gone
		      .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
		     .setCustomView(R.layout.custome_dialog_layout,arg1.getContext())         //.setCustomView(View or ResId,context)
		      .setButton1Click(new View.OnClickListener() {
		          @Override
		          public void onClick(View v) {
		              //Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
		        	  dialogBuilder.dismiss();
		                      }
		      })
		      /*.setButton2Click(new View.OnClickListener() {
		          @Override
		          public void onClick(View v) {
		              Toast.makeText(v.getContext(),"i'm btn2",Toast.LENGTH_SHORT).show();
		          }
		      })*/
		      .show();
		       }break;
				
				}//switch结束
          		}//监听结束
		});
		return newsLayout;
	}
	//自定义ListView适配器
	 class  MyListAdapter extends BaseAdapter
	   {
		   String[] title;
		   int[] img;
		   Context context;

		   //构造函数
		   MyListAdapter(Context context,String[] title,int[] img)
		   {
			   this.context=context;
			   this.title=title;
			   this.img=img;
			   }
		   private LayoutInflater mInflater;
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
			ViewHolder viewholder=null;
			View view=convertView;
			if(true)
			{
			 viewholder=new ViewHolder();
			  this.mInflater=LayoutInflater.from(context);
			  view=this.mInflater.inflate(R.layout.row_aboutbook, null);
			  viewholder.imageview=(ImageView)view.findViewById(R.id.iv_mybookinfo);
			  viewholder.textview1=(TextView)view.findViewById(R.id.tv1_mybookinfo);
			  viewholder.textview2=(TextView)view.findViewById(R.id.tv2_mybookinfo);
			  viewholder.imageview.setBackgroundResource(img[position]);
			  viewholder.textview1.setText(title[position]);
			  //int colorid=getResources().getColor(R.color.blue);
			  //viewholder.textview1.setTextColor(colorid);
			  viewholder.textview2.setText(">");
			  view.setTag(viewholder);
			}
			else
			{
				viewholder=(ViewHolder)view.getTag();
			}
			return view;
			
		}
		//内部类
		private class ViewHolder
		{
			ImageView imageview;
			TextView textview1,textview2;
		}
		
		

			

			
		
		   
	   }
	   
	}
class LoginListener implements View.OnClickListener
{

	public LoginListener(Context context)
	{
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
}
}


