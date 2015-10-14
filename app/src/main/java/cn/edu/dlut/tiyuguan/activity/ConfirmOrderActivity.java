package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.model.Sport;

public class ConfirmOrderActivity extends BaseUi {

    private String venues_id;
    private String location_id;
    private String date;
    private String time_duration;

    private TextView venus_nameTextView,location_idTextView,dateTextView,time_durationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initActionBar("确认订单");

        Intent intent = getIntent();
        venues_id = intent.getStringExtra("venues_id");
        location_id = intent.getStringExtra("location_id");
        date = intent.getStringExtra("date");
        time_duration = intent.getStringExtra("time_duration");

        initView();

    }

    private void initView(){
        venus_nameTextView = (TextView)findViewById(R.id.id_confirmorder_venues_name_textview);
        location_idTextView = (TextView)findViewById(R.id.id_confirmorder_location_id_textview);
        dateTextView = (TextView)findViewById(R.id.id_confirmorder_date_textview);
        time_durationTextView = (TextView)findViewById(R.id.id_confirmorder_time_duration_textview);

        venus_nameTextView.setText(Sport.getVenuesName(Integer.valueOf(venues_id)));
        location_idTextView.setText(location_id);
        dateTextView.setText(date);
        time_durationTextView.setText(time_duration);

    }
}
