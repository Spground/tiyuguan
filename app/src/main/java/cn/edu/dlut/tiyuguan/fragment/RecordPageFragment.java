package cn.edu.dlut.tiyuguan.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.activity.MainActivity;
import cn.edu.dlut.tiyuguan.activity.RecordListActivity;
import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.DeleteOrderEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshRecordListViewEvent;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.task.DeleteOrderTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by wujie on 2015/12/2.
 */
public class RecordPageFragment extends Fragment
        implements MyListView.OnLoadMoreListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ViewGroup rootView;

    private MyListView myListView;
    private MyAdapter myAdapter;

    private ArrayList<Record> dataSet = new ArrayList<>();
    private ArrayList<Record> tempDataSet = new ArrayList<>();
    private RefreshRecordCallBack refreshCallBack;
    private boolean isRegister = false;
    private int pageSize = 5;
    /**
     * 当前订单的类型
     */
    private RECORD_TYPE record_type;
    public final static String RECORD_TYPE_EXTRAL = "record_type";

    /**
     * callback to refresh data set
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.record_type = (RECORD_TYPE) getArguments().getSerializable(RECORD_TYPE_EXTRAL);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            this.refreshCallBack = (RefreshRecordCallBack) activity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassCastException(activity.toString() + "must implements callback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record_page, container, false);
        return this.rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**得到用户的订单列表**/
        if (BaseAuth.isLogin()) {
            Map<String, Record> recordMap = BaseAuth.getUser().getRecordMap();
            AppUtil.map2List(this.tempDataSet, recordMap, true, record_type);
        }

        /**默认显示pageSize**/
        pageSize = this.tempDataSet.size() >= 5 ? 5 : this.tempDataSet.size();
        for (int i = 0; i < pageSize; i++)
            this.dataSet.add(tempDataSet.get(i));
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRegister) {
            EventBus.getDefault().register(this);
            isRegister = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRegister) {
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegister) {
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    /**
     * init view
     **/
    private void init() {
        myListView = (MyListView) this.rootView.findViewById(R.id.id_record_listview);
        myListView.setEnablePullUpLoadMore(true);
        myListView.setOnLoadMoreCallback(this);
        myAdapter = new MyAdapter();
        myListView.setAdapter(myAdapter);
        myListView.getHeaderView().setBackgroundColor(getResources().getColor(R.color.light_gray));
        myListView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**刷新用户的预约订单**/
                //TODO:待改进
                if (RECORD_TYPE.CURRENT_RECORD == record_type)
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
        myListView.setOnItemClickListener(this);
        myListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String orderId = ((ViewHolder) view.getTag()).orderId;
        if (this.record_type == RECORD_TYPE.CURRENT_RECORD) {
            android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(this.getActivity())
                    .setTitle("取消预约")
                    .setMessage("确认取消此次预约？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            dlg.show();
        } else {
            android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(this.getActivity())
                    .setTitle("删除此订单")
                    .setMessage("确认删除此订单记录吗？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((RecordListActivity)RecordPageFragment.this.getActivity()).showProgressDlg();
                            DeleteOrderTask deleteOrderTask = new DeleteOrderTask(orderId);
                            BaseTaskPool.getInstance().addTask(deleteOrderTask);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            dlg.show();
        }
        return true;
    }

    /**
     * 自定义ListView适配器内部类
     **/
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (dataSet != null) {
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
            if (view == null) {
                view = getActivity().getLayoutInflater().
                        inflate(R.layout.activity_record_list_listview_item, null);
                viewHolder = new ViewHolder();

                viewHolder.venuesImageView = (ImageView) view.findViewById(R.id.id_activity_record_list_listview_item_veues_image_view);
                viewHolder.recordNumberTextView = (TextView) view.findViewById(R.id.id_activity_record_list_listview_item_recordnumber);
                viewHolder.venuesNameTextView = (TextView) view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_name);
                viewHolder.venuesLocationIdTextView = (TextView) view.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_location);
                viewHolder.recordTimePeriodTextView = (TextView) view.findViewById(R.id.id_activity_record_list_listview_item_record_time_period);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            if (dataSet == null)
                return view;
            /**fulfill data**/
            AppUtil.debugV("====TAG====", "dataSet " + dataSet);
            AppUtil.debugV("====TAG====", "venues id " + dataSet.get(i).getVenuesId());
            AppUtil.debugV("====TAG====", "venues name " + dataSet.get(i).getVenuesName());

            viewHolder.venuesImageView.setImageResource(AppUtil.getDrawableResId(dataSet.get(i).getVenuesName()));
            viewHolder.recordNumberTextView.setText(dataSet.get(i).getRecordId());
            viewHolder.venuesNameTextView.setText(dataSet.get(i).getVenuesName());
            viewHolder.venuesLocationIdTextView.setText(String.format("%d", dataSet.get(i).getLocationId()));
            viewHolder.recordTimePeriodTextView.setText(
                    String.format("%s 至 %s", AppUtil.timeStamp2timeStr(dataSet.get(i).getStartTime(), null),
                            AppUtil.timeStamp2timeStr(dataSet.get(i).getEndTime(), null)
                    ));
            viewHolder.orderId = dataSet.get(i).getRecordId();
            return view;
        }
    }

    static class ViewHolder {
        ImageView venuesImageView;
        TextView recordNumberTextView;
        TextView venuesNameTextView;
        TextView venuesLocationIdTextView;
        TextView recordTimePeriodTextView;
        String orderId;
    }

    /**
     * 刷新完成 EventBus回调的方法
     **/
    public void onEventMainThread(RefreshRecordListViewEvent event) {
        AppUtil.debugV("====TAG====", "RecordPageFragment的刷新完成回调");
        refreshListView();
    }

    /**
     * 网络错误 EventBus回调的方法
     **/
    public void onEventMainThread(NetworkErrorEvent errorEvent) {
        ToastUtil.showErrorToast(getActivity(), "网络错误！");
        ((RecordListActivity) getActivity()).hideProgressDlg();
    }

    /**
     * 网络错误 EventBus回调的方法
     **/
    public void onEventMainThread(DeleteOrderEvent deleteOrderEvent) {
        String result = deleteOrderEvent.getResult();
        if (result.trim().equals("success")) {
            BaseAuth.getUser().getRecordMap().remove(deleteOrderEvent.getRecord());
            for (int i = 0; i < this.dataSet.size(); i++) {
                if (this.dataSet.get(i).getRecordId().
                        equals(deleteOrderEvent.getRecord().getRecordId())) {
                    this.dataSet.remove(i);
                    break;
                }
            }
            refreshListView();
            ToastUtil.showSuccessToast(this.getActivity().getApplicationContext(), "删除成功");
        } else {
            //// TODO: 2016/4/1
            refreshListView();
            ToastUtil.showSuccessToast(this.getActivity().getApplicationContext(), "删除失败");
        }
        ((RecordListActivity) getActivity()).hideProgressDlg();
    }

    public void refreshListView() {
        myAdapter.notifyDataSetChanged();
        myListView.onRefreshComplete();
    }

    @Override
    public void onLoadMore() {
        final Handler mHandler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final ArrayList<Record> result = new ArrayList<>();
                if (dataSet.size() + pageSize <= tempDataSet.size()) {
                    result.addAll(dataSet);
                    int i = 0;
                    while (i <= pageSize) {
                        result.add(tempDataSet.get(i + dataSet.size() - 1));
                        i++;
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.size() == 0) {
                            myListView.noMoreData();
                        } else {
                            dataSet.clear();
                            dataSet.addAll(result);
                            myAdapter.notifyDataSetChanged();
                            myListView.loadMoreComplete();
                        }
                    }
                });
            }
        });
        thread.start();
    }
}
