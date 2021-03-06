package module.activity.controler;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.kymjs.aframe.ui.BindView;

import constant.Command;
import constant.ConstantStatus;
import constant.MyTimer;
import framework.base.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 22:53
 * 控制投影仪
 */
public class ControlPJActivity extends SwipeBackActivity {

    @BindView(id = R.id.control_activity_pj)
    private LinearLayout contentLayout;

    @BindView(id = R.id.pj_channel_ok, click = true)
    private ImageView powerView;
    @BindView(id = R.id.pj_channel_left, click = true)
    private ImageView leftView;
    @BindView(id = R.id.pj_channel_right, click = true)
    private ImageView rightView;
    @BindView(id = R.id.pj_channel_down, click = true)
    private ImageView downView;
    @BindView(id = R.id.pj_channel_up, click = true)
    private ImageView upView;

    private MyTimer myTimer;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        myTimer = new MyTimer(this);
    }

    /**
     * 获取状态初始化
     */
    private void initFromStatus(){
        if (ConstantStatus.getFanStatus(context).equals("on")){
            powerView.setSelected(true);
        }
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.control_activity_pj);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.pj_channel_ok:
                myTimer.sendCommand(Command.PROJECTOR_OPEN);
                if (ConstantStatus.getPjSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
                    ConstantStatus.setPjSwitch(context, ConstantStatus.SWITCH_OFF);
                    powerView.setBackgroundResource(R.drawable.tv_switch1);
                } else {
                    ConstantStatus.setPjSwitch(context, ConstantStatus.SWITCH_ON);
                    powerView.setBackgroundResource(R.drawable.tv_switch3);
                }
                break;
            case R.id.pj_channel_right:
                myTimer.sendCommand(Command.PROJECTOR_UP_ZOOM);
                break;
            case R.id.pj_channel_left:
                myTimer.sendCommand(Command.PROJECTOR_DOWN_ZOOM);
                break;
            case R.id.pj_channel_down:
                myTimer.sendCommand("");
                break;
            case R.id.pj_channel_up:
                myTimer.sendCommand("");
                break;
        }
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
        powerView = null;
        leftView = null;
        rightView = null;
        downView = null;
        upView = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
