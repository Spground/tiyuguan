package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.ExceptionErrorEvent;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.OrderSuccessEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.task.SubmitOrderTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

public class ConfirmOrderActivity extends BaseUi {

    private int venues_id;
    private int location_id;
    private int duration;//时长
    private long startTime, endTime;
    private String date;
    private boolean register = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initActionBar("确认订单");
        Intent intent = getIntent();
        venues_id = intent.getIntExtra("venues_id", -1);
        location_id = intent.getIntExtra("location_id", -1);
        date = intent.getStringExtra("date");
        duration = intent.getIntExtra("time_duration", 0);

        startTime = intent.getLongExtra("start_time", -1);
        endTime = intent.getLongExtra("end_time", -1);
        AppUtil.debugV("====TAG===", "startTime " + startTime + " endTime:" + endTime);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerEventBus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterEventBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    private void initView() {
        TextView venus_nameTextView,
                location_idTextView,
                dateTextView,
                time_periodTextView,
                total_amountTextView,
                time_durationTextView;
        Button submitBtn, cancelBtn;
        venus_nameTextView = (TextView) findViewById(R.id.id_confirmorder_venues_name_textview);
        location_idTextView = (TextView) findViewById(R.id.id_confirmorder_location_id_textview);
        dateTextView = (TextView) findViewById(R.id.id_confirmorder_date_textview);
        time_periodTextView = (TextView) findViewById(R.id.id_confirmorder_time_period_textview);
        time_durationTextView = (TextView) findViewById(R.id.id_confirmorder_time_duration_textview);
        total_amountTextView = (TextView) findViewById(R.id.id_confirmorder_total_amount_text_view);

        venus_nameTextView.setText(Sport.getInstance().getVenuesHashMap().get(venues_id).getVenuesName());
        location_idTextView.setText(location_id + "");
        dateTextView.setText(date);
        //set time period
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startTimeDate = new Date(startTime * 1000);
        Date endTimeDate = new Date(endTime * 1000);
        time_periodTextView.setText(simpleDateFormat.format(startTimeDate) + " - " + simpleDateFormat.format(endTimeDate));
        time_durationTextView.setText(duration + "小时");

        //set total amount money
        float price = -1;
        if (Sport.getInstance().getVenuesHashMap() != null) {
            price = Sport.getInstance().getVenuesHashMap().get(venues_id).getVenuesCharge();
        }
        total_amountTextView.setText(duration + " * " + price + "元/小时 = " + (duration * price) + " 元");

        //init button
        submitBtn = (Button) findViewById(R.id.id_confirmorder_submit_button);
        cancelBtn = (Button) findViewById(R.id.id_confirmorder_cancel_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = NameConstant.api.makeReserve
                        + "?userId=" + BaseAuth.getUser().getUserId()
                        + "&venuesId=" + venues_id
                        + "&locationId=" + location_id
                        + "&startTime=" + startTime
                        + "&endTime=" + endTime ;
                AppUtil.debugV("提交订单的URL", url);
                submitOrder(url);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderActivity.this.finish();
            }
        });

    }

    private void submitOrder(String url) {
        showProgressDlg();
        BaseTaskPool.getInstance().addTask(new SubmitOrderTask(url));
    }


    /**
     * 订单完成 EventBus回调的方法
     **/
    public void onEventMainThread(OrderSuccessEvent orderSuccessEvent) {
        AppUtil.debugV("=====TAG=====", "订单完成 订单号" + orderSuccessEvent.getRecord().getRecordId());
        hideProgressDlg();
        Toast.makeText(getApplicationContext(), "预定成功！订单号为：" + orderSuccessEvent.getRecord().getRecordId(), Toast.LENGTH_LONG).show();
        //新建一个model对象添加
        BaseAuth.getUser().getRecordMap().put(orderSuccessEvent.getRecord().getRecordId(), orderSuccessEvent.getRecord());

        Intent intent = new Intent(this, MakeReserveSuccessActivity.class);
        intent.putExtra("OrderId", orderSuccessEvent.getRecord().getRecordId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ConfirmOrderActivity.this.finish();
    }

    /**
     * 网络错误 EventBus回调的方法
     **/
    public void onEventMainThread(NetworkErrorEvent networkErrorEvent) {
        AppUtil.debugV("=====TAG=====", "网络错误");
        ToastUtil.showInfoToast(ConfirmOrderActivity.this, "网络错误！");
        hideProgressDlg();
    }

    /**
     * 网络错误 EventBus回调的方法
     **/
    public void onEventMainThread(ExceptionErrorEvent exceptionErrorEvent) {
        AppUtil.debugV("=====TAG=====", "异常错误");
        ToastUtil.showInfoToast(getApplicationContext(), "创建订单出现错误，请重新再试！");
        hideProgressDlg();
        this.finish();
    }

    public void registerEventBus() {
        if (!this.register) {
            EventBus.getDefault().register(this);
            register = true;
        }
    }

    public void unregisterEventBus() {
        if (this.register) {
            EventBus.getDefault().unregister(this);
            register = false;
        }
    }
}
