package module.activity.controler;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.ui.BindView;

import constant.Command;
import constant.Constant;
import constant.MyTimer;
import module.activity.voicechat.TVProgramActivity;
import framework.base.SwipeBackActivity;
import framework.ui.material.ColorGenerator;
import framework.ui.material.TextDrawable;
import module.database.TVChannelEntity;
import utils.L;
import utils.ViewUtils;
import vgod.smarthome.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ControlTVActivity.java
 * @Package:module.activity.controler
 * @time:上午10:01:29 2014-12-16
 * @useage:电视机控制界面
 */
public class ControlTVActivity extends SwipeBackActivity {

    @BindView(id = R.id.control_activity_tv_id)
    private LinearLayout contentLayout;

    @BindView(id = R.id.tv_channel_no_voice, click = true)
    private ImageView switchText;
	@BindView(id = R.id.tv_channel_text)
	private TextView channel_text;
	@BindView(id = R.id.tv_channel_0 , click = true)
	private ImageView channel_zero;
	@BindView(id = R.id.tv_channel_1 , click = true)
	private ImageView channel_one;
	@BindView(id = R.id.tv_channel_2 , click = true)
	private ImageView channel_two;
	@BindView(id = R.id.tv_channel_3 , click = true)
	private ImageView channel_three;
	@BindView(id = R.id.tv_channel_4 , click = true)
	private ImageView channel_four;
	@BindView(id = R.id.tv_channel_5 , click = true)
	private ImageView channel_five;
	@BindView(id = R.id.tv_channel_6 , click = true)
	private ImageView channel_six;
	@BindView(id = R.id.tv_channel_7 , click = true)
	private ImageView channel_seven;
	@BindView(id = R.id.tv_channel_8 , click = true)
	private ImageView channel_eight;
	@BindView(id = R.id.tv_channel_9 , click = true)
	private ImageView channel_nine;
	@BindView(id = R.id.tv_channel_ok , click = true)
	private ImageView channel_ok;
	@BindView(id = R.id.tv_channel_up , click = true)
	private ImageView channel_up;
	@BindView(id = R.id.tv_channel_down , click = true)
	private ImageView channel_down;
	@BindView(id = R.id.tv_channel_left , click = true)
	private ImageView channel_left;
	@BindView(id = R.id.tv_channel_right , click = true)
	private ImageView channel_right;

    private boolean isOkClicked = false;//是否按下OK
    private String tempChannel = "";
    //private String previewChannel = "0";

    private KJHttp kjHttp;
    private MyTimer myTimer;
    /* 记录最近一次按键的时间和当前按键的时间 */
    private long currentMilisecond = 0;
    private long lastMilisecond = 0;

    /**
     * Drawable
     */
    private TextDrawable.IBuilder mDrawableBuilder;
    /**
     * 颜色生成器
     */
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

	@Override
	protected void initData() {
		super.initData();
        contentLayout.setOnTouchListener(this);
        L.d(TAG, Constant.getCurrentRaspIds(this));
        updataChannel("1");

        myTimer = new MyTimer(this);
        TVChannelEntity.kjdb = KJDB.create(this);
        getPushData();
        mDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
	}

	@Override
	protected void initWidget() {
		super.initWidget();
        channel_one.setImageDrawable(mDrawableBuilder.build("1", mColorGenerator.getRandomColor()));
        channel_two.setImageDrawable(mDrawableBuilder.build("2", mColorGenerator.getRandomColor()));
        channel_three.setImageDrawable(mDrawableBuilder.build("3", mColorGenerator.getRandomColor()));
        channel_four.setImageDrawable(mDrawableBuilder.build("4", mColorGenerator.getRandomColor()));
        channel_five.setImageDrawable(mDrawableBuilder.build("5", mColorGenerator.getRandomColor()));
        channel_six.setImageDrawable(mDrawableBuilder.build("6", mColorGenerator.getRandomColor()));
        channel_seven.setImageDrawable(mDrawableBuilder.build("7", mColorGenerator.getRandomColor()));
        channel_eight.setImageDrawable(mDrawableBuilder.build("8", mColorGenerator.getRandomColor()));
        channel_nine.setImageDrawable(mDrawableBuilder.build("9", mColorGenerator.getRandomColor()));
        channel_zero.setImageDrawable(mDrawableBuilder.build("0", mColorGenerator.getRandomColor()));
	}

