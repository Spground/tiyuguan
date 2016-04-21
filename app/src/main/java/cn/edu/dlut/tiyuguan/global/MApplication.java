package cn.edu.dlut.tiyuguan.global;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.dlut.tiyuguan.activity.NoticeShowActivity;
import cn.edu.dlut.tiyuguan.base.BaseMessage;
import cn.edu.dlut.tiyuguan.base.BaseModel;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.bean.NoticeBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.NoticeBeanDao;
import cn.edu.dlut.tiyuguan.dao.RemindBeanDao;
import cn.edu.dlut.tiyuguan.model.NoticeModel;
import cn.edu.dlut.tiyuguan.util.AppUtil;
import cn.edu.dlut.tiyuguan.util.DBUtil;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

public class MApplication extends Application {
	private static final String TAG  = "MApplication";
	public  static  Boolean rememberme;
	private static SharedPreferences cookieFile;
	private static Editor editor;
	NoticeBeanDao noticeBeanDao;
	DaoSession daoSession;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		cookieFile = this.getSharedPreferences("cookieFile", 0);
		editor = cookieFile.edit();
		rememberme = cookieFile.getBoolean("rememberme", false);
		editor.apply();
		SDKInitializer.initialize(getApplicationContext());//在调用任何百度地图组件的时候都得初始化
		initUmengPush();
	}

	private void initUmengPush() {
		PushAgent pushAgent = PushAgent.getInstance(this);
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				String noticeJsonStr = msg.custom;
				AppUtil.debugV(TAG, "noticeJsonStr is " + noticeJsonStr);
				if(noticeJsonStr == null || noticeJsonStr.isEmpty())
					return;
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(noticeJsonStr);
				String formatJsonStr = m.replaceAll("");
				//消息推送到达后的处理
				if(daoSession == null) {
					daoSession = DBUtil.getDaoSession(getApplicationContext());
				}
				noticeBeanDao = daoSession.getNoticeBeanDao();
				String modelName = NoticeModel.class.getSimpleName();
				AppUtil.debugV(TAG, "modelName is " + modelName);
				long noticeId = -1;
				NoticeModel model;
				try {
					BaseMessage message = AppUtil.getMessage(formatJsonStr, modelName);
					model = (NoticeModel) message.getData(modelName);
					AppUtil.debugV(TAG, "Notice model conent is " + model.getNoticeContent());
					if(model != null) {
						NoticeBean bean = new NoticeBean(null,
								model.getNoticeTitle(),
								model.getNoticeContent(),
								model.getNoticeTime(),
								model.getNoticePublisher()
								);
						if(noticeBeanDao != null) {
							noticeId = noticeBeanDao.insert(bean);
							if(noticeId < 0)
								return;
						} else
							AppUtil.debugV(TAG, "noticeBeanDao is null");
					} else
						return;

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				Intent showNoticeIntent = new Intent(MApplication.this, NoticeShowActivity.class);
				showNoticeIntent.putExtra(NoticeShowActivity.EXTRA_NOTICE_ID, noticeId);
				showNoticeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(showNoticeIntent);
			}
		};
		pushAgent.setNotificationClickHandler(notificationClickHandler);
	}

}
