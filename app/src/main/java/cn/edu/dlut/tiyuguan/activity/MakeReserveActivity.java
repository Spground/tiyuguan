package cn.edu.dlut.tiyuguan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.adapterview.MyGridView;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Location;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.model.Sport;
import cn.edu.dlut.tiyuguan.task.QueryLocationInfoTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import de.greenrobot.event.EventBus;

public class MakeReserveActivity extends BaseUi {

    private TextView dateTextView,timeTextView;

    private DatePickerDialog datePickDlg;
    private TimePickerDialog timePickDlg;

    private Spinner spinner;
    private Button queryBtn;

    private MyGridView myGridView;
    private MyGridViewAdapter myGridViewAdapter;
    private HashMap<String,Location> dataSet;

    private int venues_id;
    private boolean register = false;

    private int duration = 1;//时长

    private Date startTime;//订单的开始时间
    private Date endTime;

    private SimpleDateFormat HHmmDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.debugV("MakeReserveActivity onCreate");
        AppUtil.debugV("====TAG====","选择下订单的跳转Index" + venues_id);
        HHmmDateFormat = new SimpleDateFormat("HH:mm");
        registerEventBus();
        setContentView(R.layout.activity_make_reserve);
        venues_id = getIntent().getIntExtra("venues_id",1);
        initActionBar("下单");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerEventBus();
        queryBtn.performClick();
        AppUtil.debugV("MakeReserveActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterEventBus();
        AppUtil.debugV("MakeReserveActivity onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
        AppUtil.debugV("MakeReserveActivity onDestroy");
    }

    /**初始化各种控件**/
    private void init(){

        initTimeAndDatePicker();

        /**set the name of venues**/
        //TextView venuesNameTextView;
        //venuesNameTextView = (TextView)findViewById(R.id.id_makereserve_venuesname_textview);
        //venuesNameTextView.setText(Sport.getInstance().getVenuesName(venues_id));

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
        //set date's initial value as now
        //set time's initial value as next integral hour
        Date date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        dateTextView.setText(simpleDateFormat1.format(date));
        timeTextView.setText(getNextIntegralHour(date));

        spinner = (Spinner)findViewById(R.id.id_timeduration_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView spinnerItem = (TextView)view;
                spinnerItem.setTextColor(getResources().getColor(R.color.black));
                spinnerItem.setTextSize(16);
                duration = (i + 1);//时长
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        queryBtn = (Button)findViewById(R.id.id_querylocation_query_button);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = dateTextView.getText().toString().replace("-","").trim();
                String queryStartTime = timeTextView.getText().toString().replace(":","").trim();
                String queryEndTime = "";
                //calculate endTime
                try {
                    startTime = HHmmDateFormat.parse(timeTextView.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime.getTime() + (duration * 60 * 60 * 1000));
                    endTime = calendar.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                queryStartTime += "00";
                queryEndTime = HHmmDateFormat.format(endTime).replace(":","").trim() + "00";

                AppUtil.debugV("====TAG====","queryDay" + day + "queryStartTime" + queryStartTime + "queryEndTime" + queryEndTime);
                showProgressDlg();
                queryInvalidLocation(day, queryStartTime,queryEndTime);
            }
        });

        initGridView();
    }

    /**init dlg**/
    private void initTimeAndDatePicker(){
        Date now = new Date();
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
                timeTextView.setText(
                                  (0 <= hour && hour <= 9) ?("0" + hour):(hour)
                                + ":"
                                + ((0 <= minute && minute <= 9)?("0" + minute):minute));
                timePickDlg.setStartTime(hour,minute);

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

        //set highlight & selectable days
        Calendar[] selectedDays = new Calendar[3];
        selectedDays[0] = calendar;
        selectedDays[1] = Calendar.getInstance();
        selectedDays[2] = Calendar.getInstance();
        selectedDays[1].setTimeInMillis(now.getTime() + (1 * 24 * 60 * 60 * 1000));
        selectedDays[2].setTimeInMillis(now.getTime() + (2 * 24 * 60 * 60 * 1000));

        datePickDlg.setSelectableDays(selectedDays);
        datePickDlg.setHighlightedDays(selectedDays);

        timePickDlg.setTitle("选择订单开始的时间");

    }

