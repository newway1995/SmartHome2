package utils;

import android.util.Log;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 01:04
 * Log
 */
public class L {
    private static final String TAG = "Default_Log";

    public static void d(String mTAG,String log){
        Log.d(mTAG,log);
    }

    public static void d(String log){
        Log.d(TAG,log);
    }

    public static void v(String mTAG,String log){
        Log.v(mTAG,log);
    }

    public static void v(String log){
        Log.v(TAG,log);
    }

    public static void e(String mTAG,String log){
        Log.e(mTAG,log);
    }

    public static void e(String log){
        Log.e(TAG,log);
    }

    public static void i(String mTAG,String log){
        Log.i(mTAG, log);
    }

    public static void i(String log){
        Log.i(TAG,log);
    }
}
