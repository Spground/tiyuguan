package cn.edu.dlut.tiyuguan.activity;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import cn.edu.dlut.tiyuguan.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SportAnalysisActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sportanalysis);
		//这是actionbar
		ActionBar actionBar = this.getActionBar();    
  	    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
  	    actionBar.setDisplayShowHomeEnabled(false);
  	    actionBar.setTitle(" ");
  	    View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
  	    ((TextView)actionbarLayout).setText("我的运动分析");
  	     actionBar.setDisplayShowCustomEnabled(true);
  	     ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
  	     actionBar.setCustomView(actionbarLayout,layout);
		//actionbar设置结束
		//设置图表数据
		 CombinedChart chart = (CombinedChart) findViewById(R.id.chart);
	        chart.setLogEnabled(true);
	        chart.setDrawBorders(false);
	        chart.setDrawGridBackground(false);
	        chart.setDrawBarShadow(false);
	        chart.setDescription("我的运动分析表,单位：焦耳");
	        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
	        Entry c1e1 = new Entry(10.00f, 0); // 0 == quarter 1
	        Entry c1e2 = new Entry(25.00f, 1); // 1 == quarter 2 ...
	        Entry c1e3= new Entry(31.00f, 2);
	        Entry c1e4 = new Entry(40.00f, 3);
	        Entry c1e5 = new Entry(37.50f, 4);
	        Entry c1e6 = new Entry(31.50f, 5);
	        Entry c1e7 = new Entry(43.00f, 6);
	        valsComp1.add(c1e1);
	        valsComp1.add(c1e2);
	        valsComp1.add(c1e3);
	        valsComp1.add(c1e4);
	        valsComp1.add(c1e5);
	        valsComp1.add(c1e6);
	        valsComp1.add(c1e7);
	        // and so on ...
	        LineDataSet setComp1 = new LineDataSet(valsComp1,"日平均消耗卡路里");
	        
	        setComp1.setColor(getResources().getColor(R.color.pink));
	        setComp1.setCircleColor(getResources().getColor(R.color.darkred));
	        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	        dataSets.add(setComp1);
	        ArrayList<String> xVals = new ArrayList<String>();
	        xVals.add("Sun"); xVals.add("Mon"); xVals.add("Tue"); xVals.add("Wed");
	        xVals.add("Thur"); xVals.add("Fri"); xVals.add("Sat");
	        LineData lData = new LineData(xVals, dataSets);
	        //BarSet
	        BarEntry bE0=new BarEntry(20,0);
	        BarEntry bE1=new BarEntry(50,1);
	        BarEntry bE2=new BarEntry(62,2);
	        BarEntry bE3=new BarEntry(80,3);
	        BarEntry bE4=new BarEntry(75,4);
	        BarEntry bE5=new BarEntry(63,5);
	        BarEntry bE6=new BarEntry(86,6);
	        ArrayList<BarEntry> aB=new ArrayList<BarEntry>();
	        aB.add(bE0);
	        aB.add(bE1);
	        aB.add(bE2);
	        aB.add(bE3);
	        aB.add(bE4);
	        aB.add(bE5);
	        aB.add(bE6);
	        BarDataSet bDataSet=new BarDataSet(aB,"日消耗卡路里");
	        bDataSet.setColor(getResources().getColor(R.color.darkgreen));
	        //bDataSet.setBarShadowColor(getResources().getColor(R.color.transparent));
	        BarData bData=new BarData();
	        bData.addDataSet(bDataSet);
	        CombinedData cData=new CombinedData(xVals);
	        cData.setData(lData);
	        cData.setData(bData);
	        chart.setData(cData);
	        chart.invalidate();
	        chart.setDrawGridBackground(false);
	        chart.setBorderWidth(0);
	        chart.animateX(3000);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
