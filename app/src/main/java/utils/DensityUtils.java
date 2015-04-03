package utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-25
 * Time: 12:58
 * 系统屏幕的一些操作
 */
public class DensityUtils {
    private DensityUtils(){}

    private static DensityUtils instance;

    /**
     * 获取实例
     * @return
     */
    public static DensityUtils getInstance(){
        if (instance == null){
            synchronized (DensityUtils.class){
                if (instance == null)
                    instance = new DensityUtils();
            }
        }
        return instance;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context
     * @param dpValue
     * @return px
     */
    public int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context
     * @param pxValue
     * @return dip
     */
    public int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     * @param context
     * @param pxValue
     * @return sp
     */
    public int px2sp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     * @param context
     * @param spValue
     * @return px
     */
    public int sp2px(Context context, float spValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * scale + 0.5);
    }

    /**
     * 获取屏幕的宽度
     * @param activity
     * @return
     */
    public int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     * @param activity
     * @return
     */
    public int getScreenHeight(Activity activity){
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕长宽比(宽高比)
     * @param context
     * @return
     */
    public int getScreenRate(Activity context){
        return getScreenHeight(context) / getScreenWidth(context);
    }

    /**
     * 获取状态栏的高度
     * @param activity
     * @return
     */
    public int getStateBarHeight(Activity activity){
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取标题栏的高度
     * @param activity
     * @return
     */
    public int getTitleBarHeight(Activity activity){
        //取到的view就是程序不包括标题栏的部分
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStateBarHeight(activity);
    }
}
