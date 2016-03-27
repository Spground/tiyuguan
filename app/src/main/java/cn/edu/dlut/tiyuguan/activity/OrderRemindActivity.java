package cn.edu.dlut.tiyuguan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;

public class OrderRemindActivity extends BaseUi {
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_remind);
        initActionBar("预约提醒");
        init();
    }

    private void init() {
        this.mListView = (ListView)findViewById(R.id.order_remind_listview);
        this.mListView.setAdapter(new MAdapter());
    }

    class MAdapter extends BaseAdapter {
        MAdapter() {

        }

        MAdapter(String[] orders) {

        }
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = View.inflate(OrderRemindActivity.this ,R.layout.listview_orderremind_item, null);
            return convertView;
        }
    }
}


