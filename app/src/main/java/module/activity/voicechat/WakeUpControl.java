package module.activity.voicechat;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import core.json.VoiceJsonParser;
import core.voice.ApkInstaller;
import module.inter.StringProcessor;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-12
 * Time: 00:22
 * 唤醒控制
 */
public class WakeUpControl {
    private Context context;
    private StringProcessor voiceProcessor;

    /* 语音控制部分 */
    //语音听写对象
    private SpeechRecognizer mIat;
    //用HashMap保存听写的结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //引擎类型 云端
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语音+安装助手类
    private ApkInstaller mInstaller;
    private SharedPreferences mSharedPreferences;

    /**
     * 构造函数
     * @param context
     */
    public WakeUpControl(Context context) {
        this.context = context;
        initVoice();
        setParam();
    }

    /**
     * 初始化语音控制
     */
    private void initVoice(){
        mSharedPreferences = context.getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mEngineType = SpeechConstant.TYPE_CLOUD;
    }

    /**
     * 参数设置
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
        // 设置语音后端点
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
        // 设置标点符号
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
        // 设置音频保存路径
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()
                + "/iflytek/wavaudio.pcm");
        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(context, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 听写监听器。不显示Dialog的时候才运行这个
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {
        private boolean isContinue = true;

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onError(SpeechError error) {
        }

        @Override
        public void onEndOfSpeech() {
            if (isContinue) {
                startSpeak();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = parseResult(results);
            if (text.contains("你好") && (text.contains("小威")||text.contains("小伟")||text.contains("小微")||text.contains("小薇")||text.contains("小魏"))){
                voiceProcessor.stringProcess(text);
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    /**
     * 解析语音转化的文字结果
     * @param results
     * @return
     */
    private String parseResult(RecognizerResult results){
        String text = VoiceJsonParser.parseIatResult(results.getResultString());
        String sn = null;

        //读取json结果中得sn字段
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }

    /**
     * 针对调用Activity的点击事件
     */
    public void startSpeak(){
        int ret = 0; // 函数调用返回值

        mIatResults.clear();
        // 不显示听写对话框
        ret = mIat.startListening(recognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            Toast.makeText(context,"听写失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context,"请开始说话…", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 语音处理函数
     * @param voiceProcessor
     */
    public void setVoiceProcessor(StringProcessor voiceProcessor) {
        this.voiceProcessor = voiceProcessor;
    }

    public void stop(){
        // 退出时释放连接
        mIat.cancel();
        //mIat.destroy();
    }

    public void start(){
        if (!mIat.isListening())
            mIat.startListening(recognizerListener);
    }

}
