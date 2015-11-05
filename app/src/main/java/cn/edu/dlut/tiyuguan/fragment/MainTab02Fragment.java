package cn.edu.dlut.tiyuguan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.edu.dlut.tiyuguan.activity.MakeReserveActivity;
import cn.edu.dlut.tiyuguan.adapterview.MyListView.OnRefreshListener;
import cn.edu.dlut.tiyuguan.activity.GotoOrderActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenueInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenueInfo;

import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.task.QueryVenuesInfoTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class MainTab02Fragment extends Fragment{

	int[] drawableIds={R.drawable.bask,R.drawable.swim,
			R.drawable.taiqiu,R.drawable.yumao,R.drawable.ping};
	int[] nameIds={R.string.bask,R.string.swim,
			R.string.taiqiu,R.string.yumao,R.string.ping};
	 View messageLayout;
     MyListView mListView;
     MyAdapter myAdapter;

    AsyncTask<String,Void,String> refreshTask;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        messageLayout = inflater.inflate(R.layout.main_tab_02, container, false);
        initView();
		//自定义适配器
		 return messageLayout;
	}

    /****/
    private void initView() {

        mListView = (MyListView)messageLayout.findViewById(R.id.ListView01);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
        //lv设置监听跳转到预约界面,监听用户选择哪一个场馆
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
                                    long arg3) {
                // TODO Auto-generated method stub
                //if user is not login
                if(!BaseAuth.isLogin()){
                    ToastUtil.showInfoToast(getActivity(),"未登录，请您登陆再操作！");
                    ((MainActivity)getActivity()).switchFragment("我的");
                    return;
                }
                Intent intent = new Intent(getActivity(),MakeReserveActivity.class);
                AppUtil.debugV("====TAG====","选择的ListView Index" + postion);
                switch(postion) {
                    //篮球预约
                    case 1:
                        intent.putExtra("venues_id", 1);
                        startActivity(intent);
                        break;
                    //游泳
                    case 2:
                        intent.putExtra("venues_id", 3);
                        startActivity(intent);
                        break;
                    //台球
                    case 3:
                        intent.putExtra("venues_id", 4);
                        startActivity(intent);
                        break;
                    //羽毛球
                    case 4:
                        intent.putExtra("venues_id", 2);
                        startActivity(intent);
                        break;
                    //乒乓球
                    case 5:
                        intent.putExtra("venues_id", 5);
                        startActivity(intent);
                        break;
                }
            }
        });
        //刷新的监听
        mListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(Sport.getInstance().getVenuesHashMap() == null
                        ||
                        Sport.getInstance().getVenuesHashMap().size() == 0
                        )
                    BaseTaskPool
                            .getInstance()
                            .addTask(new QueryVenuesInfoTask(NameConstant.api.queryVenuesInfo));

                if(refreshTask != null && refreshTask.getStatus() == AsyncTask.Status.RUNNING)
                    refreshTask.cancel(true);
                String queryUrl = "";
                refreshTask =  new AsyncTask<String, Void, String>() {
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        myAdapter.notifyDataSetChanged();
                        mListView.onRefreshComplete();
                    }
                };
                refreshTask.execute(queryUrl, null, null);
            }
        });
    }

    /**自定义适配器**/
    class MyAdapter extends  BaseAdapter{


        @Override
        public int getCount() {
            return nameIds.length;
        }

        @Override
        public Object getItem(int i) {
            return nameIds[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.main_tab_02_listview_item,null);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.id_main_tab_02_listview_item_image_view);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.id_main_tab_02_listview_item_text_view);
                viewHolder.numTextView = (TextView)convertView.findViewById(R.id.id_main_tab_02_istview_item_num_textview);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            //fulfill view with data
            viewHolder.imageView.setImageResource(drawableIds[position]);
            viewHolder.textView.setText(nameIds[position]);
            viewHolder.numTextView.setText((int)(50 * Math.random()) + "");

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView numTextView;
    }
}




