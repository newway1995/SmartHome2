package module.activity;

import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;

import module.activity.controler.ControlADActivity;
import module.activity.controler.ControlTVActivity;
import module.activity.gesturepwd.UnLockGesturePasswordActivity;
import module.activity.security.SecurityCameraActivity;
import module.activity.user.ForgetPasswordActivity;
import module.activity.user.LoginActivity;
import module.activity.user.RegisterActivity;
import module.activity.user.SettingPortrait;
import module.core.ui.ResideMenu;
import module.core.ui.ResideMenuItem;
import module.core.ui.SegmentedGroup;
import utils.ViewUtils;
import vgod.smarthome.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:MainActivity.java
 * @Package:vgod.shome
 * @time:上午1:54:53 2014-12-14
 * @useage:Main
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener{
    @BindView(id = R.id.nav_actionbar_segment , click = true)
    private SegmentedGroup segmentedGroup;//segment
    @BindView(id = R.id.nav_actionbar_menu , click = true)
    private ImageView navLeft;//左边menu
    @BindView(id = R.id.nav_actionbar_more , click = true)
    private ImageView navRight;    //右边menu
    private ResideMenu resideMenu;
    @BindView(id = R.id.nav_actionbar_segment_device , click = true)
    private RadioButton deviceRadio; //设备
    @BindView(id = R.id.nav_actionbar_segment_scene , click = true)
    private RadioButton sceneRadio;//场景

    private ResideMenuItem mainFrame;
    private ResideMenuItem deviceFrame;
    private ResideMenuItem settingFrame;
    private ResideMenuItem helpFrame;
    private ResideMenuItem aboutFrame;

    //测试
    @BindView(id = R.id.main_camera , click = true)
    private Button cameraBtn;
    @BindView(id = R.id.main_camera , click = true)
    private Button registerBtn;
    @BindView(id = R.id.main_login , click = true)
    private Button loginBtn;
    @BindView(id = R.id.main_forget_pwd , click = true)
    private Button forgetPwdBtn;
    @BindView(id = R.id.main_unlock , click = true)
    private Button unlockBtn;
    @BindView(id = R.id.main_setting_portrait , click = true)
    private Button settingPortraitBtn;
    @BindView(id = R.id.main_ac , click = true)
    private Button ac;
    @BindView(id = R.id.main_tv , click = true)
    private Button tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AnnotateUtil.initBindView(this);
        initView();
        initData();
    }

    //初始化界面
    private void initView(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackgroundResource(R.drawable.menu_bg);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValueX(0.5f);//x = 0.7,y = 0.7的时候发生变换
        resideMenu.setScaleValueY(0.7f);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);//右侧禁止滑动

        mainFrame = new ResideMenuItem(this , R.drawable.icon_home1,"主界面");
        deviceFrame = new ResideMenuItem(this , R.drawable.icon_list , "设备列表");
        settingFrame = new ResideMenuItem(this , R.drawable.icon_setting , "设置");
        helpFrame = new ResideMenuItem(this , R.drawable.icon_faq , "帮助");
        aboutFrame = new ResideMenuItem(this , R.drawable.icon_about , "关于");

        mainFrame.setOnClickListener(this);
        deviceFrame.setOnClickListener(this);
        settingFrame.setOnClickListener(this);
        helpFrame.setOnClickListener(this);
        aboutFrame.setOnClickListener(this);

        resideMenu.addMenuItem(mainFrame);
        resideMenu.addMenuItem(deviceFrame);
        resideMenu.addMenuItem(settingFrame);
        resideMenu.addMenuItem(helpFrame);
        resideMenu.addMenuItem(aboutFrame);

        segmentedGroup.check(R.id.nav_actionbar_segment_device);
        segmentedGroup.setOnCheckedChangeListener(this);
    }

    //初始化数据
    private void initData(){

    }

    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.nav_actionbar_menu:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.nav_actionbar_more:
                PopupWindow popupWindow = ViewUtils.getInstance().getPopWindow(MainActivity.this,new String[]{"Vgod","Vgod"},200,200);
                popupWindow.showAtLocation(navRight,Gravity.TOP | Gravity.RIGHT,0,0);
                break;
            case R.id.main_camera:
                startActivity(new Intent(MainActivity.this, SecurityCameraActivity.class));
                break;
            case R.id.main_forget_pwd:
                startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.main_unlock:
                startActivity(new Intent(MainActivity.this, UnLockGesturePasswordActivity.class));
                break;
            case R.id.main_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.main_register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.main_setting_portrait:
                startActivity(new Intent(MainActivity.this, SettingPortrait.class));
                break;
            case R.id.main_ac:
                startActivity(new Intent(MainActivity.this, ControlADActivity.class));
                break;
            case R.id.main_tv:
                startActivity(new Intent(MainActivity.this, ControlTVActivity.class));
                break;
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(MainActivity.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(MainActivity.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * SegmentedGroup点击事件
     * */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup , int checkedId){

    }

}
