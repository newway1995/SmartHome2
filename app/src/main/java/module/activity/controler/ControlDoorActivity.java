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
import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-22
 * Time: 20:36
 * 控制门
 */
public class ControlDoorActivity extends BaseActivity{
    @BindView(id = R.id.activity_door_layout)
    private RelativeLayout contentLayout;
    @BindView(id = R.id.activity_door_switch, click = true)
    private ImageView doorSwitch;
    @BindView(id = R.id.activity_door_image)
    private ImageView doorImage;

    /** 定时器 */
    private MyTimer myTimer;

    @Override
    protected void initData() {
        myTimer = new MyTimer(context);
        contentLayout.setOnTouchListener(this);
        if (ConstantStatus.getDoorSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            doorSwitch.setImageResource(R.drawable.tv_switch1);
            doorImage.setImageResource(R.drawable.door_open);
        } else {
            doorSwitch.setImageResource(R.drawable.tv_switch3);
            doorImage.setImageResource(R.drawable.door_close);
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
        myTimer.sendCommand(Command.DOOR_SWITCH);
        if (v.getId() == R.id.activity_door_switch) {
            if (ConstantStatus.getDoorSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
                ConstantStatus.setDoorSwitch(context, ConstantStatus.SWITCH_OFF);
                doorSwitch.setImageResource(R.drawable.tv_switch3);
                doorImage.setImageResource(R.drawable.door_close);
            } else {
                ConstantStatus.setDoorSwitch(context, ConstantStatus.SWITCH_ON);
                doorSwitch.setImageResource(R.drawable.tv_switch1);
                doorImage.setImageResource(R.drawable.door_open);
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
        setContentView(R.layout.control_activity_door);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onReset();
    }

    private void onReset() {
        contentLayout = null;
        doorSwitch = null;
        doorImage = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
