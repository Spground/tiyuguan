package cn.edu.dlut.tiyuguan.widget;

import cn.edu.dlut.tiyuguan.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog{
	public CustomProgressDialog(Context context) {
		super(context);
	}
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	public static Dialog createDialog(Context context,String dlgText,boolean cancelable){
		LayoutInflater inflater = LayoutInflater.from(context); 
		View dlgView = inflater.inflate(R.layout.loading_dialog, null);

        LinearLayout layout = (LinearLayout) dlgView.findViewById(R.id.dialog_view);
        ImageView spaceshipImage = (ImageView) dlgView.findViewById(R.id.img);
        TextView tipTextView = (TextView) dlgView.findViewById(R.id.tipTextView);

        // 加载动画 使用ImageView显示动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                context, R.anim.loading_animation);  
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        // 设置加载信息
        tipTextView.setText(dlgText);

        // 创建自定义样式dialog
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

        //是否可以用“返回键”取消
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

}  
