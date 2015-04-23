package module.activity.common;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.kymjs.aframe.ui.BindView;

import module.activity.controler.ControlADActivity;
import module.activity.controler.ControlCurtainActivity;
import module.activity.controler.ControlDoorActivity;
import module.activity.controler.ControlFanActivity;
import module.activity.controler.ControlHeaterActivity;
import module.activity.controler.ControlPJActivity;
import module.activity.controler.ControlTVActivity;
import module.activity.controler.ControllerBulbActivity;
import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 23:34
 * 选择遥控器界面
 */
public class SelectControllerActivity extends BaseActivity{

    @BindView(id = R.id.activity_normal_select_controller_id)
    private LinearLayout contentLayout;

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

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
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
                startActivity(new Intent(SelectControllerActivity.this, ControllerBulbActivity.class));
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
