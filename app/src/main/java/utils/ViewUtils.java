package utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 创建一个PopupWindow
     * @param context
     * @return
     */
    public PopupWindow getBottomPopupWindow(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_show_menu, null);
        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        TextView setting = (TextView)view.findViewById(R.id.pop_menu_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("设置被点击");
                popupWindow.dismiss();
            }
        });

        TextView about = (TextView)view.findViewById(R.id.pop_menu_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("关于被点击");
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }
}
