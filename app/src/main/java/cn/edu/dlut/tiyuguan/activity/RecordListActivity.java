package cn.edu.dlut.tiyuguan.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseAuth;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.event.NetworkErrorEvent;
import cn.edu.dlut.tiyuguan.event.RefreshCompletedEvent;
import cn.edu.dlut.tiyuguan.event.RefreshRecordListViewEvent;
import cn.edu.dlut.tiyuguan.fragment.RecordPageFragment;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.model.Record;
import cn.edu.dlut.tiyuguan.task.QueryUserOrderRecordTask;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;
import cn.edu.dlut.tiyuguan.widget.VPagerTitle;
import de.greenrobot.event.EventBus;

/**
 * 用户查看预约记录
 */
public class RecordListActivity extends BaseUi implements RecordPageFragment.RefreshRecordCallBack, View.OnClickListener{
    private static final String TAG = "RecordListActivity";

    private static final int NUM_PAGES = 2;
    private ArrayList<Record> dataSet;

    private boolean isRegister = false;

    private ViewPager viewPager;
    private List<VPagerTitle> titles;
    private int[] ids = {R.id.cur_record_title, R.id.his_record_title};
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        /**初始化actionBar**/
        initActionBar("订单记录");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isRegister){
            EventBus.getDefault().register(this);
            isRegister = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegister){
            EventBus.getDefault().unregister(this);
            isRegister = false;
        }
    }

    private void initView() {
        this.viewPager = (ViewPager)findViewById(R.id.id_record_list_viewpager);
        this.viewPager.setAdapter(new RecordPageAdapter(getSupportFragmentManager()));
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != currentIndex) {
                    titles.get(currentIndex).setSelected(false);
                    titles.get(position).setSelected(true);
                    currentIndex = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        titles = new ArrayList<>(2);
        for (int i = 0; i < 2; i++) {
            VPagerTitle title = (VPagerTitle)this.findViewById(ids[i]);
            title.setIndex(i);
            title.setSelected(i == currentIndex);
            title.setOnClickListener(this);
            titles.add(title);
        }
    }
    /**刷新完成 EventBus回调的方法**/
    public void onEventMainThread(RefreshCompletedEvent refreshCompletedEvent) {
        AppUtil.debugV("====TAG====", "RecordListActivity的刷新完成回调");
        AppUtil.map2List(this.dataSet, BaseAuth.getUser().getRecordMap(), true, 0);
        //notify fragment update data
        EventBus.getDefault().post(new RefreshRecordListViewEvent());
        this.hideProgressDlg();
    }
    /**网络错误 EventBus回调的方法**/
    public void onEventMainThread(NetworkErrorEvent errorEvent) {
        AppUtil.debugV("====TAG====", "RecordListActivity的网络错误回调" );
        ToastUtil.showErrorToast(this, "网络未连接或出现错误！");
        this.hideProgressDlg();
    }

    @Override
    public void onClick(View v) {
        int index = ((VPagerTitle)v).getIndex();
        if (index != currentIndex) {
            titles.get(currentIndex).setSelected(false);
            titles.get(index).setSelected(true);
            currentIndex = index;
            viewPager.setCurrentItem(index);
        }
    }

    @Override
    public void onRefreshRecord(RecordPageFragment.RECORD_TYPE recordType,
                                ArrayList<Record> dataSet) {
        //real refresh operation goes here
        this.dataSet = dataSet;
        this.showProgressDlg();
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String endTime = format.format(now) + "240000";
        String startTime = AppUtil.getBeforeTime(3, format, now) + "000000";
        String queryUrl = NameConstant.api.queryUserRecord + "?userId=" + BaseAuth.getUser().getUserId() + "&startTime=" + startTime + "&endTime=" + endTime;
        AppUtil.debugV("====TAG====", "queryUrl:" + queryUrl);
        BaseTaskPool.getInstance().addTask(new QueryUserOrderRecordTask(queryUrl));
    }

    /**
     * Fragment适配器
     */
    class RecordPageAdapter extends FragmentStatePagerAdapter {
        public RecordPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AppUtil.debugV(TAG, "ViewPager position is " + position);
            Bundle bundle = new Bundle();
            if(position == 0)
                bundle.putInt("record_type", RecordPageFragment.current_record);
            else if(position == 1)
                bundle.putInt("record_type", RecordPageFragment.history_record);
            RecordPageFragment instance = new RecordPageFragment();
            instance.setArguments(bundle);
            return instance;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