	@Override
	public void widgetClick(View v) {
        lastMilisecond = currentMilisecond;
        currentMilisecond = System.currentTimeMillis();

        switch (v.getId()) {
		case R.id.tv_channel_0://按下0
            myTimer.sendCommand(Command.TELEVISION_ZERO);
			if (!tempChannel.equals("0"))
                updataChannel("0");
			break;
		case R.id.tv_channel_1://按下1
            myTimer.sendCommand(Command.TELEVISION_ONE);
            updataChannel("1");
			break;
		case R.id.tv_channel_2://按下2
            myTimer.sendCommand(Command.TELEVISION_TWO);
            updataChannel("2");
			break;
		case R.id.tv_channel_3://按下3
            myTimer.sendCommand(Command.TELEVISION_THREE);
            updataChannel("3");
			break;
		case R.id.tv_channel_4://按下4
            myTimer.sendCommand(Command.TELEVISION_FOUR);
            updataChannel("4");
			break;
		case R.id.tv_channel_5://按下5
            myTimer.sendCommand(Command.TELEVISION_FIVE);
            updataChannel("5");
			break;
		case R.id.tv_channel_6://按下6
            myTimer.sendCommand(Command.TELEVISION_SIX);
            updataChannel("6");
			break;
		case R.id.tv_channel_7://按下7
            myTimer.sendCommand(Command.TELEVISION_SEVEN);
            updataChannel("7");
			break;
		case R.id.tv_channel_8://按下8
            myTimer.sendCommand(Command.TELEVISION_EIGHT);
            updataChannel("8");
			break;
		case R.id.tv_channel_9://按下9
            myTimer.sendCommand(Command.TELEVISION_NINE);
            updataChannel("9");
			break;
		case R.id.tv_channel_ok://按下ok
            myTimer.sendCommand(Command.TELEVISION_OK);
            Toast.makeText(this,"当前频道为 : " + tempChannel,Toast.LENGTH_SHORT).show();
            //previewChannel = tempChannel;
            tempChannel = "";
			break;
		case R.id.tv_channel_up://按下up
            myTimer.sendCommand(Command.TELEVISION_UP_CHANNEL);
            operateChannel('u');
			break;
		case R.id.tv_channel_down://按下down
            myTimer.sendCommand(Command.TELEVISION_DOWN_CHANNEL);
            operateChannel('d');
			break;
		case R.id.tv_channel_left://按下left
            myTimer.sendCommand(Command.TELEVISION_DOWN_VOL);
            operateChannel('l');
			break;
		case R.id.tv_channel_right://按下right
            myTimer.sendCommand(Command.TELEVISION_UP_VOL);
            operateChannel('r');
			break;
        case R.id.tv_channel_no_voice:
            myTimer.sendCommand(Command.TELEVISION_SWITCH);
            break;
		default:
			break;
		}
		super.widgetClick(v);
	}

	@Override
	public void setRootView() {
		super.setRootView();
		setContentView(R.layout.control_activity_tv);
		setActionBarView(true);
	}

    /**
     * 更新频道
     * @param a
     */
    private void updataChannel(String a){
        if (isContinuity())
            tempChannel += a;
        else
            tempChannel = a;
        channel_text.setText(tempChannel);
        showChannelText(Integer.valueOf(tempChannel));
    }


    private void operateChannel(char ope){
        switch (ope)
        {
            case 'u':
                try {
                    tempChannel = Integer.parseInt(tempChannel) + 1 + "";
                    channel_text.setText(tempChannel);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    L.d(TAG, "operateChannel Up");
                }
                break;
            case 'd':
                try {
                    if (tempChannel.equals("1"))
                        break;
                    if (!tempChannel.equals("0") && !tempChannel.equals("")){
                        tempChannel = Integer.parseInt(tempChannel) - 1 + "";
                        channel_text.setText(tempChannel);
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    L.d(TAG, "operateChannel Up");
                }
                break;
            case 'l':
                break;
            case 'r':
                break;            
        }
        showChannelText(Integer.valueOf(tempChannel));
    }

    /**
     * 获取推送数据
     */
    private void getPushData() {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.login_default_avatar);
        Intent intent = new Intent(context, TVProgramActivity.class);
        //弹出框
        ViewUtils.getInstance().showNotification(context,
                "CCTV1", "动画片" +
                "  " + "动画片" + "  " + "动画片", largeIcon, intent, 12);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.menu_tv_settings){//执行定时功能
            myTimer.setTimer(true);
            myTimer.showTimerDialog();
        } else if (item.getItemId() == R.id.menu_tv_timer){
            myTimer.setTimerAndTimerMillisecond(false, 0);
        } else if (item.getItemId() == R.id.menu_tv_show_program){
            startActivity(new Intent(ControlTVActivity.this, TVProgramActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_controller_tv, menu);
        return true;
    }

    /**
     * 将频道号码显示成台 12->湖南卫视
     * @param number
     */
    private void showChannelText(int number){
        TVChannelEntity entity = TVChannelEntity.query(number);
        if (entity != null){
            channel_text.setText(tempChannel + "\t" + entity.getChannelText());
        }
    }

    /**
     * 判断是否连续
     * @return true | false
     */
    private boolean isContinuity(){
        return (currentMilisecond - lastMilisecond) < 1000;
    }

    @Override
    protected void onStop() {
        super.onStop();
        resetMemory();
    }

    /**
     * 回收内存
     */
    private void resetMemory(){
        contentLayout = null;
        switchText = null;
        channel_text = null;
        channel_zero = null;
        channel_one = null;
        channel_two = null;
        channel_three = null;
        channel_four = null;
        channel_five = null;
        channel_six = null;
        channel_seven = null;
        channel_eight = null;
        channel_nine = null;
        channel_ok = null;
        channel_up = null;
        channel_down = null;
        channel_left = null;
        channel_right = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
