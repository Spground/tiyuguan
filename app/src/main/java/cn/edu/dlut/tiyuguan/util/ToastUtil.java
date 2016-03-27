package cn.edu.dlut.tiyuguan.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.dlut.tiyuguan.R;

/**
 * Created by asus on 2015/10/6.
 */

/**
 * TODO:自定义Toast的样式
 */
public class ToastUtil {

    public static void showErrorToast(Context ctx,String msg) {
        Toast toast = setUpToastView(ctx, msg,
                R.drawable.shape_toast_bg_error,
                R.color.toast_font_error);
        toast.show();
    }

    public static void showWarningToast(Context ctx,String msg) {
        Toast toast = setUpToastView(ctx, msg,
                R.drawable.shape_toast_bg_warning,
                R.color.toast_font_warning);
        toast.show();
    }

    public static void showSuccessToast(Context ctx, String msg) {
        Toast toast = setUpToastView(ctx, msg,
                R.drawable.shape_toast_bg_success,
                R.color.toast_font_success);
        toast.show();
    }

    public static void showInfoToast(Context ctx,String msg) {
        Toast toast = setUpToastView(ctx, msg,
                R.drawable.shape_toast_bg_success,
                R.color.toast_font_info);
        toast.show();
    }

    public static void showNormalToast(Context ctx,String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param ctx
     * @param msg
     * @param bgRID
     * @param colorRID
     * @return
     */
    private static Toast setUpToastView(Context ctx, String msg, int bgRID, int colorRID) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_default_layout, null);
        view.setBackgroundResource(bgRID);

        TextView textView = ((TextView) view.findViewById(R.id.toast_message));
        textView.setText(msg != null ? msg : "");
        textView.setTextColor(colorRID);

        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setView(view);
        return toast;
    }
}
