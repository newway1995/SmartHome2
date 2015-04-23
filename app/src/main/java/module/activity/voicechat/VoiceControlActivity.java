package module.activity.voicechat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.iflytek.sunflower.FlowerCollector;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.SystemTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import constant.Command;
import constant.ConstantStatus;
import constant.MyTimer;
import constant.TVChannelConstant;
import constant.TimingExecute;
import constant.VoiceCommand;
import core.voice.VoiceRecognizeUtils;
import core.voice.VoiceSpeakUtils;
import module.database.TVChannelEntity;
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


    /**
     * 语音识别帮助类
     */
    private VoiceRecognizeUtils voiceRecognizeUtils;
    private VoiceSpeakUtils voiceSpeakUtils;
    private List<String> commandList;//不是commandList而是resultList

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
        initVoice();
        initData();
        initListView();
    }

    @SuppressLint("NewApi")
    protected void initData() {
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
                //测试
                //VoiceCommand.parseVoiceCommand(context, str);
                /** 测试设置 */
                processVoiceSetting(str);
                /** 当前所有的指令集合 **/
                commandList = VoiceCommand.parseVoiceCommand(context, str);
                /** 定时时间 **/
                //int time = StringUtils.getInstance().getNumberBeforePattern(str);
                if (commandList != null && commandList.size() > 0) {
                    sendData(VoiceCommand.getVoiceFeedback(), true);
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

    /**
     * 封装一个函数来解决发送指令的问题
     */
    private void processVoiceCommand(final String str) {

    }

    /**
     * 封装一个函数解决非指令发送的问题
     */
    private void processVoiceSetting(final String str) {
        /** 是否显示电器连接状态 **/
        processShowElecStatus(str);
        /** 是否显示当前CommandList **/
        processShowCommandList(str);
        /** 是否为查询电视节目 **/
        goToTVProgramActivity(str);
        /** 是否为电视节目选择 */
        processTVProgramSelect(str);
        /** 设置电视节目键值对 */
        //processTVProgramSetting(str);
        /** 用户查询电视节目列表,目前只支持湖北卫视和CCTV1 */
        parseTVProgramSelect(str);
    }

    /** 是否为电视节目选择 */
    private void processTVProgramSelect(final String str) {
        HashMap<String, String> map = VoiceCommand.parseTVProgramSelect(context, str);
        if (map != null) {
            sendData(map.get("channelText"), true);
            sendData(map.get("number"), true);
        }
    }

    /** 是否显示电器连接状态 **/
    private void processShowElecStatus(final String str) {
        if (VoiceCommand.isShowElecStatus(context, str))
            sendData(ConstantStatus.getAllStatus(context), true);
    }

    /** 是否显示当前CommandList **/
    private void processShowCommandList(final String str) {
        if (VoiceCommand.isShowCommandList(context, str))
            startActivity(new Intent(VoiceControlActivity.this, ShowCommandListActivity.class));
    }

    /** 设置电视节目键值对 */
    private void processTVProgramSetting(final String str) {
        HashMap<String, String> map = VoiceCommand.parseTVProgramSetting(context, str);
        TVChannelEntity.kjdb = KJDB.create(this);
        if (map != null) {
            TVChannelEntity.insert(new TVChannelEntity(Integer.parseInt(map.get("number")), map.get("channelText"), map.get("channelRel")));
        } else {
            sendData("不好意思,小V无法明白您的意思...", true);
        }
    }

    /**
     * 湖北卫视和cctv1
     */
    private void parseTVProgramSelect(final String str) {
        String result = VoiceCommand.getTVProgramInfo(context, str);
        if (result != null) {
            sendData(result, true);
        }
    }


    /**
     * 初始化ListView数据
     */
    private void initListView(){
        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setMsgType(i % 2 == 0);
            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgAdapter(this, mDataArrays);
        chatListView.setAdapter(mAdapter);
        showListViewItem(mAdapter.getCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.voice_control_btn_record://点击录音
                voiceRecognizeUtils.startSpeak();
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
            entity.setName(isComMsg ? "小威" : "VGOD");
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
    protected void onStop(){
        super.onStop();
        cleanMemory();
        setContentView(R.layout.null_view);
        finish();
        System.gc();
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

    /**
     * 清理内存
     */
    private void cleanMemory(){
        contentLayout = null;
        chatListView = null;//listview
        mBtnRcd = null;//按住说话,默认为隐藏
    }
}
