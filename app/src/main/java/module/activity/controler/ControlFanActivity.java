package module.activity.controler;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import org.kymjs.aframe.ui.BindView;
import constant.Command;
import constant.MyTimer;
import module.core.BaseActivity;
import module.view.animation.GifView;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-31
 * Time: 17:22
 * FIXME
 */
public class ControlFanActivity extends BaseActivity{

    @BindView(id = R.id.control_activity_fan_id)
    private RelativeLayout contentLayout;

    @BindView(id = R.id.gif_fan)
    private GifView gifView;
    @BindView(id = R.id.fan_switch, click = true)
    private ImageView switchView;
    @BindView(id = R.id.fan_change, click = true)
    private ImageView changeView;
    @BindView(id = R.id.fan_shake, click = true)
    private ImageView shakeView;

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

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.fan_switch:
                isOpen = !isOpen;
                if (!gifView.isPaused()){
                    gifView.setPaused(true);
                    changeView.setBackgroundResource(R.drawable.zerodang);
                    dangwei = -1;
                }else{
                    gifView.setPaused(false);
                    changeView.setBackgroundResource(R.drawable.onedang);
                    dangwei = 0;//0代表一档
                }
                myTimer.sendCommand(Command.FAN_SWITCH);
                break;
            case R.id.fan_shake:
                myTimer.sendCommand(Command.FAN_SHAKE);
                break;
            case R.id.fan_change:
                if (dangwei == -1)//还没有打开
                    return;
                dangwei = (dangwei + 1) % 3;
                if (dangwei == 0)
                    changeView.setBackgroundResource(R.drawable.onedang);
                else if (dangwei == 1)
                    changeView.setBackgroundResource(R.drawable.twodang);
                else if (dangwei == 2)
                    changeView.setBackgroundResource(R.drawable.threedang);
                myTimer.sendCommand(Command.FAN_ADD);
                break;
        }
        super.widgetClick(v);
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

}
