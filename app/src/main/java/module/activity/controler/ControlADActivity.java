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
import module.core.BaseActivity;
import module.core.SwipeBackActivity;
import utils.L;
import vgod.smarthome.R;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ControlADActivity.java
 * @Package:module.activity.controler
 * @time:下午1:25:39 2014-12-16
 * @useage:控制空调
 */
public class ControlADActivity extends SwipeBackActivity {

    @BindView(id = R.id.control_activity_ad)
    private LinearLayout contentLayout;

    @BindView(id = R.id.ad_temp_up, click = true)
    private ImageView temp_up_view;
    @BindView(id = R.id.ad_temp_down, click = true)
    private ImageView temp_down_view;
    @BindView(id = R.id.ad_switch, click = true)
    private ImageView ad_open;
    @BindView(id = R.id.ad_hot, click = true)
    private ImageView mode_wram;
    @BindView(id = R.id.ad_cold, click = true)
    private ImageView mode_cold;
    @BindView(id = R.id.ad_wet, click = true)
    private ImageView mode_wetout;

    @BindView(id = R.id.ad_temp_text)
    private ImageView currentTempView;
    /**
     * 当前温度
     */
    private int currentTemp = 17;
    private static final int MAX_TEMP = 31;
    private static final int MIN_TEMP = 16;


    /**
     * 时间定位
     */
    private MyTimer myTimer;

	@Override
	protected void initData() {
		super.initData();
        contentLayout.setOnTouchListener(this);
        myTimer = new MyTimer(this);
	}

    /**
     * 从保存的状态文件恢复
     */
    private void initFromStatus(){
        currentTemp = Integer.valueOf(ConstantStatus.getAirStatus(context));
        setDrawableByTemp();
        if (ConstantStatus.getAirSwitch(context).equals("on"))
            ad_open.setSelected(true);
    }
	
	@Override
	protected void initWidget() {
		super.initWidget();
        temp_down_view = (ImageView)findViewById(R.id.ad_temp_down);
        temp_up_view = (ImageView)findViewById(R.id.ad_temp_up);
        ad_open = (ImageView)findViewById(R.id.ad_switch);

        temp_down_view.setOnClickListener(this);
        temp_up_view.setOnClickListener(this);
        ad_open.setOnClickListener(this);
        initFromStatus();
	}



    @Override
    public void widgetClick(View v) {
        L.d(TAG, "CurrentTemp = " + currentTemp);
        super.widgetClick(v);
        switch (v.getId())
        {
            case R.id.ad_temp_up:
                upTemp();
                myTimer.sendCommand(Command.AIRCONDITION_UP);
                break;
            case R.id.ad_temp_down:
                downTemp();
                myTimer.sendCommand(Command.AIRCONDITION_DOWN);
                break;
            case R.id.ad_switch:
                myTimer.sendCommand(Command.AIRCONDITION_SWITCH);
                if (ConstantStatus.getAirSwitch(context).equals("on"))
                    ConstantStatus.setAirSwitch(context, "off");
                else
                    ConstantStatus.setAirSwitch(context, "on");
                break;
            case R.id.ad_cold:
                myTimer.sendCommand(Command.AIRCONDITION_COLD);
                ConstantStatus.setAirMode(context, "cold");
                break;
            case R.id.ad_hot:
                myTimer.sendCommand(Command.AIRCONDITION_HOT);
                ConstantStatus.setAirMode(context, "hot");
                break;
            case R.id.ad_wet:
                myTimer.sendCommand(Command.AIRCONDITION_WETOUT);
                ConstantStatus.setAirMode(context, "wet");
                break;
        }
        ConstantStatus.setAirStatus(context, currentTemp + "");
    }

	
    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.control_activity_ad);
        setActionBarView(true);
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

    /**
     * 调大温度之后界面相应地变换
     */
    private void upTemp(){
        if (currentTemp >= MAX_TEMP || currentTemp < MIN_TEMP)
            return;
        currentTemp = currentTemp + 1;
        setDrawableByTemp();
    }

    /**
     * 调小温度之后界面相应地变换
     */
    private void downTemp(){
        if (currentTemp > MAX_TEMP || currentTemp <= MIN_TEMP)
            return;
        currentTemp = currentTemp - 1;
        setDrawableByTemp();
    }

    private void setDrawableByTemp(){
        L.d(TAG, "setDrawableByTemp = " + currentTemp);
        switch (currentTemp)
        {
            case 16:
                currentTempView.setBackgroundResource(R.drawable.img_temp_16);
                break;
            case 17:
                currentTempView.setBackgroundResource(R.drawable.img_temp_17);
                break;
            case 18:
                currentTempView.setBackgroundResource(R.drawable.img_temp_18);
                break;
            case 19:
                currentTempView.setBackgroundResource(R.drawable.img_temp_19);
                break;
            case 20:
                currentTempView.setBackgroundResource(R.drawable.img_temp_20);
                break;
            case 21:
                currentTempView.setBackgroundResource(R.drawable.img_temp_21);
                break;
            case 22:
                currentTempView.setBackgroundResource(R.drawable.img_temp_22);
                break;
            case 23:
                currentTempView.setBackgroundResource(R.drawable.img_temp_23);
                break;
            case 24:
                currentTempView.setBackgroundResource(R.drawable.img_temp_24);
                break;
            case 25:
                currentTempView.setBackgroundResource(R.drawable.img_temp_25);
                break;
            case 26:
                currentTempView.setBackgroundResource(R.drawable.img_temp_26);
                break;
            case 27:
                currentTempView.setBackgroundResource(R.drawable.img_temp_27);
                break;
            case 28:
                currentTempView.setBackgroundResource(R.drawable.img_temp_28);
                break;
            case 29:
                currentTempView.setBackgroundResource(R.drawable.img_temp_29);
                break;
            case 30:
                currentTempView.setBackgroundResource(R.drawable.img_temp_30);
                break;
            case 31:
                currentTempView.setBackgroundResource(R.drawable.img_temp_31);
                break;
            default:
                currentTempView.setBackgroundResource(R.drawable.img_temp_16);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        contentLayout = null;
        temp_up_view = null;
        temp_down_view = null;
        ad_open = null;
        mode_wram = null;
        mode_cold = null;
        mode_wetout = null;
        currentTempView = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
