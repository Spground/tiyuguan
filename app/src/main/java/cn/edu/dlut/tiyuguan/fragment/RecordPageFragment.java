package cn.edu.dlut.tiyuguan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.event.RefreshRecordListViewEvent;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by asus on 2015/12/2.
 */
public class RecordPageFragment extends Fragment {
    private ViewGroup rootView;

    private MyListView myListView;
    private MyAdapter myAdapter;


    private ArrayList<Record> dataSet = new ArrayList<>();

    private RefreshRecordCallBack refreshCallBack;
    private boolean isRegister = false;

    /**
     * 当前订单的类型
     */
    private int record_type = 0;

    /**
     * callback to refresh dataset
     */
    public interface RefreshRecordCallBack {
        void onRefreshRecord(RECORD_TYPE recordType, ArrayList<Record> dataSet);
    }

    /**
     * Record type :current unused record、history invalid record
     */
    public enum RECORD_TYPE {
        CURRENT_RECORD,
        HISTORY_RECORD
    }

    public static final int  current_record = 0;
    public static final int history_record = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.record_type = getArguments().getInt("record_type");
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            this.refreshCallBack = (RefreshRecordCallBack)activity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassCastException(activity.toString() + "must implements callback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup)inflater.inflate(R.layout.fragment_record_page, container, false);
        return this.rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**得到用户的预定列表**/
        if(BaseAuth.isLogin()){
            Map<String,Record> recordMap = BaseAuth.getUser().getRecordMap();
            AppUtil.map2List(this.dataSet, recordMap, true, record_type);
        }
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isRegister){
            EventBus.getDefault().register(this);
            isRegister = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    /**init view**/
    private void init(){
        myListView = (MyListView)this.rootView.findViewById(R.id.id_record_listview);
        myAdapter = new MyAdapter();
        myListView.setAdapter(myAdapter);
        myListView.getHeaderView().setBackgroundColor(getResources().getColor(R.color.light_gray));
        myListView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**刷新用户的预约订单**/
                //TODO:待改进
                if(current_record == record_type)
                    RecordPageFragment.this.refreshCallBack.onRefreshRecord(
                            RECORD_TYPE.CURRENT_RECORD,
                            RecordPageFragment.this.dataSet
                    );
                else
                    RecordPageFragment.this.refreshCallBack.onRefreshRecord(
                            RECORD_TYPE.HISTORY_RECORD,
                            RecordPageFragment.this.dataSet
                    );
            }
        });
    }

    /**自定义ListView适配器内部类**/
    class MyAdapter extends BaseAdapter {
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
            ViewHolder viewHolder;
            if(view == null) {
                view = getActivity().getLayoutInflater().
                        inflate(R.layout.activity_record_list_listview_item,null);
                viewHolder = new ViewHolder();

                viewHolder.venuesImageView = (ImageView)view.findViewById(R.id.id_activity_record_list_listview_item_veues_image_view);
                viewHolder.recordNumberTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_recordnumber);
                viewHolder.venuesNameTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_name);
                viewHolder.venuesLocationTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_location);
                viewHolder.recordTimePeriodTextView = (TextView)view.findViewById(R.id.id_activity_record_list_listview_item_record_time_period);

                view.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder)view.getTag();
            }

            if(dataSet == null)
                return view;
            /**fulfill data**/
            AppUtil.debugV("====TAG====","dataSet " + dataSet);
            AppUtil.debugV("====TAG====","venues id " + dataSet.get(i).getVenuesId());
            AppUtil.debugV("====TAG====","venues name " + dataSet.get(i).getVenuesName());

            viewHolder.venuesImageView.setImageResource(AppUtil.getDrawableResId(dataSet.get(i).getVenuesName()));
            viewHolder.recordNumberTextView.setText(dataSet.get(i).getRecordId());
            viewHolder.venuesNameTextView.setText(dataSet.get(i).getVenuesName());
            viewHolder.venuesLocationTextView.setText(dataSet.get(i).getLocation());
            viewHolder.recordTimePeriodTextView.setText(dataSet.get(i).getStartTime() + "至" + dataSet.get(i).getEndTime().substring(11));

            return view;
        }
    }

    static class ViewHolder {
        ImageView venuesImageView;

        TextView recordNumberTextView;
        TextView venuesNameTextView;
        TextView venuesLocationTextView;
        TextView recordTimePeriodTextView;

    }

    /**刷新完成 EventBus回调的方法**/
    public void onEventMainThread(RefreshRecordListViewEvent event) {
        AppUtil.debugV("====TAG====", "RecordPageFragment的刷新完成回调");
        refreshListView();
    }

    public void refreshListView() {
        myAdapter.notifyDataSetChanged();
        myListView.onRefreshComplete();
    }
}
