package module.activity.voicechat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import core.json.VoiceJsonParser;
import core.voice.ApkInstaller;
import module.core.BaseActivity;
import module.view.adapter.ChatMsgAdapter;
import module.view.adapter.ChatMsgEntity;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:27
 * 语音控制界面
 */
public class VoiceControlActivity extends BaseActivity{

    @BindView(id = R.id.voice_control_btn_record, click = true)
    private Button mBtnSend;
    @BindView(id = R.id.voice_control_listview)
    private ListView chatListView;

    /* 语音控制部分 */
    //语音听写对象
    private SpeechRecognizer mIat;
    //语音听写UI
    private RecognizerDialog mIatDialog;
    //用HashMap保存听写的结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //引擎类型 云端
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语音+安装助手类
    private ApkInstaller mInstaller;
    private SharedPreferences mSharedPreferences;


    private ChatMsgAdapter mAdapter ;
    /* 需要显示的数据 */
    private List<ChatMsgEntity> mDataArrays = new ArrayList<>();

    /* =======================================测试数据===================================== */
    private String[] msgArray = new String[]{"  孩子们，要好好学习，天天向上！要好好听课，不要翘课！不要挂科，多拿奖学金！三等奖学金的争取拿二等，二等的争取拿一等，一等的争取拿励志！",
            "姚妈妈还有什么吩咐...",
            "还有，明天早上记得跑操啊，不来的就扣德育分！",
            "德育分是什么？扣了会怎么样？",
            "德育分会影响奖学金评比，严重的话，会影响毕业",
            "哇！学院那么不人道？",
            "你要是你不听话，我当场让你不能毕业！",
            "姚妈妈，我知错了(- -我错在哪了...)"};

    private String[]dataArray = new String[]{"2012-09-01 18:00", "2012-09-01 18:10",
            "2012-09-01 18:11", "2012-09-01 18:20",
            "2012-09-01 18:30", "2012-09-01 18:35",
            "2012-09-01 18:40", "2012-09-01 18:50"};
    private final static int COUNT = 8;


    @Override
    protected void initData() {
        super.initData();
        initListView();
        initVoice();
        switchProgressBar(false);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    int ret = 0; // 函数调用返回值
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.voice_control_btn_record:
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(recognizerDialogListener);
                    mIatDialog.show();
                    Toast.makeText(VoiceControlActivity.this,"请开始说话…", Toast.LENGTH_SHORT).show();
                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(recognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        Toast.makeText(VoiceControlActivity.this,"听写失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VoiceControlActivity.this,"请开始说话…", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void setRootView() {
        requestWindowFeature(Window.FEATURE_PROGRESS);//在窗口标题上显示带进度的横向进度条
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//显示不带进度的进度条
        super.setRootView();
        setContentView(R.layout.activity_voice_control);
        setActionBarView(true);
        switchProgressBar(true);
    }

    /**
     * 初始化ListView数据
     */
    private void initListView(){
        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0)
            {
                entity.setName("姚妈妈");
                entity.setMsgType(true);
            }else{
                entity.setName("Shamoo");
                entity.setMsgType(false);
            }

            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgAdapter(this, mDataArrays);
        chatListView.setAdapter(mAdapter);
    }

    /**
     * 初始化语音控制
     */
    private void initVoice(){
        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        mIatDialog = new RecognizerDialog(VoiceControlActivity.this, mInitListener);
        mInstaller = new ApkInstaller(this);
        mEngineType = SpeechConstant.TYPE_CLOUD;
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(VoiceControlActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 发送一条数据
     */
    private void sendData(String contString)
    {
        if (contString.length() > 0)
        {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setName("");
            entity.setMsgType(false);
            entity.setText(contString);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            chatListView.setSelection(chatListView.getCount() - 1);
        }
    }

    /**
     * 获取日期
     * @return
     */
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
        return sbBuffer.toString();
    }

    /**
     * 设置显示Progressbar
     * @param isShow
     */
    private void switchProgressBar(boolean isShow){
        if (isShow){
            setProgressBarVisibility(true);
            setProgressBarIndeterminate(true);
            setProgress(4500);
        }else {
            setProgressBarVisibility(false);
            setProgressBarIndeterminate(false);
        }
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            //Toast.makeText(VoiceControlActivity.this, "开始说话", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
            Toast.makeText(VoiceControlActivity.this, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            Toast.makeText(VoiceControlActivity.this, "结束说话", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            L.d(TAG, "听写监听器:" + parseResult(results));
            if (isLast) {
                Toast.makeText(VoiceControlActivity.this, parseResult(results), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
            Toast.makeText(VoiceControlActivity.this, "当前正在说话，音量大小：" + volume, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            sendData(parseResult(results));
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(VoiceControlActivity.this, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
        mIatDialog.dismiss();
    }

    @Override
    protected void onResume() {
        // 移动数据统计分析
        FlowerCollector.onResume(VoiceControlActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(VoiceControlActivity.this);
        super.onPause();
    }
}
