package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;

public class MakeReserveSuccessActivity extends BaseUi {

    private String orderIdStr;
    private TextView orderId;

    private Button goHomeBtn,continueMakeReserveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reserve_success);
        initActionBar("预定成功");
        initView();
        orderIdStr = getIntent().getStringExtra("OrderId");
        orderId.setText("订单号: " + orderIdStr);
    }

    /****/
    private void initView() {
        orderId = (TextView)findViewById(R.id.id_make_reserve_success_orderid_text_view);
        goHomeBtn = (Button)findViewById(R.id.id_make_reserve_success_gohome_btn);
        continueMakeReserveBtn = (Button)findViewById(R.id.id_make_reserve_success_continue_make_reserve_btn);

        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeReserveSuccessActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        continueMakeReserveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeReserveSuccessActivity.this,MakeReserveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
