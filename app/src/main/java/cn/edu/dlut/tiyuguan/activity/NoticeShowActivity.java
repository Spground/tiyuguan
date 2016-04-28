package cn.edu.dlut.tiyuguan.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;
import cn.edu.dlut.tiyuguan.bean.NoticeBean;
import cn.edu.dlut.tiyuguan.dao.DaoMaster;
import cn.edu.dlut.tiyuguan.dao.DaoSession;
import cn.edu.dlut.tiyuguan.dao.NoticeBeanDao;
import cn.edu.dlut.tiyuguan.global.NameConstant;
import cn.edu.dlut.tiyuguan.util.ToastUtil;

public class NoticeShowActivity extends BaseUi implements BaseUi.AsyncLoadDataFromDBCallback {
    public static final String EXTRA_NOTICE_ID = "extra_notice_id";
    DaoSession daoSession;
    NoticeBeanDao noticeBeanDao;
    long noticeId;
    private TextView noticeTitleTV, noticeTimeTV, noticePublisherTV;
    private EditText noticeContentET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_show);
        noticeId = getIntent().getLongExtra(EXTRA_NOTICE_ID, -1);
        initActionBar("体育馆通知");
        initView();
        showProgressDlg();
        asynLoadDataFromDB(this, NameConstant.dbName);
    }

    @Override
    public void asyncLoadDataFromDBCompleted(SQLiteDatabase database) {
        daoSession = new DaoMaster(database).newSession();
        noticeBeanDao = daoSession.getNoticeBeanDao();
        NoticeBean bean = noticeBeanDao.load(noticeId);
        hideProgressDlg();
        if(bean != null) {
            showNotice(bean);
        }
    }

    private void initView() {
        noticeTitleTV = (TextView) findViewById(R.id.notice_title);
        noticeTimeTV =  (TextView) findViewById(R.id.notice_time);
        noticeContentET =  (EditText) findViewById(R.id.notice_content);
        noticePublisherTV = (TextView) findViewById(R.id.notice_publisher);
    }

    private void showNotice(NoticeBean bean) {
        String noticeTitle = bean.getNoticeTitle();
        String noticeContent = bean.getNoticeContent();
        String noticeTime = bean.getNoticeTime();
        String noticePublisher = bean.getNoticePublisher();
        noticeContentET.setText(noticeContent);
        noticePublisherTV.setText(noticePublisher);
        noticeTimeTV.setText(noticeTime);
        noticeTitleTV.setText(noticeTitle);
    }
}
