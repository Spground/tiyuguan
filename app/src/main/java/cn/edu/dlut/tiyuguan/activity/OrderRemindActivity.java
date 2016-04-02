package cn.edu.dlut.tiyuguan.activity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.bean.RemindBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.RemindBeanDao;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.util.AppUtil;

public class OrderRemindActivity extends BaseUi implements BaseUi.AsyncLoadDataFromDBCallback,
        AdapterView.OnItemLongClickListener, View.OnClickListener {
    ListView mListView;
    MAdapter mAdapter;
    List<RemindBean> dataSet;
    RemindBeanDao remindBeanDao;
    DaoSession daoSession;
    Button addRemindBtn;

    MHandler mHandler = new MHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_remind);
        initActionBar("预约提醒");
        init();
        showProgressDlg();
        asynLoadDataFromDB(this, NameConstant.dbName);
        mHandler.sendMessageDelayed(Message.obtain(), 900L);
    }

    private void init() {
        this.mListView = (ListView)findViewById(R.id.order_remind_listview);
        this.addRemindBtn = (Button)findViewById(R.id.addRemindBtn);
        this.addRemindBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addRemindBtn :
                RemindBean remindBean = new RemindBean(null, 3000L, "123456");
                remindBeanDao.insert(remindBean);
                this.dataSet.clear();
                this.dataSet.addAll(remindBeanDao.loadAll());
                this.mAdapter.notifyDataSetChanged();
                break;
        }
    }

    //TODO 待使用线程池优化
    @Override
    public void asyncLoadDataFromDBCompleted(SQLiteDatabase database) {
        daoSession = new DaoMaster(database).newSession();
        remindBeanDao = daoSession.getRemindBeanDao();
        initListView();
        hideProgressDlg();
    }

    private void initListView() {
        this.dataSet = new ArrayList<>();
        this.dataSet.addAll(remindBeanDao.loadAll());
        mAdapter = new MAdapter();
        this.mListView.setAdapter(mAdapter);
        this.mListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, final long l) {
        android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(this)
                .setTitle("删除此提醒")
                .setMessage("确认删除此预约提醒吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderRemindActivity.this.dataSet.remove(view.getId());
                        remindBeanDao.deleteByKey(((ViewHolder)view.getTag()).remindId);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dlg.show();
        return true;
    }

    class MAdapter extends BaseAdapter {
        MAdapter() {
        }

        @Override
        public int getCount() {
            return OrderRemindActivity.this.dataSet.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return OrderRemindActivity.this.dataSet.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = View.inflate(OrderRemindActivity.this ,R.layout.listview_orderremind_item, null);
                viewHolder = new ViewHolder();
                viewHolder.orderIdTextView = (TextView)convertView.findViewById(R.id.id_activity_record_list_listview_item_recordnumber);
                viewHolder.venuesNameTextView = (TextView) convertView.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_name);
                viewHolder.venuesLocationTextView = (TextView) convertView.findViewById(R.id.id_activity_record_list_listview_item_text_view_venues_location);
                viewHolder.orderTimePeriodTextView = (TextView) convertView.findViewById(R.id.id_activity_record_list_listview_item_record_time_period);
                viewHolder.leftTimeTextView = (TextView)convertView.findViewById(R.id.leftTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            Record order = BaseAuth.getUser().getRecordMap().get(OrderRemindActivity.this.dataSet.get(position).getOrderId());
            if(order != null) {
                viewHolder.orderIdTextView.setText(order.getRecordId());
                viewHolder.venuesLocationTextView.setText(String.format("%d", order.getLocationId()));
                viewHolder.orderTimePeriodTextView.setText(
                        String.format("%s 至 %s", AppUtil.timeStamp2timeStr(order.getStartTime(), null),
                                AppUtil.timeStamp2timeStr(order.getEndTime(), null))
                );
                long leftSeconds = (order.getStartTime() - System.currentTimeMillis() / 1000);
                int leftHours = (int)(leftSeconds / 3600);
                int leftMins = (int)((leftSeconds - leftHours * 3600) / 60);
                leftSeconds = leftSeconds - (leftHours * 3600 + leftMins * 60);
                viewHolder.leftTimeTextView.setText(String.format("倒计时: %02d:%02d:%02d",
                        leftHours, leftMins, leftSeconds
                        ));
                viewHolder.venuesNameTextView.setText(order.getVenuesName());
            }
            viewHolder.remindId = OrderRemindActivity.this.dataSet.get(position).getId();
            convertView.setId(position);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView orderIdTextView;
        TextView venuesNameTextView;
        TextView venuesLocationTextView;
        TextView orderTimePeriodTextView;
        TextView leftTimeTextView;
        Long remindId;
    }

    static class MHandler extends Handler {
        WeakReference<OrderRemindActivity> ref;

        MHandler(OrderRemindActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OrderRemindActivity activity = ref.get();
            if(activity != null) {
                activity.mAdapter.notifyDataSetChanged();
                activity.mHandler.sendMessageDelayed(Message.obtain(), 900L);
            }
        }
    }
}


