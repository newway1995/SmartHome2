package module.activity.voicechat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.iflytek.sunflower.FlowerCollector;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.SystemTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import constant.Command;
import constant.ConstantStatus;
import constant.MyTimer;
import constant.TVChannelConstant;
import constant.TimingExecute;
import constant.VoiceCommand;
import core.voice.VoiceRecognizeUtils;
import core.voice.VoiceSpeakUtils;
import module.activity.energy.EnergyFanActivity;
import module.activity.energy.EnergyTVActivity;
import framework.base.SwipeBackActivity;
import module.inter.StringProcessor;
import module.adapter.ChatMsgAdapter;
import module.database.ChatMsgEntity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:27
 * 语音控制界面
 */
public class VoiceControlActivity extends SwipeBackActivity{

    @BindView(id = R.id.voice_control_listview)
    private ListView chatListView;//listview

    @BindView(id = R.id.voice_control_btn_record)
    private ImageView mBtnRcd;//按住说话,默认为隐藏


    /**
     * 语音识别帮助类
     */
    private VoiceRecognizeUtils voiceRecognizeUtils;
    private VoiceSpeakUtils voiceSpeakUtils;
    private List<String> commandList;//不是commandList而是resultList

    private ChatMsgAdapter mAdapter ;
    /* 需要显示的数据 */
    private List<ChatMsgEntity> mDataArrays = new ArrayList<>();

    private String[] voiceCommandReturn = new String[]{
            "听不懂",
            "好好说话",
            "什么?",
            "请说普通话",
            "What?",
            "再说一遍吧",
            "没听清楚"
    };

    private Context context = this;
    private final String TAG = getClass().getSimpleName();
    //定时器
    private MyTimer myTimer;
    private TimingExecute timingExecute;
    private boolean isReturnData = true;

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_voice_control);
        setActionBarView(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //点击事件的监听
        mBtnRcd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    voiceRecognizeUtils.startSpeak();
                    mBtnRcd.setBackgroundResource(R.drawable.yuyin_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    voiceRecognizeUtils.stopSpeak();
                    mBtnRcd.setBackgroundResource(R.drawable.yuyin);
                }
                return true;
            }
        });
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
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
                isReturnData = true;//默认需要返回数据
                //测试
                //VoiceCommand.parseVoiceCommand(context, str);
                /** 测试能耗 */
                if (testEnergy(str)) {
                    return;
                }
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
                } else if ((commandList == null || commandList.size() == 0) && isReturnData) {
                    sendData(voiceCommandReturn[new Random().nextInt(voiceCommandReturn.length)], true);
                    isReturnData = true;
                }
            }
        });

        voiceSpeakUtils = new VoiceSpeakUtils(this);
    }

    /**
     * 测试能耗
     */
    private boolean testEnergy(final String str) {
        if (str.contains("风扇") || str.contains("电风扇") || str.contains("电扇")) {
            startActivity(new Intent(VoiceControlActivity.this, EnergyFanActivity.class));
            return true;
        } else if(str.contains("电视") || str.contains("电视机")) {
            startActivity(new Intent(VoiceControlActivity.this, EnergyTVActivity.class));
            return true;
        }
        return false;
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
        /** 小威再见 */
        closeVoice(str);
        /** 是否显示电器连接状态 **/
        processShowElecStatus(str);
        /** 是否显示当前CommandList **/
        processShowCommandList(str);
        /** 是否为查询电视节目 **/
        //goToTVProgramActivity(str);
        /** 是否为电视节目选择 */
        processTVProgramSelect(str);
        /** 设置电视节目键值对 */
        processTVProgramSetting(str);
        /** 用户查询电视节目列表,目前只支持湖北卫视和CCTV1 */
        parseTVProgramSelect(str);
    }

    /** 是否为电视节目选择 */
    private void processTVProgramSelect(final String str) {
        HashMap<String, String> map = VoiceCommand.parseTVProgramSelect(context, str);
        if (map != null) {
            if (map.get("error") != null) {
                sendData("小威找不到您要的电视节目", true);
            } else {
                int number = Integer.parseInt(map.get("number"));
                sendData("小威已经帮您换到了" + map.get("channelText") + number + "频道", true);
                myTimer.setTimerAndTimerMillisecond(false, 0);
                myTimer.sendCommand(Command.TELEVISION_CHANNEL + number);
                isReturnData = false;
            }
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
        if (VoiceCommand.parseTVProgramSetting(context, str)) {
            startActivity(new Intent(VoiceControlActivity.this, TVProgramSettingActivity.class));
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
        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setMsgType(true);
        entity.setText("欢迎您使用小威语音助手O(∩_∩)O哈哈~");
        mDataArrays.add(entity);

        mAdapter = new ChatMsgAdapter(this, mDataArrays);
        chatListView.setAdapter(mAdapter);
        showListViewItem(mAdapter.getCount());
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
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

    /**
     * 小威再见
     * @param source 输入源
     */
    private void closeVoice(final String source) {
        if (source.contains("再见")) {
            isReturnData = false;
            finish();
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
        chatListView = null;//listview
        mBtnRcd = null;//按住说话,默认为隐藏
    }
}
