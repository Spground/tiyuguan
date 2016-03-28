package cn.edu.dlut.tiyuguan.activity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.bean.OrderBean;
import cn.edu.dlut.tiyuguan.bean.RemindBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.RemindBeanDao;
import cn.edu.dlut.tiyuguan.global.NameConstant;

public class OrderRemindActivity extends BaseUi implements BaseUi.AsyncLoadDataFromDBCallback,
        AdapterView.OnItemLongClickListener, View.OnClickListener {
    ListView mListView;
    MAdapter mAdapter;
    List<RemindBean> dataSet;
    RemindBeanDao remindBeanDao;
    DaoSession daoSession;

    Button addRemindBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_remind);
        initActionBar("预约提醒");
        init();
        showProgressDlg();
        asynLoadDataFromDB(this, NameConstant.dbName);
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
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            OrderBean orderBean = OrderRemindActivity.this.dataSet.get(position).getOrderBean();
            if(orderBean != null) {
                viewHolder.orderIdTextView.setText(orderBean.getOrderId());
                viewHolder.orderTimePeriodTextView.setText(orderBean.getStartTime() + " 至 " + orderBean.getEndTime() );
                viewHolder.remindId = OrderRemindActivity.this.dataSet.get(position).getId();
            }
            convertView.setId(position);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView orderIdTextView;
        TextView venuesNameTextView;
        TextView venuesLocationTextView;
        TextView orderTimePeriodTextView;
        Long remindId;
    }
}


