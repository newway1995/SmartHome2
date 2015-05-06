package module.activity.controler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.kymjs.aframe.ui.BindView;
import constant.ConstantStatus;
import constant.MyTimer;
import module.core.BaseActivity;
import module.core.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-23
 * Time: 10:36
 * 热水器
 */
public class ControlHeaterActivity extends SwipeBackActivity {

    @BindView(id = R.id.activity_heater_layout)
    private RelativeLayout contentLayout;
    @BindView(id = R.id.heater_channel_down, click = true)
    private ImageView downHeater;
    @BindView(id = R.id.heater_channel_up, click = true)
    private ImageView upHeater;
    @BindView(id = R.id.heater_channel_left, click = true)
    private ImageView leftHeater;
    @BindView(id = R.id.heater_channel_right, click = true)
    private ImageView rightHeater;
    @BindView(id = R.id.heater_channel_ok, click = true)
    private ImageView okHeater;
    @BindView(id = R.id.control_activity_text)
    private TextView tempText;

    private MyTimer myTimer;
    private int temp = 0;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        myTimer = new MyTimer(context);

        temp = ConstantStatus.getHeaterTemp(context);
        tempText.setText("温度: " + temp + " ℃");
        if (ConstantStatus.getHeaterSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            okHeater.setBackgroundResource(R.drawable.tv_switch3);
        } else {
            okHeater.setBackgroundResource(R.drawable.tv_switch1);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.control_activity_heater);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.heater_channel_down:
                down();
                myTimer.sendCommand("");
                break;
            case R.id.heater_channel_up:
                up();
                myTimer.sendCommand("");
                break;
            case R.id.heater_channel_left:
                down();
                myTimer.sendCommand("");
                break;
            case R.id.heater_channel_right:
                up();
                myTimer.sendCommand("");
                break;
            case R.id.heater_channel_ok:
                if (ConstantStatus.getHeaterSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
                    ConstantStatus.setHeaterSwitch(context, ConstantStatus.SWITCH_OFF);
                    okHeater.setBackgroundResource(R.drawable.tv_switch1);
                } else {
                    ConstantStatus.setHeaterSwitch(context, ConstantStatus.SWITCH_ON);
                    okHeater.setBackgroundResource(R.drawable.tv_switch3);
                }
                myTimer.sendCommand("");
                break;
            default:
                break;
        }
    }


    private void up() {
        if (!ConstantStatus.getHeaterSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            Toast("请先打开热水器");
            return;
        }
        temp = ConstantStatus.getHeaterTemp(context);
        ConstantStatus.setHeaterTemp(context, temp++);
    }

    private void down() {
        if (!ConstantStatus.getHeaterSwitch(context).equals(ConstantStatus.SWITCH_ON)) {
            Toast("请先打开热水器");
            return;
        }
        temp = ConstantStatus.getHeaterTemp(context);
        ConstantStatus.setHeaterTemp(context, temp--);
    }
}
