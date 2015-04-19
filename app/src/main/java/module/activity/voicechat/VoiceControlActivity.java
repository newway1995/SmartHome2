package module.activity.voicechat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.iflytek.sunflower.FlowerCollector;
import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.SystemTool;
import java.util.ArrayList;
import java.util.List;
import constant.Command;
import constant.ConstantStatus;
import constant.MyTimer;
import constant.TVChannelConstant;
import constant.TimingExecute;
import constant.VoiceCommand;
import core.voice.VoiceRecognizeUtils;
import core.voice.VoiceSpeakUtils;
import module.inter.StringProcessor;
import module.view.adapter.ChatMsgAdapter;
import module.database.ChatMsgEntity;
import utils.StringUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:27
 * 语音控制界面
 */
public class VoiceControlActivity extends Activity implements View.OnClickListener{

    @BindView(id = R.id.voice_control_relativelayout)
    private LinearLayout contentLayout;

    @BindView(id = R.id.voice_control_listview)
    private ListView chatListView;//listview

    @BindView(id = R.id.voice_control_btn_record, click = true)
    private TextView mBtnRcd;//按住说话,默认为隐藏

    private InputMethodManager inputMethodManager;

    /**
     * 语音识别帮助类
     */
    private VoiceRecognizeUtils voiceRecognizeUtils;
    private VoiceSpeakUtils voiceSpeakUtils;
    private List<String> commandList;

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

    private Context context = this;
    private final String TAG = getClass().getSimpleName();
    //定时器
    private MyTimer myTimer;
    private TimingExecute timingExecute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voice_control);
        AnnotateUtil.initBindView(this);
        //getBlueBackground();
        initVoice();
        initData();
        initListView();
    }

    @SuppressLint("NewApi")
    protected void initData() {
        //contentLayout.setBackground(new BitmapDrawable(getResources(), bgBitmap));
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        initVoice();
        initListView();
        myTimer = new MyTimer(context);
    }

    /**
     * 初始化语音帮助类
     */
    private void initVoice(){
        timingExecute = new TimingExecute(context);
        voiceRecognizeUtils = new VoiceRecognizeUtils(this);
        //给voiceRecognize设置回调函数
        voiceRecognizeUtils.setVoiceProcessor(new StringProcessor() {
            @Override
            public void stringProcess(String str) {
                super.stringProcess(str);
                sendData(str, false);
                if (str.contains("跳转"))
                {
                    testActivity();
                }
                /** 是否显示电器连接状态 **/
                if (VoiceCommand.isShowElecStatus(context, str))
                    sendData(ConstantStatus.getAllStatus(context), true);
                /** 是否显示当前CommandList **/
                if(VoiceCommand.isShowCommandList(context, str))
                    startActivity(new Intent(VoiceControlActivity.this, ShowCommandListActivity.class));
                /** 是否为查询电视节目 **/
                goToTVProgramActivity(str);
                /** 当前所有的指令集合 **/
                commandList = VoiceCommand.parseVoiceCommand(context, str);
                /** 定时时间 **/
                int time = StringUtils.getInstance().getNumberBeforePattern(str);
                if (commandList != null && commandList.size() > 0) {
                    //myTimer.setTimer(true);
                    //myTimer.setTimerMilliscond(time * 1000);
                    //myTimer.sendCommand(commandList);

                    sendData("指令集合为" + commandList + ",并且在 " + time + " 秒钟之后执行。", true);
                    timingExecute.initThread();
                    timingExecute.sendData();
                } else if (StringUtils.getInstance().hasCCTV(str)) {
                    sendData("小威帮您找到了CCTV节目表哦~", true);
                    myTimer.setTimer(true);
                    myTimer.setTimerMilliscond(5000);
                    myTimer.sendCommand(Command.TELEVISION_UP_VOL);
                }
            }
        });

        voiceSpeakUtils = new VoiceSpeakUtils(this);
    }

    private void testActivity(){
        startActivity(new Intent(VoiceControlActivity.this, ShowCommandListActivity.class));
    }

    /**
     * 初始化ListView数据
     */
    private void initListView(){
        // 在代码中实现列表动画
        Animation animation = (Animation) AnimationUtils.loadAnimation(
                context, R.anim.list_anim);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setDelay(0.4f);  //设置动画间隔时间
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); //设置列表的显示顺序
        chatListView.setLayoutAnimation(lac);  //为ListView 添加动画
        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0)
            {
                entity.setName("小微");
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
        showListViewItem(mAdapter.getCount());
        //设置外部点击事件
        chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.voice_control_btn_record://点击录音
                voiceRecognizeUtils.startSpeak();
                break;
            case R.id.ivPopUp://切换输入源
                break;
            case R.id.voice_control_btn_msg_send://发送文字按钮被点击
                break;
        }
    }


    /**
     * 发送一条数据
     */
    private void sendData(String contString, boolean isComMsg)
    {
        if (contString.length() > 0)
        {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(SystemTool.getDataTime("yyyy-MM-dd : hh:mm"));
            entity.setName(isComMsg == true ? "小威" : "VGOD");
            entity.setMsgType(isComMsg);
            entity.setText(contString);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            chatListView.setSelection(chatListView.getCount() - 1);
            if (isComMsg)
                voiceSpeakUtils.startSpeak(contString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置ListView显示那几个标签
     * @param i
     */
    private void showListViewItem(int i){
        chatListView.setSelection(i);
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyBoard(){
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 跳转到显示电视节目表
     */
    private void goToTVProgramActivity(String string){
        if (TVChannelConstant.WhichChannel(string) != null)
        {
            Intent intent = new Intent(context, TVProgramActivity.class);
            intent.putExtra("code", TVChannelConstant.WhichChannel(string));
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        voiceRecognizeUtils.getmIat().cancel();
        voiceRecognizeUtils.getmIat().destroy();
        voiceRecognizeUtils.getmIatDialog().dismiss();
    }

    @Override
    protected void onResume() {
        // 移动数据统计分析
        FlowerCollector.onResume(context);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(context);
        super.onPause();
    }

}
