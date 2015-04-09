package core.voice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.iflytek.cloud.SynthesizerListener;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import module.activity.voicechat.TtsSettings;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-07
 * Time: 23:43
 * 将文字转换成语音输出
 */
public class VoiceSpeakUtils {
    private Context context;
    /** 文字转语音 */
    private SpeechSynthesizer mTts;
    /** 默认的发音人 */
    private String voicer = "xiaoyan";
    /** 缓冲进度 */
    private int mPercentForBuffering = 0;
    /** 播放进度 */
    private int mPercentForPlaying = 0;
    /** 引擎类型 */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private SharedPreferences mSharedPreferences;
    private Toast mToast;

    public VoiceSpeakUtils(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        setParam();
    }

    /**
     * @param text
     * 开始说话
     */
    public void startSpeak(String text){
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                //未安装则跳转到提示安装页面
                showTip("未安装服务...");
            }else {
                showTip("语音合成失败,错误码: " + code);
            }
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            //showTip(String.format(context.getString(R.string.tts_toast_format),
            //        mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(context.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicer);
            //设置语速
            mTts.setParameter(SpeechConstant.SPEED,mSharedPreferences.getString("speed_preference", "50"));
            //设置音调
            mTts.setParameter(SpeechConstant.PITCH,mSharedPreferences.getString("pitch_preference", "50"));
            //设置音量
            mTts.setParameter(SpeechConstant.VOLUME,mSharedPreferences.getString("volume_preference", "50"));
            //设置播放器音频流类型
            mTts.setParameter(SpeechConstant.STREAM_TYPE,mSharedPreferences.getString("stream_preference", "3"));
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人 voicer为空默认通过语音+界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME,"");
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 在Activity的Destroy当中需要销毁stop()、destroy()
     * @return
     */
    public SpeechSynthesizer getmTts() {
        return mTts;
    }
}
