package cn.edu.dlut.tiyuguan.activity;

import cn.edu.dlut.tiyuguan.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FeedBackActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.activity_feedback);
	  //设置actionbar
	  		ActionBar actionBar = this.getActionBar();    
	  	    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
	  	    actionBar.setDisplayShowHomeEnabled(false);
	  	    actionBar.setTitle(" ");
	  	    View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.tv, null);
	  	    ((TextView)actionbarLayout).setText("用户反馈");
	  	     actionBar.setDisplayShowCustomEnabled(true);
	  	     ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(Gravity.CENTER);
	  	   actionBar.setCustomView(actionbarLayout,layout);
	  	     //actionbar结束
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
