package module.activity.controler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

import constant.Command;
import constant.Constant;
import constant.ConstantStatus;
import module.core.BaseActivity;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-31
 * Time: 00:24
 * FIXME
 */
public class ControllerBulbActivity extends BaseActivity{
    @BindView(id = R.id.activity_controller_bulb)
    private RelativeLayout contentLayout;

    @BindView(id = R.id.bulb_switch, click = true)
    private ImageView switchBulb;
    @BindView(id = R.id.bulb_img)
    private ImageView bulbImage;

    private boolean isOpen = false;

    /* 定时功能 */
    private boolean isTimer;//是否定时
    private int timerMilliscond;//多久之后执行
    private static final int[] timerSelector = new int[]{5, 10, 15, 20, 30};
    private List<String> commandList;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        isTimer = false;
        timerMilliscond = 0;
        commandList = new ArrayList<>();
        initFromStatus();
    }

    /**
     * 获取状态初始化
     */
    private void initFromStatus(){
        if (ConstantStatus.getBulbSwitch(context).equals(ConstantStatus.SWITCH_ON)){
            switchBulb.setBackgroundResource(R.drawable.tv_switch3);
        }
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_controller_bulb);
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        if (v.getId() == R.id.bulb_switch){
            sendCommand(Command.LIGHT_OPEN);
        }
        super.widgetClick(v);
    }

    /**
     * 发送指令
     * @param command
     */
    public void sendCommand(String command){
        if (isTimer == true)//如果产生了定时器则不发送指令
        {
            commandList.add(command);
            return;
        }
        L.d(TAG, Constant.getCurrentRaspIds(this));

        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put(Command.COMMAND, command);
        params.put("rasp_ids",Constant.getCurrentRaspIds(this));
        params.put("action","SEND_COMMAND");
        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                L.d(TAG, "指令发送成功...s = " + s);
                switchClick();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 状态切换
     */
    private void switchClick(){
        if (ConstantStatus.getBulbSwitch(context).equals(ConstantStatus.SWITCH_OFF)) {//如果现在为关闭状态
            ConstantStatus.setBulbSwitch(context, ConstantStatus.SWITCH_ON);//调整为打开
            switchBulb.setBackgroundResource(R.drawable.tv_switch3);
            //bulbImage.setBackgroundResource(R.drawable.bulb_light);
        }else {
            ConstantStatus.setBulbSwitch(context, ConstantStatus.SWITCH_OFF);
            switchBulb.setBackgroundResource(R.drawable.tv_switch1);
            //bulbImage.setBackgroundResource(R.drawable.bulb_dark);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.action_settings){//执行定时功能
            isTimer = true;
            showTimerDialog();
        } else if (item.getItemId() == R.id.action_cancel_timer){
            setTimerAndTimerMillisecond(false, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void showTimerDialog(){
        new AlertDialog.Builder(this)
                .setTitle("设置定时")
                .setItems(new String[]{"5分钟","10分钟","15分钟","20分钟","30分钟"},new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timerMilliscond = timerSelector[which] * 1000;
                        new Thread(new MyThread(timerMilliscond)).start();
                        L.d(TAG, "执行定时器,命令将在 " + timerSelector[which] + " 之后执行");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTimerAndTimerMillisecond(false, 0);
                    }
                })
                .show();

    }

    /**
     * 设置定时时间和变量
     * @param flag
     * @param time
     */
    private void setTimerAndTimerMillisecond(boolean flag, int time){
        if (flag == false){
            isTimer = false;
            timerMilliscond = 0;
        }
        isTimer = flag;
        timerMilliscond = time;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                for (String command : commandList)
                    sendCommand(command);
                L.d(TAG, "CommandList = " + commandList);
            }
            super.handleMessage(msg);
        }
    };

    class MyThread implements Runnable{
        private final int millisecond;
        private MyThread(int millisecond) {
            this.millisecond = millisecond;
        }

        @Override
        public void run() {
            while (isTimer){
                try{
                    Thread.sleep(millisecond);
                    Message message = new Message();
                    message.what = 1;
                    isTimer = false;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
