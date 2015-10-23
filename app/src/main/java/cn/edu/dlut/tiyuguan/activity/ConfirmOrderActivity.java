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
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.task.SubmitOrderTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

public class ConfirmOrderActivity extends BaseUi {

    private String venues_id;
    private String location_id;
    private String date;

    private int duration;//时长

    private Date startTime,endTime;

    private TextView venus_nameTextView,
            location_idTextView,
            dateTextView,
            time_periodTextView,
            total_amountTextView,
            time_durationTextView;

    private Button submitBtn,cancelBtn;

    private boolean register = false;

    private SimpleDateFormat HHmmDateFormate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        HHmmDateFormate = new SimpleDateFormat("HH:mm");
        initActionBar("确认订单");
        Intent intent = getIntent();
        venues_id = intent.getStringExtra("venues_id");
        location_id = intent.getStringExtra("location_id");
        date = intent.getStringExtra("date");
        duration = intent.getIntExtra("time_duration", 0);

        startTime = (Date)intent.getSerializableExtra("start_time");
        endTime = (Date)intent.getSerializableExtra("end_time");
        AppUtil.debugV("====TAG===",(startTime == null || endTime == null) ? "null":"not null");
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

    private void initView(){
        venus_nameTextView = (TextView)findViewById(R.id.id_confirmorder_venues_name_textview);
        location_idTextView = (TextView)findViewById(R.id.id_confirmorder_location_id_textview);
        dateTextView = (TextView)findViewById(R.id.id_confirmorder_date_textview);
        time_periodTextView = (TextView)findViewById(R.id.id_confirmorder_time_period_textview);
        time_durationTextView =  (TextView)findViewById(R.id.id_confirmorder_time_duration_textview);
        total_amountTextView = (TextView)findViewById(R.id.id_confirmorder_total_amount_text_view);

        venus_nameTextView.setText(Sport.getVenuesName(Integer.valueOf(venues_id)));
        location_idTextView.setText(location_id);
        dateTextView.setText(date);
        //set time period
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        time_periodTextView.setText(simpleDateFormat.format(startTime) + " - " + simpleDateFormat.format(endTime));
        time_durationTextView.setText(duration + "小时");

        //set total amount money
        int price = -1;
        if(Sport.getInstance().getVenuesHashMap() != null){
            String charge = Sport.getInstance().getVenuesHashMap().get(venues_id).getVenuesCharge().trim();
            price = Integer.valueOf(charge);
        }
        total_amountTextView.setText(duration + " * " + price + "元/小时 = " + (duration * price) + " 元");

        //init button
        submitBtn = (Button)findViewById(R.id.id_confirmorder_submit_button);
        cancelBtn = (Button)findViewById(R.id.id_confirmorder_cancel_button);
        submitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String url = NameConstant.api.makeReserve
                        + "?userId=" + BaseAuth.getUser().getUserId()
                        + "&venuesId=" + venues_id
                        + "&location=" + location_id
                        + "&startTime=" + (date.replace("-", "") + HHmmDateFormate.format(startTime).replace(":","").trim() + "00")
                        + "&endTime=" + (date.replace("-","") + (HHmmDateFormate.format(endTime).replace(":","").trim() + "00"));
                AppUtil.debugV("提交订单的URL",url);
                submitOrder(url);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ConfirmOrderActivity.this.finish();
            }
        });

    }

    private void submitOrder(String url){
        showProgressDlg();
        BaseTaskPool.getInstance().addTask(new SubmitOrderTask(url));
    }


    /**订单完成 EventBus回调的方法**/
    public void onEventMainThread(OrderSuccessEvent orderSuccessEvent){
        AppUtil.debugV("=====TAG=====", "订单完成 订单号" + orderSuccessEvent.getOrderId());
        hideProgressDlg();
        Toast.makeText(getApplicationContext(),"预定成功！订单号为：" + orderSuccessEvent.getOrderId(),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this,MakeReserveSuccessActivity.class);
        intent.putExtra("OrderId",orderSuccessEvent.getOrderId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(NetworkErrorEvent networkErrorEvent){
        AppUtil.debugV("=====TAG=====", "网络错误");
        ToastUtil.showInfoToast(ConfirmOrderActivity.this, "网络错误！");
        hideProgressDlg();
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(ExceptionErrorEvent exceptionErrorEvent){
        AppUtil.debugV("=====TAG=====", "异常错误");
        ToastUtil.showInfoToast(getApplicationContext(), "创建订单出现错误，请重新再试！");
        hideProgressDlg();
        this.finish();
    }

    public void registerEventBus(){
        if(!this.register){
            EventBus.getDefault().register(this);
            register = true;
        }
    }

    public void unregisterEventBus(){
        if(this.register){
            EventBus.getDefault().unregister(this);
            register = false;
        }
    }
}
