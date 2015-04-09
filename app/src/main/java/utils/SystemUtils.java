package utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-09
 * Time: 19:24
 * 系统帮助类
 */
public class SystemUtils {
    private static class SingletonHolder{
        static final SystemUtils instance = new SystemUtils();
    }

    /**
     * 返回单例模式
     * @return
     */
    public static SystemUtils getInstance(){
        return SingletonHolder.instance;
    }

    /**
     * 震动
     * @param context
     * @param milliSeconds
     *              震动时间
     */
    public void vibrate(Context context, long milliSeconds){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliSeconds);
    }

    /**
     * 震动
     * @param context
     * @param pattern
     *          震动模式
     * @param repeat
     *          重复次数
     */
    public void vibrate(Context context, long[] pattern, int repeat){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeat);
    }
}
