package module.activity.controler;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import org.kymjs.aframe.ui.BindView;
import constant.Command;
import constant.ConstantStatus;
import constant.MyTimer;
import framework.base.SwipeBackActivity;
import framework.ui.animation.GifView;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-31
 * Time: 17:22
 * FIXME
 */
public class ControlFanActivity extends SwipeBackActivity {

    @BindView(id = R.id.control_activity_fan_id)
    private RelativeLayout contentLayout;

    @BindView(id = R.id.gif_fan)
    private GifView gifView;//动画
    @BindView(id = R.id.fan_switch, click = true)
    private ImageView switchView;//开关
    @BindView(id = R.id.fan_change, click = true)
    private ImageView changeView;//改变大小
    @BindView(id = R.id.fan_shake, click = true)
    private ImageView shakeView;//摇头
    @BindView(id = R.id.fan_time, click = true)
    private ImageView timeView;//定时
    @BindView(id = R.id.fan_type, click = true)
    private ImageView typeView;//风类

    private int dangwei = -1;
    private boolean isOpen = false;

    /**
     * 时间定位
     */
    private MyTimer myTimer;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        gifView.setMovieResource(R.raw.fan2);
        gifView.setPaused(true);
        initFromStatus();
        myTimer = new MyTimer(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.control_activity_fan);
    }

    /**
     * 获取状态初始化
     */
    private void initFromStatus(){
        if (ConstantStatus.getFanSwitch(context).equals(ConstantStatus.SWITCH_ON)){
            String dangwei = ConstantStatus.getFanStatus(context);
            if (dangwei.equals("0"))
                changeView.setBackgroundResource(R.drawable.onedang);
            else if(dangwei.equals("1"))
                changeView.setBackgroundResource(R.drawable.twodang);
            else if(dangwei.equals("2"))
                changeView.setBackgroundResource(R.drawable.threedang);
            gifView.setPaused(false);
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.fan_switch:
                closeFan();
                break;
            case R.id.fan_shake:
                myTimer.sendCommand(Command.FAN_SHAKE);
                if (ConstantStatus.getFanShake(context).equals(ConstantStatus.SWITCH_OFF))
                    ConstantStatus.setFanShake(context, ConstantStatus.SWITCH_ON);
                else
                    ConstantStatus.setFanShake(context, ConstantStatus.SWITCH_OFF);
                break;
            case R.id.fan_change:
                upFan();
                break;
            case R.id.fan_time:
                myTimer.sendCommand(Command.FAN_TIME);
                break;
            case R.id.fan_type:
                myTimer.sendCommand(Command.FAN_TYPE);
                break;
        }
        super.widgetClick(v);
    }


    /**
     * 关闭电视,switchBtn为黑色,档位调0,gif停止
     */
    private void closeFan() {
        if (ConstantStatus.getFanSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            ConstantStatus.setFanSwitch(context, ConstantStatus.SWITCH_OFF);
            changeView.setImageResource(R.drawable.zerodang);
            ConstantStatus.setFanStatus(context, "0");
            if (!gifView.isPaused())
                gifView.setPaused(true);
        }
        myTimer.sendCommand(Command.FAN_SWITCH);
    }

    /**
     * 打开
     */
    private void upFan() {
        boolean isOpen = ConstantStatus.getFanSwitch(context).equals(ConstantStatus.SWITCH_ON);
        /** 如果已经开机,直接调大 */
        if (isOpen) {
            dangwei = Integer.parseInt(ConstantStatus.getFanStatus(context));
            dangwei = (dangwei + 1) % 3;
            ConstantStatus.setFanStatus(context, dangwei+"");
            if (dangwei == 0)
                changeView.setBackgroundResource(R.drawable.onedang);
            else if (dangwei == 1)
                changeView.setBackgroundResource(R.drawable.twodang);
            else if (dangwei == 2)
                changeView.setBackgroundResource(R.drawable.threedang);
            else
                changeView.setBackgroundResource(R.drawable.onedang);
        } else {
            /** 如果还没有开机,就先开机 */
            dangwei = 0;
            ConstantStatus.setFanSwitch(context, ConstantStatus.SWITCH_ON);
            ConstantStatus.setFanStatus(context, "0");
            changeView.setBackgroundResource(R.drawable.onedang);
            if (gifView.isPaused()) {
                gifView.setPaused(false);
            }
        }
        myTimer.sendCommand(Command.FAN_ADD);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.action_settings){//执行定时功能
            myTimer.setTimer(true);
            myTimer.showTimerDialog();
        } else if (item.getItemId() == R.id.action_cancel_timer){
            myTimer.setTimerAndTimerMillisecond(false, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        contentLayout = null;
        gifView = null;
        switchView = null;
        changeView = null;
        shakeView = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }

}
