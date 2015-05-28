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
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-22
 * Time: 20:35
 * 控制窗帘
 */
public class ControlCurtainActivity extends SwipeBackActivity {
    @BindView(id = R.id.activity_curtain_layout)
    private RelativeLayout contentLayout;
    @BindView(id = R.id.activity_curtain_switch, click = true)
    private ImageView curtainSwitch;
    @BindView(id = R.id.activity_curtain_image)
    private ImageView curtainImage;

    /** 定时器 */
    private MyTimer myTimer;

    @Override
    protected void initData() {
        contentLayout.setOnTouchListener(this);
        myTimer = new MyTimer(context);
        if (ConstantStatus.getCurtainSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            curtainSwitch.setImageResource(R.drawable.tv_switch1);
            curtainImage.setImageResource(R.drawable.curtain_open);
        } else {
            curtainSwitch.setImageResource(R.drawable.tv_switch3);
            curtainImage.setImageResource(R.drawable.curtain_close);
        }
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        myTimer.sendCommand(Command.CURTAIN_SWITCH);
        if (v.getId() == R.id.activity_curtain_switch) {
            if (ConstantStatus.getCurtainSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
                ConstantStatus.setCurtainSwitch(context, ConstantStatus.SWITCH_OFF);
                curtainSwitch.setImageResource(R.drawable.tv_switch3);
                curtainImage.setImageResource(R.drawable.curtain_close);
            } else {
                ConstantStatus.setCurtainSwitch(context, ConstantStatus.SWITCH_ON);
                curtainSwitch.setImageResource(R.drawable.tv_switch1);
                curtainImage.setImageResource(R.drawable.curtain_open);
            }
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
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.control_activity_curtain);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onReset();
    }

    private void onReset() {
        contentLayout = null;
        curtainSwitch = null;
        curtainImage = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
