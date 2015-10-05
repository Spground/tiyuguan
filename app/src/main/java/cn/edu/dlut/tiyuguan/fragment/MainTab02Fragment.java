package cn.edu.dlut.tiyuguan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.edu.dlut.tiyuguan.adapterview.MyListView.OnRefreshListener;
import cn.edu.dlut.tiyuguan.activity.GotoOrderActivity;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.global.UserInfo;
import cn.edu.dlut.tiyuguan.global.VenueInfo;
import cn.edu.dlut.tiyuguan.internet.RefreshVenueInfo;

import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.R;

import android.support.v4.app.Fragment;
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

	public  Handler parentHandler=new Handler()
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
				   VenueInfo.setVenueInfo(str);
				}
			}
			
		}
		
	};
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        messageLayout = inflater.inflate(R.layout.main_tab_02, container, false);
        initView();
		//自定义适配器
		 return messageLayout;
	}

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
                //实例化将要跳转到预定界面的Intent
                Intent intent=new Intent((MainActivity)getActivity(),GotoOrderActivity.class);
                switch(postion)
                {
                    //篮球预约
                    case 1:
                        intent.putExtra("index", 1);
                        startActivity(intent);
                        break;
                    //游泳
                    case 2:
                        intent.putExtra("index", 2);
                        startActivity(intent);
                        break;
                    //台球
                    case 3:
                        intent.putExtra("index", 3);
                        startActivity(intent);
                        break;
                    //羽毛球
                    case 4:
                        intent.putExtra("index", 4);
                        startActivity(intent);
                        break;
                    //乒乓球
                    case 5:
                        intent.putExtra("index", 5);
                        startActivity(intent);
                        break;
                }
            }
        });
        //刷新的监听
        mListView.setonRefreshListener(new OnRefreshListener() {

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
                        RefreshVenueInfo.doRefreshVenueInfo(UserInfo.httpClient, parentHandler);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        myAdapter.notifyDataSetChanged();
                        mListView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
    }

    /*自定义适配器*/
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
            ViewHolder viewHolder = null;
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




