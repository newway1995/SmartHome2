package module.activity.voicechat;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iflytek.sunflower.FlowerCollector;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.SystemTool;

import java.util.ArrayList;
import java.util.List;
import core.voice.VoiceRecognizeUtils;
import module.core.BaseActivity;
import module.inter.StringProcessor;
import module.view.adapter.ChatMsgAdapter;
import module.view.adapter.ChatMsgEntity;
import utils.StringUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:27
 * 语音控制界面
 */
public class VoiceControlActivity extends BaseActivity{

    @BindView(id = R.id.voice_control_relativelayout)
    private LinearLayout contentLayout;//右滑退出的作用

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

    private InputMethodManager inputMethodManager;

    /**
     * 语音识别帮助类
     */
    private VoiceRecognizeUtils voiceRecognizeUtils;

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
        contentLayout.setOnTouchListener(this);
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        initListView();
        voiceRecognizeUtils = new VoiceRecognizeUtils(this);
        //给voiceRecognize设置回调函数
        voiceRecognizeUtils.setVoiceProcessor(new StringProcessor(){
            @Override
            public void stringProcess(String str) {
                super.stringProcess(str);
                sendData(str, false);
                if (StringUtils.getInstance().hasCCTV(str))
                    sendData("小威帮您找到了CCTV节目表哦~", true);
            }
        });
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
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.voice_control_btn_record://点击录音
                voiceRecognizeUtils.startSpeak();
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
            chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);
            showListViewItem(chatListView.getCount() - 1);
        }else {//当前模式为文字输入,所以需要切换至语音,并且显示"按住说话"
            mBottom.setVisibility(View.GONE);
            mBtnRcd.setVisibility(View.VISIBLE);
            chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
        }
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_voice_control);
        setActionBarView(true);
        // 启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                if (inputMethodManager != null) {
//                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    public  boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = { 0, 0 };
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            if (event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            }else {
//                return true;
//            }
//        }
//        return false;
//    }


}
