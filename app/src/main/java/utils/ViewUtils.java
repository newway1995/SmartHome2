package utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-01-06
 * Time: 18:27
 * 常见的帮助类
 */
public class ViewUtils {
    private static ViewUtils instance;
    private ViewUtils(){}

    public static ViewUtils getInstance(){
        if (instance == null){
            synchronized (ViewUtils.class){
                if (instance == null)
                    instance = new ViewUtils();
            }
        }
        return instance;
    }

    /**
     * 生成PopWindow
     * */
    public PopupWindow getPopWindow(Context context,String[]title,int w,int h){
        PopupWindow popupWindow = new PopupWindow(w,h);
        LinearLayout layout = new LinearLayout(context);
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);//设置垂直
        LinearLayout.LayoutParams lp_LinearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp_LinearLayout.gravity = Gravity.CENTER;
        for (int i = 0; i < title.length; i++){
            TextView textView = new TextView(context);
            textView.setText(title[i]);
            layout.addView(textView,lp_LinearLayout);
        }
        popupWindow.setContentView(layout);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.PopMenu);
        return popupWindow;
    }
}
