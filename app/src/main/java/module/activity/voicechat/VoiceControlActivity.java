package module.activity.voicechat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import utils.ViewUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:27
 * 语音控制界面
 */
public class VoiceControlActivity extends BaseActivity{

    @BindView(id = R.id.voice_control_relativelayout)
    private RelativeLayout contentLayout;//右滑退出的作用

    @BindView(id = R.id.voice_control_btn_msg_send, click = true)
    private Button mBtnSend;//发送按钮

    @BindView(id = R.id.voice_control_listview)
    private ListView chatListView;//listview

    @BindView(id = R.id.ivPopUp, click = true)
    private ImageView chatting_mode_btn;//模式切换按钮-- 语音和文字

    @BindView(id = R.id.et_sendmessage)
    private EditText mEditTextContent;//文字输入的内容

    @BindView(id = R.id.voice_control_btn_record, click = true)
    private TextView mBtnRcd;//按住说话,默认为隐藏

    @BindView(id = R.id.btn_bottom)
    private RelativeLayout mBottom;//底部显示的edittext和send按钮

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
    private String[] msgArray = new String[]{"欢迎您使用小X语音助手O(∩_∩)O哈哈~",
            "五分钟之后回家,帮我把空调调到24度,电视调到CCTV5频道,天快黑了,再帮我把电灯打开",
            "收到,空调已经调到制冷模式24度,电视已经调到14台CCTV5频道,电灯已经打开,不客气哈",
            "我要看湖南卫视",
            "收到,电视已经调到21台湖南卫视频道",
            "帮我把窗帘打开吧",
            "收到,窗帘已经打开",
            "我要睡觉了",
            "收到,电视已经为您关闭,电灯已经为您关闭,空调已经设置到睡眠模式"};

    private String[]dataArray = new String[]{"2015-04-01 18:00", "2015-04-01 18:10",
            "2015-04-01 18:16", "2015-04-01 19:10",
            "2015-04-01 19:10", "2015-04-01 20:07",
            "2015-04-01 20:08", "2015-04-01 21:50",
            "2015-04-01 21:51"};
    private final static int COUNT = 9;


    @Override
    protected void initData() {
        super.initData();
        //右滑退出
        contentLayout.setOnTouchListener(this);
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
            case R.id.voice_control_btn_record://点击录音
                mIatResults.clear();
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(recognizerDialogListener);
                    mIatDialog.show();
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
            case R.id.ivPopUp://切换输入源
                switchInputMode();
                break;
            case R.id.voice_control_btn_msg_send://发送文字按钮被点击
                sendData(mEditTextContent.getText().toString().trim(), false);
                mEditTextContent.setText(null);
                break;
        }
    }

    /**
     * 切换输入的模式
     */
    private void switchInputMode(){
        boolean btn_voice = mBtnRcd.getVisibility() == View.VISIBLE;
        if (btn_voice){//当前模式为语音输入,所以需要切换至文字输入,显示底部输入
            mBottom.setVisibility(View.VISIBLE);
            mBtnRcd.setVisibility(View.GONE);
            ViewUtils.getInstance().showSoftInputMethod(this, mEditTextContent);
            chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);
        }else {//当前模式为文字输入,所以需要切换至语音,并且显示"按住说话"
            mBottom.setVisibility(View.GONE);
            mBtnRcd.setVisibility(View.VISIBLE);
            chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
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
        // 启动activity时不自动弹出软键盘
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                entity.setName("小溪");
                entity.setMsgType(true);
            }else{
                entity.setName("GodV");
                entity.setMsgType(false);
            }

            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgAdapter(this, mDataArrays);
        chatListView.setAdapter(mAdapter);
        chatListView.setSelection(mAdapter.getCount());//滑到最底部
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
    private void sendData(String contString, boolean isComMsg)
    {
        if (contString.length() > 0)
        {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setName("");
            entity.setMsgType(isComMsg);
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
     * 听写监听器。不显示Dialog的时候才运行这个
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            Toast.makeText(VoiceControlActivity.this, "开始说话", Toast.LENGTH_SHORT).show();
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
            L.d(TAG, "isLast = " + isLast + ", Result = " + parseResult(results));
            if (isLast)
                sendData(parseResult(results), false);
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

    /**
     * 点击EditText外部则隐藏SoftMethod
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            }else {
                return true;
            }
        }
        return false;
    }


}
