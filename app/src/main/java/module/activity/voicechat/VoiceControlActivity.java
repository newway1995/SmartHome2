package module.activity.voicechat;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import module.core.BaseActivity;
import module.view.adapter.ChatMsgAdapter;
import module.view.adapter.ChatMsgEntity;
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

    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.voice_control_btn_record:
                sendData("我是VGod,其实我想告诉你,龙哥长得就像一坨屎!");
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
