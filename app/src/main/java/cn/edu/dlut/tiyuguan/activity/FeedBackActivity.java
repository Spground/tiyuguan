package cn.edu.dlut.tiyuguan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseTask;
import cn.edu.dlut.tiyuguan.base.BaseTaskPool;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.util.AppClient;
import cn.edu.dlut.tiyuguan.util.AppUtil;

public class FeedBackActivity extends BaseUi implements View.OnClickListener, DialogInterface.OnClickListener{
	private final static String TAG = "FeedBackActivity";

	private EditText fbkEdit;
	private Button submitBtn;
	private AlertDialog alertDialog;
	private String fbkContent;
	private Map<String, String> params = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.activity_feedback);
		initActionBar("用户反馈");
		initView();
	}

	/**
	 * init view and set event listener
	 */
	private void initView() {
		this.fbkEdit = (EditText)findViewById(R.id.id_feedback_edittxt);
		this.submitBtn = (Button) findViewById(R.id.id_submit_btn);
		this.submitBtn.setOnClickListener(this);
		alertDialog = new AlertDialog.Builder(this).
				setMessage("提交反馈成功,谢谢您的反馈!")
				.setNegativeButton("知道了",FeedBackActivity.this)
				.setCancelable(false)
				.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() != R.id.id_submit_btn)
			return;
		this.fbkContent = this.fbkEdit.getText().toString().trim();
		if(this.fbkContent.length() > 0 && this.fbkContent.length() <= 140) {
			//start performing network operation
			showProgressDlg();
			AppUtil.debugV("===" + TAG + "=== input content", this.fbkContent);
			BaseTaskPool.getInstance().addTask(new BaseTask() {
				@Override
				public void start() {
					params.clear();
					params.put("fbk_content", FeedBackActivity.this.fbkContent);
					AppUtil.debugV("===" + TAG + "=== fbk url is ", NameConstant.api.submitFbkContent);
					try {
						String response = AppClient.getInstance().post(NameConstant.api.submitFbkContent, params);
						onCompleted(response);
					} catch (IOException e) {
						e.printStackTrace();
						onExceptionError(e);
					}
				}

				@Override
				public void onNetworkError(Exception e) {
					super.onNetworkError(e);
					AppUtil.debugV("===" + TAG + "=== network exception", e.toString());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							hideProgressDlg();
							toastError("Error");
						}
					});
				}

				@Override
				public void onExceptionError(Exception e) {
					super.onExceptionError(e);
					AppUtil.debugV("===" + TAG + "=== exception is", e.toString());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							hideProgressDlg();
							toastError("Exception");
						}
					});
				}

				@Override
				public void onCompleted(String response) {
					super.onCompleted(response);
					AppUtil.debugV("===" + TAG + "=== response is", response);
					try {
						JSONObject jObj = new JSONObject(response);
						String codeValue = jObj.getString("code");
						if (codeValue.equalsIgnoreCase("success")) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									hideProgressDlg();
									alertDialog.show();
								}
							});
						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									hideProgressDlg();
									toastError("提交失败，请你稍后再试!");
								}
							});
						}

					} catch (JSONException e) {
						e.printStackTrace();
						onExceptionError(e);
						return;
					}
				}
			});
		}
		else
			toastWarning("字数要求在140字以内");
		closeSoftInputWindow();
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

	/**
	 * close soft input
	 */
	private void closeSoftInputWindow() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.fbkEdit.getWindowToken(), 0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		FeedBackActivity.this.finish();
	}
}
