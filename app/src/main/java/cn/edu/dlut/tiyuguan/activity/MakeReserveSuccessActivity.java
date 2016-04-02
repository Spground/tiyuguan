package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.bean.RemindBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.RemindBeanDao;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

public class MakeReserveSuccessActivity extends BaseUi implements BaseUi.AsyncLoadDataFromDBCallback {
    private TextView orderId;
    private String orderIdStr;
    private RemindBeanDao remindBeanDao;
    private DaoSession daoSession;

    private Button goHomeBtn, continueMakeReserveBtn, addRemindBtn;

    private static long DEFAULT_ADVANCE = 30 * 60;//默认的提前量 30分钟

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reserve_success);
        initActionBar("预定成功");
        initView();
        orderIdStr = getIntent().getStringExtra("OrderId");
        orderId.setText("订单号: " + orderIdStr);
        asynLoadDataFromDB(MakeReserveSuccessActivity.this, NameConstant.dbName);
    }

    private void initView() {

        orderId = (TextView) findViewById(R.id.id_make_reserve_success_orderid_text_view);
        goHomeBtn = (Button) findViewById(R.id.id_make_reserve_success_gohome_btn);
        continueMakeReserveBtn = (Button) findViewById(R.id.id_make_reserve_success_continue_make_reserve_btn);
        addRemindBtn = (Button) findViewById(R.id.addRemindBtn);

        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeReserveSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        continueMakeReserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeReserveSuccessActivity.this, MakeReserveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        addRemindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remindBeanDao.insert(new RemindBean(null, DEFAULT_ADVANCE, MakeReserveSuccessActivity.this.orderIdStr)) > 0) {
                    ToastUtil.showSuccessToast(MakeReserveSuccessActivity.this.getApplicationContext(), "添加提醒成功");
                    MakeReserveSuccessActivity.this.addRemindBtn.setEnabled(false);
                    MakeReserveSuccessActivity.this.addRemindBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });
    }

    @Override
    public void asyncLoadDataFromDBCompleted(SQLiteDatabase database) {
        daoSession = new DaoMaster(database).newSession();
        remindBeanDao = daoSession.getRemindBeanDao();
    }
}
