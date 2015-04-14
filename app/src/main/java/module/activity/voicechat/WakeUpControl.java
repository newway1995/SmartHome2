package module.activity.voicechat;

import android.content.Context;
import android.widget.Toast;

import cn.yunzhisheng.common.USCError;
import cn.yunzhisheng.wakeup.WakeUpRecognizer;
import cn.yunzhisheng.wakeup.WakeUpRecognizerListener;
import utils.SystemUtils;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-12
 * Time: 00:22
 * 唤醒控制
 */
public class WakeUpControl {
    public static final String appKey = "_appKey_";
    public static final String  secret = "_secret_";

    private WakeUpRecognizer mWakeUpRecognize;
    private Context context;

    private SkipToOther mSkipToOther;

    public WakeUpControl(Context context) {
        this.context = context;
        init();
    }

    /**
     * 初始化函数
     */
    private void init(){
        mWakeUpRecognize = new WakeUpRecognizer(context, appKey);
        mWakeUpRecognize.setListener(new WakeUpRecognizerListener() {
            @Override
            public void onWakeUpRecognizerStart() {
                Toast("小威语音助手准备就绪");
            }

            @Override
            public void onWakeUpRecognizerStop() {
                Toast("停止语音唤醒");
            }

            @Override
            public void onWakeUpResult(boolean b, String s, float v) {
                if (b) {//成功就跳转
                    SystemUtils.getInstance().vibrate(context, 200);
                    mSkipToOther.skip();
                }
            }

            @Override
            public void onWakeUpError(USCError uscError) {
                Toast("唤醒失败");
            }
        });
    }

    /**
     * 开始唤醒
     */
    public void startWakeUp(){
        mWakeUpRecognize.start();
    }

    /**
     * 停止唤醒
     */
    public void stopWakeUp(){
        mWakeUpRecognize.stop();
        mWakeUpRecognize.release();
    }

    /**
     * 显示信息
     * @param msg
     */
    private void Toast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置回调
     * @param mSkipToOther
     */
    public void setmSkipToOther(SkipToOther mSkipToOther) {
        this.mSkipToOther = mSkipToOther;
    }

    public interface SkipToOther{
        void skip();
    }
}
