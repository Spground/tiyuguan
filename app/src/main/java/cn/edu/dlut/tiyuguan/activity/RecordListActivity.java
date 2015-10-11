package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseService;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.service.QueryRecordService;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

/**
 * 用户查看预约记录
 */
public class RecordListActivity extends BaseUi {

    private MyListView myListView;
    private MyAdapter myAdapter;

    private LinkedHashMap<String,Record> recordList;
    private ArrayList<Record> dataSet;

    int[] drawableIds = {R.drawable.bask,R.drawable.yumao,
            R.drawable.ping,R.drawable.swim,R.drawable.taiqiu};
    private boolean isRegister = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**得到用户的预定列表**/
        if(BaseAuth.isLogin()){
            recordList = BaseAuth.getUser().getRecordMap();
            dataSet = convertDataSet(recordList);
        }
        setContentView(R.layout.activity_record_list);
        /**初始化actionBar**/
        initActionBar("预约订单");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isRegister){
            EventBus.getDefault().register(this);
            isRegister = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    /**init view**/
    private void init(){
        myListView = (MyListView)findViewById(R.id.id_activity_record_list_listview);
        myAdapter = new MyAdapter();
        myListView.setAdapter(myAdapter);
        myListView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**刷新用户的预约订单**/
                Intent intentQueryRecord = new Intent(RecordListActivity.this,QueryRecordService.class);
                intentQueryRecord.setAction(QueryRecordService.NAME + BaseService.ACTION_START );
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                String endTime = format.format(now) + "240000";
                String startTime = AppUtil.getBeforeTime(3,format,now) + "000000";
                AppUtil.debugV("====TAG====","startTime:" + startTime + "endTime:" + endTime);
                intentQueryRecord.putExtra("queryUrl", NameConstant.api.queryUserRecord + "?userId=" + BaseAuth.getUser().getUserId()+"&startTime=" + startTime + "&endTime=" + endTime);
                startService(intentQueryRecord);
            }
        });
    }

    /**自定义ListView适配器内部类**/
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if(dataSet != null){
                return dataSet.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(view == null){
                view = RecordListActivity.this.getLayoutInflater().inflate(R.layout.activity_record_list_listview_item,null);
                viewHolder = new ViewHolder();

                viewHolder.venuesImageView = (ImageView)view.findViewById(R.id.id_activity_record_list_listview_item_veues_image_view);
                viewHolder.recordNumberTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_recordnumber);
                viewHolder.venuesNameTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_name);
                viewHolder.venuesLocationTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_location);
                viewHolder.recordTimePeriodTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_record_time_period);

                view.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)view.getTag();
            }

            /**fulfill data**/
            AppUtil.debugV("====TAG====","venues id " + dataSet.get(i).getVenuesId());
            viewHolder.venuesImageView.setImageResource(drawableIds[Sport.getVenuesId(dataSet.get(i).getVenuesName()) - 1]);
            viewHolder.recordNumberTextView.setText(dataSet.get(i).getRecordId());
            viewHolder.venuesNameTextView.setText(dataSet.get(i).getVenuesName());
            viewHolder.venuesLocationTextView.setText(dataSet.get(i).getLocation());
            viewHolder.recordTimePeriodTextView.setText(dataSet.get(i).getStartTime() + "至" + dataSet.get(i).getEndTime());

            return view;
        }
    }

    static class ViewHolder{
        ImageView venuesImageView;

        TextView recordNumberTextView;
        TextView venuesNameTextView;
        TextView venuesLocationTextView;
        TextView recordTimePeriodTextView;

    }

    /**将LinkedList转为ArrayList**/
    private ArrayList<Record> convertDataSet(LinkedHashMap<String,Record> recordList){
        ArrayList<Record> dataSet = new ArrayList<Record>();
        if(recordList == null)
            return null;
        for(String key : recordList.keySet()){
            dataSet.add(recordList.get(key));
        }
        return dataSet;
    }

    /**刷新完成 EventBus回调的方法**/
    public void onEventMainThread(RefreshCompletedEvent refreshCompletedEvent){
        AppUtil.debugV("====TAG====", "RecordlistActivity的刷新完成回调" );
        dataSet = convertDataSet(BaseAuth.getUser().getRecordMap());
        myAdapter.notifyDataSetChanged();
        this.hideProgressDlg();
        myListView.onRefreshComplete();
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(NetworkErrorEvent errorEvent){
        AppUtil.debugV("====TAG====", "RecordlistActivity的网络错误回调" );
        ToastUtil.showErrorToast(this, "网络未连接或出现错误！");
        this.hideProgressDlg();
        myListView.onRefreshComplete();
    }

}
