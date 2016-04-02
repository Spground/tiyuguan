package cn.edu.dlut.tiyuguan.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;

public class PostDetailActivity extends BaseUi {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        initActionBar("发布详情");
    }
}
