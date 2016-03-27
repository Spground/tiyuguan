package cn.edu.dlut.tiyuguan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.adapterview.MyListView;
import cn.edu.dlut.tiyuguan.base.BaseUi;

public class FitAdviceActivity extends BaseUi {

    ListView mListView;
    String[] dataSet = new String[]{"【史上最强健身攻略】兄弟你该健身了，说真的！", "运动前必做的准备运动", "【明晚】西山篮球约吗？"};
    int[] attachImageResIDs = new int[]{R.drawable.splash01, R.drawable.fuji_ex , R.drawable.sunny};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit__advice);
        initActionBar("运动指南");
        init();
    }

    private void init() {
        this.mListView = (MyListView)findViewById(R.id.fit_advice_listview);
        this.mListView.setAdapter(new MAdapter(this.dataSet));
    }

    class MAdapter extends BaseAdapter {
        String[] dataSet;
        MAdapter(String[] dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public int getCount() {
            return this.dataSet.length;
        }

        @Override
        public Object getItem(int position) {
            return this.dataSet[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = View.inflate(FitAdviceActivity.this, R.layout.listview_fit_advice_item, null);
            }
            ((TextView)convertView.findViewById(R.id.title)).setText(this.dataSet[position]);
            ((ImageView)convertView.findViewById(R.id.attachImage)).setImageResource(FitAdviceActivity.this.attachImageResIDs[position]);
            return convertView;
        }
    }

}
