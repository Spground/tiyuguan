package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.adapterview.MyGridView;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Location;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.task.QueryLocationInfoTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

public class MakeReserveActivity extends BaseUi {

    private TextView dateTextView,timeTextView;
    private TextView veneusNameTextView;

    private DatePickerDialog datePickDlg;
    private TimePickerDialog timePickDlg;

    private Button queryBtn;

    private MyGridView myGridView;
    private MyGridViewAdapter myGridViewAdapter;
    private HashMap<String,Location> dataSet;

    private int venues_id;

    private boolean register = false;

    //location id tag key
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        setContentView(R.layout.activity_make_reserve);
        venues_id = getIntent().getIntExtra("venues_id",1);
        AppUtil.debugV("====TAG====","选择下订单的跳转Index" + venues_id);
        initActionBar("下单");
        init();
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

    /**初始化各种控件**/
    private void init(){

        initTimeAndDatePicker();

        /**set the name of venues**/
        veneusNameTextView = (TextView)findViewById(R.id.id_makereserve_venuesname_textview);
        veneusNameTextView.setText(Sport.getInstance().getVenuesName(venues_id));

        dateTextView = (TextView)findViewById(R.id.id_choosedate_text_view);
        timeTextView = (TextView)findViewById(R.id.id_choosetime_text_view);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickDlg.show(getFragmentManager(),"DatePickDialog");
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                timePickDlg.show(getFragmentManager(),"TimePickDialog");
            }
        });

        //set initial value
        Date date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");

        dateTextView.setText(simpleDateFormat1.format(date));
        timeTextView.setText(simpleDateFormat2.format(date));

        queryBtn = (Button)findViewById(R.id.id_querylocation_query_button);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = dateTextView.getText().toString().replace("-","").trim();
                String time = timeTextView.getText().toString().replace(":","").trim();
                AppUtil.debugV("====TAG====","day" + day + "time" + time);
                time += "00";
                showProgressDlg();
                queryInvalidLocation(day, time);
            }
        });

        initGridView();
    }

    /**init dlg**/
    private void initTimeAndDatePicker(){
        Calendar calendar = Calendar.getInstance();

        datePickDlg = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                if(verifySelectedDate(year,month,day))
                    dateTextView.setText(year + "-" + (month + 1 ) + "-" + day);
                else{
                    ToastUtil.showInfoToast(MakeReserveActivity.this,"选择的日期不可用，请重新选择！");
                }
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        timePickDlg = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                timeTextView.setText( (hour == 0 ? "00" : hour) + ":" + (minute == 0 ? "00" : minute));
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
    }

    /**init the adapter**/
    private void initGridView(){
        myGridView = (MyGridView)findViewById(R.id.id_makereserve_gridview);
        myGridViewAdapter = new MyGridViewAdapter();
        myGridView.setAdapter(myGridViewAdapter);
        myGridView.setSelector(R.color.gray);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the location id
                String locationId = (String)view.getTag(R.id.id_grid_item_makereserve_locationname_text_view);
                AppUtil.debugV("====TAG====","locationId " + locationId);

                Intent intent = new Intent(MakeReserveActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("venues_id",venues_id + "");
                intent.putExtra("location_id",locationId);
                intent.putExtra("date",dateTextView.getText().toString());
                intent.putExtra("time_duration",timeTextView.getText().toString() + "- 22:00");

                startActivity(intent);

            }
        });
    }

    /**验证选择的日期是否可用**/
    private boolean verifySelectedDate(int year,int month,int day ){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        Date now = new Date();
        Date selectedDate = new Date();
        selectedDate.setTime(calendar.getTimeInMillis());
        long differDays = 0;
        try {
            differDays = AppUtil.getDaysBetweenDate(selectedDate,now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(differDays <= 2 && differDays >= 0){
            return true;
        }
        return false;
    }
    /**GridView的适配器**/
    class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            String locationNum = "0";
            if(Sport.getInstance().getVenuesHashMap() != null){
                locationNum = Sport.getInstance().getVenuesHashMap().get(venues_id + "").getLocationNum();
            }
            return Integer.valueOf(locationNum.trim());
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = MakeReserveActivity.this.getLayoutInflater().inflate(R.layout.grid_item_view_makereserve, null);
                viewHolder.locationNameTextView = (TextView)convertView.findViewById(R.id.id_grid_item_makereserve_locationname_text_view);
                convertView.setTag(viewHolder);
                //set location id tag
                convertView.setTag(R.id.id_grid_item_makereserve_locationname_text_view,(position + 1) + "");
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.locationNameTextView.setText((position + 1) + "");
            viewHolder.locationNameTextView.setBackgroundColor(getResources().getColor(R.color.main_color));
            convertView.setClickable(false);
            //判断是否可以用
            if(dataSet != null){
                String valid = dataSet.get((position + 1) + "") == null ? "true":(dataSet.get((position + 1) + "").getValid());
                //invalid location
                if(valid != null && valid.equals("false")){
                    AppUtil.debugV("====TAG====","有Location不可用");
                    viewHolder.locationNameTextView.setText("不可用");
                    viewHolder.locationNameTextView.setBackgroundColor(getResources().getColor(R.color.gray));
                    convertView.setClickable(true);
                }
            }
            return convertView;
        }
    }

    static class ViewHolder{
        public TextView locationNameTextView;
    }

    /**update dataSet & refresh view**/
    private void updateDataSet(){
        if(Sport.getInstance().getVenuesHashMap() != null){
            dataSet = Sport.getInstance().getVenuesHashMap().get(venues_id + "").getLocationMap();
            myGridViewAdapter.notifyDataSetChanged();
            AppUtil.debugV("====TAG====","updateDataSet() invoked!");
        }
        AppUtil.debugV("====TAG====","getVenuesHashMap() == null!" + ((Sport.getInstance().getVenuesHashMap() == null)?"null":"not null"));
    }

    /**刷新完成 EventBus回调的方法**/
    public void onEventMainThread(RefreshCompletedEvent refreshCompletedEvent){
        AppUtil.debugV("=====TAG=====", "Location数据刷新完成");
        hideProgressDlg();
        updateDataSet();
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(NetworkErrorEvent networkErrorEvent){
        AppUtil.debugV("=====TAG=====", "网络错误");
        ToastUtil.showInfoToast(MakeReserveActivity.this,"网络错误！");
        hideProgressDlg();
        updateDataSet();
    }

    /**查询数据**/
    private void queryInvalidLocation(String queryDay,String queryTime){
        String url = NameConstant.api.queryInvalidLocationInfo + "?venuesId=" + venues_id + "&queryDay=" + queryDay + "&queryTime=" + queryTime;
        AppUtil.debugV("====queryInvalidLocation URL====",url);
        BaseTaskPool.getInstance().addTask(new QueryLocationInfoTask(url, String.valueOf(venues_id)));
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