    /**init the adapter**/
    private void initGridView(){
        myGridView = (MyGridView)findViewById(R.id.id_makereserve_gridview);
        myGridViewAdapter = new MyGridViewAdapter();
        myGridView.setAdapter(myGridViewAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the location id
                String locationId = (String)view.getTag(R.id.id_grid_item_makereserve_locationname_text_view);
                AppUtil.debugV("====TAG====","locationId " + locationId);

                //calculate endTime & startTime
                //
                try {
                    startTime = HHmmDateFormat.parse(timeTextView.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime.getTime() + (duration * 60 * 60 * 1000));
                    endTime = calendar.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                AppUtil.debugV("====TAG===","StartTime:" + startTime.getHours() + ":" + startTime.getMinutes());
                AppUtil.debugV("====TAG===","EndTime:" + endTime.getHours() + ":" + endTime.getMinutes());

                boolean ok = false;
                try {
                    ok = verifySelectedTime(startTime,endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    ok = false;
                }
                if(!ok){
                    ToastUtil.showInfoToast(MakeReserveActivity.this,"你选择的时间段不满足场馆的开闭馆要求，请重新选择！");
                    return;
                }

                Intent intent = new Intent(MakeReserveActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("venues_id",venues_id + "");
                intent.putExtra("location_id",locationId);
                intent.putExtra("date",dateTextView.getText().toString());
                intent.putExtra("start_time",startTime);
                intent.putExtra("end_time",endTime);
                intent.putExtra("time_duration",duration);

                startActivity(intent);

            }
        });
    }

    /***验证时间是否可用**/
    private boolean verifySelectedTime(Date startDate,Date endDate) throws Exception{

        if(endDate.before(startDate))
            return false;

        String openTimeStr = "" ,closeTimeStr = "";
        Date openTime = null,closeTime = null;

        if(Sport.getInstance().getVenuesHashMap() != null){
            openTimeStr = Sport.getInstance().getVenuesHashMap().get(venues_id + "").getOpenTime();
            closeTimeStr = Sport.getInstance().getVenuesHashMap().get(venues_id + "").getCloseTime();
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            openTime = simpleDateFormat.parse(openTimeStr);
            closeTime = simpleDateFormat.parse(closeTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        if(openTime == null || closeTime == null)
            throw new Exception("无法解析场馆的开闭馆时间");
        //比较时间大小
        return openTime.before(startDate) && openTime.before(endDate) && closeTime.after(endDate) && closeTime.after(startDate);
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

        return differDays <= 2 && differDays >= 0;
    }

    /***得到指定日期的下一个整点时刻的HH:mm格式字符串**/
    private String getNextIntegralHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nextHour = (currentHour + 1) % 24;
        String nextIntegralHourStr = (nextHour > 9 ? nextHour:("0" + nextHour)) + ":00";
        return nextIntegralHourStr;
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
            viewHolder.locationNameTextView.setBackgroundResource(R.drawable.selector_location_grid_view_item);
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
        updateDataSet();
        hideProgressDlg();
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(NetworkErrorEvent networkErrorEvent){
        AppUtil.debugV("=====TAG=====", "网络错误");
        ToastUtil.showInfoToast(MakeReserveActivity.this,"网络错误！");
        hideProgressDlg();
        updateDataSet();
    }

    /**查询数据**/
    private void queryInvalidLocation(String queryDay,String queryStartTime,String queryEndTime){
        String url = NameConstant.api.queryInvalidLocationInfo + "?venuesId=" + venues_id + "&queryDay=" + queryDay + "&queryStartTime=" + queryStartTime + "&queryEndTime=" + queryEndTime;
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

    abstract class RecordFilter{
        public abstract void filter(Map<String, Record> recordMap);
    }
}
