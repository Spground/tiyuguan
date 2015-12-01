package cn.edu.dlut.tiyuguan.activity;

import android.os.Bundle;

import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.base.BaseUi;

public class AboutActivity extends BaseUi {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initActionBar("关于");
    }
}
