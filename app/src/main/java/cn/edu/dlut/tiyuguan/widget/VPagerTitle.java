package cn.edu.dlut.tiyuguan.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.edu.dlut.tiyuguan.R;

/**
 * Created by asus on 2015/12/2.
 */
public class VPagerTitle extends TextView {
    private int index;
    private boolean selected = false;
    private int normalDrawable, selectedDrawable;
    private int normalTextColor = Color.rgb(97, 97, 97);
    private int selectedTextColor = Color.rgb(160, 160, 160);

    public VPagerTitle(Context context) {
        super(context);
        initView();
    }

    public VPagerTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VPagerTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.index = 0;
        this.selected = false;
        this.normalDrawable = R.drawable.vpagertitle_normal;
        this.selectedDrawable = R.drawable.vpagertitle_selected;
        this.setTextColor(normalTextColor);
        this.setBackgroundResource(normalDrawable);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        this.selected = selected;
        if (this.selected) {
            this.setBackgroundResource(selectedDrawable);
            this.setTextColor(selectedTextColor);
        } else {
            this.setBackgroundResource(normalDrawable);
            this.setTextColor(normalTextColor);
        }
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }
}
