package module.activity.common;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.kymjs.aframe.ui.BindView;

import constant.ConstantStatus;
import module.activity.controler.ControlADActivity;
import module.activity.controler.ControlCurtainActivity;
import module.activity.controler.ControlDoorActivity;
import module.activity.controler.ControlFanActivity;
import module.activity.controler.ControlHeaterActivity;
import module.activity.controler.ControlPJActivity;
import module.activity.controler.ControlTVActivity;
import module.activity.controler.ControlBulbActivity;
import framework.base.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 23:34
 * 选择遥控器界面
 */
public class SelectControllerActivity extends SwipeBackActivity {

    @BindView(id = R.id.activity_normal_select_controller_id)
    private LinearLayout contentLayout;
    //点击View
    @BindView(id = R.id.select_controller_ad, click = true)
    private RelativeLayout selectAD;
    @BindView(id = R.id.select_controller_tv, click = true)
    private RelativeLayout selectTV;
    @BindView(id = R.id.select_controller_pj, click = true)
    private RelativeLayout selectPJ;
    @BindView(id = R.id.select_controller_fan, click = true)
    private RelativeLayout selectFan;
    @BindView(id = R.id.select_controller_bulb, click = true)
    private RelativeLayout selectBuld;
    @BindView(id = R.id.select_controller_curtain, click = true)
    private RelativeLayout selectCurtain;
    @BindView(id = R.id.select_controller_door, click = true)
    private RelativeLayout selectDoor;
    @BindView(id = R.id.select_controller_heater, click = true)
    private RelativeLayout selectHeater;

    //状态显示View
    @BindView(id = R.id.select_controller_ad_statu)
    private TextView adStatus;
    @BindView(id = R.id.select_controller_tv_statu)
    private TextView tvStatus;
    @BindView(id = R.id.select_controller_pj_statu)
    private TextView pjStatus;
    @BindView(id = R.id.select_controller_fan_statu)
    private TextView fanStatus;
    @BindView(id = R.id.select_controller_bulb_statu)
    private TextView bulbStatus;
    @BindView(id = R.id.select_controller_curtain_statu)
    private TextView curtainStatus;
    @BindView(id = R.id.select_controller_door_statu)
    private TextView doorStatus;
    @BindView(id = R.id.select_controller_heater_statu)
    private TextView heaterStatus;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        showStatus(adStatus, ConstantStatus.getAirSwitch(context));
        showStatus(heaterStatus, ConstantStatus.getHeaterSwitch(context));
        showStatus(pjStatus, ConstantStatus.getPjSwitch(context));
        showStatus(tvStatus, ConstantStatus.getTVSwitch(context));
        showStatus(bulbStatus, ConstantStatus.getBulbSwitch(context));
        showStatus(fanStatus, ConstantStatus.getFanSwitch(context));
        showStatus(doorStatus, ConstantStatus.getDoorSwitch(context));
        showStatus(curtainStatus, ConstantStatus.getCurtainSwitch(context));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWidget();
    }

    private void showStatus(TextView textView, String flag) {
        textView.setText(flag.equals(ConstantStatus.SWITCH_ON) ? "开":"关");
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.select_controller_ad:
                startActivity(new Intent(SelectControllerActivity.this, ControlADActivity.class));
                break;
            case R.id.select_controller_tv:
                startActivity(new Intent(SelectControllerActivity.this, ControlTVActivity.class));
                break;
            case R.id.select_controller_pj:
                startActivity(new Intent(SelectControllerActivity.this, ControlPJActivity.class));
                break;
            case R.id.select_controller_fan:
                startActivity(new Intent(SelectControllerActivity.this, ControlFanActivity.class));
                break;
            case R.id.select_controller_bulb:
                startActivity(new Intent(SelectControllerActivity.this, ControlBulbActivity.class));
                break;
            case R.id.select_controller_curtain:
                startActivity(new Intent(SelectControllerActivity.this, ControlCurtainActivity.class));
                break;
            case R.id.select_controller_door:
                startActivity(new Intent(SelectControllerActivity.this, ControlDoorActivity.class));
                break;
            case R.id.select_controller_heater:
                startActivity(new Intent(SelectControllerActivity.this, ControlHeaterActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_normal_select_controller);
        setActionBarView(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
