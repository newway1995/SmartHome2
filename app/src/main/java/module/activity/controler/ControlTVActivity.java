package module.activity.controler;

import org.kymjs.aframe.ui.BindView;

import utils.L;
import vgod.smarthome.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import module.core.BaseActivity;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ControlTVActivity.java
 * @Package:module.activity.controler
 * @time:上午10:01:29 2014-12-16
 * @useage:电视机控制界面
 */
public class ControlTVActivity extends BaseActivity{

	@BindView(id = R.id.tv_channel_text)
	private TextView channel_text;
	@BindView(id = R.id.tv_channel_0 , click = true)
	private TextView channel_zero;
	@BindView(id = R.id.tv_channel_1 , click = true)
	private TextView channel_one;
	@BindView(id = R.id.tv_channel_2 , click = true)
	private TextView channel_two;
	@BindView(id = R.id.tv_channel_3 , click = true)
	private TextView channel_three;
	@BindView(id = R.id.tv_channel_4 , click = true)
	private TextView channel_four;
	@BindView(id = R.id.tv_channel_5 , click = true)
	private TextView channel_five;
	@BindView(id = R.id.tv_channel_6 , click = true)
	private TextView channel_six;
	@BindView(id = R.id.tv_channel_7 , click = true)
	private TextView channel_seven;
	@BindView(id = R.id.tv_channel_8 , click = true)
	private TextView channel_eight;
	@BindView(id = R.id.tv_channel_9 , click = true)
	private TextView channel_nine;
	@BindView(id = R.id.tv_channel_10 , click = true)
	private TextView channel_ten;
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
    private String previewChannel = "";

	
	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	protected void initWidget() {
		super.initWidget();
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.tv_channel_0://按下0
			if (!tempChannel.equals("0"))
                updataChannel("0");
			break;
		case R.id.tv_channel_1://按下1
            updataChannel("1");
			break;
		case R.id.tv_channel_2://按下2
            updataChannel("2");
			break;
		case R.id.tv_channel_3://按下3
            updataChannel("3");
			break;
		case R.id.tv_channel_4://按下4
            updataChannel("4");
			break;
		case R.id.tv_channel_5://按下5
            updataChannel("5");
			break;
		case R.id.tv_channel_6://按下6
            updataChannel("6");
			break;
		case R.id.tv_channel_7://按下7
            updataChannel("7");
			break;
		case R.id.tv_channel_8://按下8
            updataChannel("8");
			break;
		case R.id.tv_channel_9://按下9
            updataChannel("9");
			break;
		case R.id.tv_channel_10://按下10
            updataChannel("10");
			break;
		case R.id.tv_channel_ok://按下ok
            Toast.makeText(this,"当前频道为 : " + tempChannel,Toast.LENGTH_SHORT).show();
            previewChannel = tempChannel;
            tempChannel = "";
			break;
		case R.id.tv_channel_up://按下up
            operateChannel('u');
			break;
		case R.id.tv_channel_down://按下down
            operateChannel('d');
			break;
		case R.id.tv_channel_left://按下left
            operateChannel('l');
			break;
		case R.id.tv_channel_right://按下right
            operateChannel('r');
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
	
    private void updataChannel(String a){
        tempChannel += a;
        channel_text.setText(tempChannel);
    }
    
    
    
    private void operateChannel(char ope){
        switch (ope)
        {
            case 'u':
                try {
                    previewChannel = Integer.parseInt(previewChannel) + 1 + "";
                    channel_text.setText(previewChannel);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    L.d(TAG,"operateChannel Up");
                }
                break;
            case 'd':
                try {
                    if (!previewChannel.equals("0") && !previewChannel.equals("")){
                        previewChannel = Integer.parseInt(previewChannel) - 1 + "";
                        channel_text.setText(previewChannel);
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    L.d(TAG,"operateChannel Up");
                }
                break;
            case 'l':
                break;
            case 'r':
                break;            
        }
    }
}
