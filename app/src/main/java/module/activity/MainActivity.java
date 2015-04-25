package module.activity;

import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;

import constant.Constant;
import module.activity.common.AboutActivity;
import module.activity.common.ContactUsActivity;
import module.activity.common.FastConnectWifiActivity;
import module.activity.common.SelectControllerActivity;
import module.activity.common.SettingActivity;
import module.activity.common.WeatherInfoActivity;
import module.activity.voicechat.VoiceControlActivity;
import module.activity.voicechat.WakeUpControl;
import module.core.ui.ImportMenuView;
import module.core.ui.ResideMenu;
import module.core.ui.ResideMenuItem;
import module.core.ui.RippleLayout;
import module.core.ui.SegmentedGroup;
import module.database.EntityDao;
import module.inter.NormalProcessor;
import module.inter.StringProcessor;
import module.view.adapter.RaspberryAdapter;
import vgod.smarthome.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:MainActivity.java
 * @Package:vgod.shome
 * @time:上午1:54:53 2014-12-14
 * @useage:Main
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener{
    @BindView(id = R.id.main_contentView)
    private RelativeLayout contentLayout;

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
    @BindView(id = R.id.activity_main_voice, click = true)
    private ImageView activity_main_voice;

    private ResideMenuItem mainFrame;//主界面
    private ResideMenuItem deviceFrame;//设备列表
    private ResideMenuItem settingFrame;//设置
    private ResideMenuItem helpFrame;//帮助
    private ResideMenuItem aboutFrame;//关于
    private ResideMenuItem fastConnectWifi;//快速连接Wifi技术
    private ResideMenuItem voiceControl;//小威语音助手

    @BindView(id = R.id.add)
    public static ImageButton add;
    @BindView(id = R.id.more2)
    public static RippleLayout ripple;
    @BindView(id = R.id.main_activity_import_menu)
    ImportMenuView menuView;


    /* 网络实例 */
    private KJHttp kjHttp;
    /* 数据库实例 */
    private EntityDao entityDao;
    /* 从网络当中树莓派保存列表 */
    private ArrayList<HashMap<String, String>> raspList;
    private RaspberryAdapter raspberryAdapter;
    @BindView(id = R.id.rasp_list)
    private ListView raspListView;
    @BindView(id = R.id.sceen_list)
    private ListView sceenListView;

    private Context context = this;
    private MainModel mainModel = new MainModel();
    /** 语音唤醒 **/
    //private WakeUpControl mWakeUpControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AnnotateUtil.initBindView(this);
        initView();
        initData();
        //initWakeUp();
    }

    private boolean isReset = false;//是否重置到nav_actionbar_segment_device
    @Override
    protected void onStart() {
        super.onStart();
        getRaspberryFromDB();
        if (isReset)
            ((RadioButton)findViewById(R.id.nav_actionbar_segment_device)).setChecked(true);
    }

    //初始化界面
    private void initView(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackgroundResource(R.drawable.menu_bg);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValueX(0.5f);//x = 0.7,y = 0.7的时候发生变换
        resideMenu.setScaleValueY(0.7f);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);//右侧禁止滑动

        mainFrame = new ResideMenuItem(this , R.drawable.icon_home1,"主界面");
        deviceFrame = new ResideMenuItem(this , R.drawable.icon_list , "设备列表");
        voiceControl = new ResideMenuItem(this, R.drawable.voice_control_icon, "语音助手");//小威
        settingFrame = new ResideMenuItem(this , R.drawable.icon_setting , "设置");
        helpFrame = new ResideMenuItem(this , R.drawable.icon_faq , "帮助");
        aboutFrame = new ResideMenuItem(this , R.drawable.icon_about , "关于");
        fastConnectWifi = new ResideMenuItem(this, R.drawable.fast_connect, "快速连接");

        mainFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resideMenu.closeMenu();
            }
        });
        deviceFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resideMenu.closeMenu();
            }
        });
        settingFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(context, SettingActivity.class));
            }
        });//设置
        helpFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(context, ContactUsActivity.class));
            }
        });//帮助
        aboutFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AboutActivity.class));
            }
        });//关于
        fastConnectWifi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FastConnectWifiActivity.class));
            }
        });//快速连接Wifi
        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, VoiceControlActivity.class));
            }
        });

        resideMenu.addMenuItem(mainFrame);
        resideMenu.addMenuItem(deviceFrame);
        resideMenu.addMenuItem(voiceControl);
        resideMenu.addMenuItem(settingFrame);
        resideMenu.addMenuItem(helpFrame);
        resideMenu.addMenuItem(aboutFrame);
        resideMenu.addMenuItem(fastConnectWifi);

        segmentedGroup.check(R.id.nav_actionbar_segment_device);
        segmentedGroup.setOnCheckedChangeListener(this);
        initListViewClick();
    }

    //初始化数据
    private void initData(){
        kjHttp = new KJHttp();
        menuView.setEnabled(false);
        ripple.setRippleFinishListener(new RippleLayout.RippleFinishListener() {
            @Override
            public void rippleFinish(int id) {
                if (id == R.id.more2) {
                    menuView.setVisibility(View.VISIBLE);
                    menuView.setEnabled(true);
                    menuView.setFocusable(true);
                    menuView.rl_closeVisiableAnimation();
                    menuView.animation(context);
                    menuView.bringToFront();
                    ripple.setVisibility(View.GONE);
                }
            }
        });
        setRippleProcessor();
        raspList = new ArrayList<>();
        getRaspberryFromDB();
        entityDao = new EntityDao(this);
    }

    /**
     * 初始化唤醒
     */
    /*private void initWakeUp(){
        if (!Constant.getWakeUp(context))
            return;
        mWakeUpControl = new WakeUpControl(this);
        mWakeUpControl.setVoiceProcessor(new StringProcessor() {
            @Override
            public void stringProcess(String str) {
                super.stringProcess(str);
                startActivity(new Intent(MainActivity.this, VoiceControlActivity.class));
                mWakeUpControl.stop();
            }
        });
        mWakeUpControl.startSpeak();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (!Constant.getWakeUp(context))
            return;
        //initWakeUp();
        //mWakeUpControl.startSpeak();
    }


    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.nav_actionbar_menu:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.nav_actionbar_more:
                startActivity(new Intent(context, WeatherInfoActivity.class));
                overridePendingTransition(R.anim.activity_open_in, R.anim.activity_open_exist);
                break;
            case R.id.activity_main_voice:
                startActivity(new Intent(MainActivity.this, VoiceControlActivity.class));
                break;
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

    /**
     * 初始化ListView的点击事件
     */
    private void initListViewClick(){
        raspListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rasp_ids = raspList.get(position).get("rasp_ids");
                Constant.setCurrentRaspIds(context, rasp_ids);
                startActivity(new Intent(context,SelectControllerActivity.class));
            }
        });
        /** 场景的ListView */
        sceenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    /**
     * SegmentedGroup点击事件
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup , int checkedId){
        switch (checkedId){
            case R.id.nav_actionbar_segment_device:
                raspListView.setVisibility(View.VISIBLE);
                sceenListView.setVisibility(View.GONE);
                break;
            case R.id.nav_actionbar_segment_scene:
                //startActivity(new Intent(context, VoiceControlActivity.class));
                raspListView.setVisibility(View.GONE);
                sceenListView.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 设置ripple的点击事件
     */
    private void setRippleProcessor(){
        menuView.setFirstProcessor(new NormalProcessor(){
            @Override
            public void onProcess(){
                showRaspberryListDialog();
            }
        });
        menuView.setSecondProcessor(new NormalProcessor() {
            @Override
            public void onProcess() {
                mainModel.showAddRaspberryDialog(MainActivity.this, kjHttp);
            }
        });
    }


    /**
     * 在添加遥控器的时候需要提示树莓派列表
     */
    private void showRaspberryListDialog(){
        mainModel.showRaspberryListDialog(context, kjHttp, entityDao, new NormalProcessor(){
            @Override
            public void onProcess() {
                super.onProcess();
                startActivity(new Intent(context, SelectControllerActivity.class));
            }
        });
    }

    /**
     * 从SQLite当中获取树莓派列表
     */
    private void getRaspberryFromDB(){
        raspList = mainModel.getRaspberryFromDB(context);
        if (raspList != null && raspList.size() != 0) {
            raspberryAdapter = new RaspberryAdapter(this, raspList);
            raspListView.setAdapter(raspberryAdapter);
            raspListView.postInvalidate();
        }
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        isReset = true;
    }
}
