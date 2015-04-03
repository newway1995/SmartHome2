package module.activity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;

import constant.Command;
import constant.Constant;
import module.activity.common.AboutActivity;
import module.activity.common.ContactUsActivity;
import module.activity.common.SelectControllerActivity;
import module.activity.common.SettingActivity;
import module.activity.common.WeatherInfoActivity;
import module.activity.security.SecurityCameraActivity;
import module.core.ui.ImportMenuView;
import module.core.ui.ResideMenu;
import module.core.ui.ResideMenuItem;
import module.core.ui.RippleLayout;
import module.core.ui.SegmentedGroup;
import module.database.EntityDao;
import module.database.RaspberryEntity;
import module.inter.NormalProcessor;
import module.view.adapter.RaspberryAdapter;
import utils.L;
import vgod.smarthome.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AnnotateUtil.initBindView(this);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.d("OnStart()");
        getRaspberryFromDB();
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
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        helpFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
            }
        });
        aboutFrame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        resideMenu.addMenuItem(mainFrame);
        resideMenu.addMenuItem(deviceFrame);
        resideMenu.addMenuItem(settingFrame);
        resideMenu.addMenuItem(helpFrame);
        resideMenu.addMenuItem(aboutFrame);

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
                if(id == R.id.more2){
                    menuView.setVisibility(View.VISIBLE);
                    menuView.setEnabled(true);
                    menuView.setFocusable(true);
                    menuView.rl_closeVisiableAnimation();
                    menuView.animation(MainActivity.this);
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

    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.nav_actionbar_menu:
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                break;
            case R.id.nav_actionbar_more:
                startActivity(new Intent(MainActivity.this, WeatherInfoActivity.class));
                overridePendingTransition(R.anim.activity_open_in, R.anim.activity_open_exist);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rasp_ids = raspList.get(position).get("rasp_ids");
                Constant.setCurrentRaspIds(MainActivity.this, rasp_ids);
                startActivity(new Intent(MainActivity.this,SelectControllerActivity.class));
            }
        });
    }

    /**
     * 监听ResideMenu打开或者关闭的情况
     */
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
        }
    };

    /**
     * SegmentedGroup点击事件
     * */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup , int checkedId){
        switch (checkedId){
            case R.id.nav_actionbar_segment_device:
                break;
            case R.id.nav_actionbar_segment_scene:
                //startActivity(new Intent(MainActivity.this,SecurityCameraActivity.class));
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
                showAddRaspberryDialog();
            }
        });
    }

    /**
     * 显示添加树莓派的对话框
     */
    private void showAddRaspberryDialog(){
        ViewGroup alertView = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.pop_add_raspberry,null,true);
        final EditText usernameText = (EditText)alertView.findViewById(R.id.add_raspberry_username);
        final EditText passwordText = (EditText)alertView.findViewById(R.id.add_raspberry_pwd);
        final EditText nicknameText = (EditText)alertView.findViewById(R.id.add_raspberry_nickname);
        final EditText functionText = (EditText)alertView.findViewById(R.id.add_raspberry_function);

        new AlertDialog.Builder(this)
                .setTitle("添加树莓派")
                .setView(alertView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String username = usernameText.getText().toString().trim();
                        final String password = passwordText.getText().toString().trim();
                        final String nickname = nicknameText.getText().toString().trim();
                        final String function = functionText.getText().toString().trim();
                        /* 验证账户密码是否正确 */
                        KJStringParams params = new KJStringParams();
                        params.put(Command.COMMAND_DEVICE, Command.PHONE);
                        params.put("action", "ADD_RASP");
                        params.put("user_name", Constant.getUsername(MainActivity.this));
                        params.put("rasp_pwd", password);
                        params.put("rasp_ids", username);
                        params.put("nick_name", nickname);
                        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
                            @Override
                            public void onSuccess(String s) {

                                try {
                                    if (!Constant.isGetDataSuccess(new JSONObject(s)))
                                        return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    KJDB kjdb = KJDB.create(MainActivity.this);
                                    kjdb.save(new RaspberryEntity(username, password, nickname, function));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "不能重复添加树莓派", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(MainActivity.this, "树莓派添加成功...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                L.d("树莓派添加失败,ErrorMsg = " + strMsg);
                                Toast.makeText(MainActivity.this, "树莓派添加失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "取消操作", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    /**
     * 在添加遥控器的时候需要提示树莓派列表
     */
    private void showRaspberryListDialog(){
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put("action", "GET_RASP");
        params.put("user_name", Constant.getUsername(this));
        L.d("USERNAME = " + Constant.getUsername(MainActivity.this));
        kjHttp.post(this, Constant.HTTP_SINA_API, params,new StringCallBack() {
            @Override
            public void onSuccess(final String s) {
                raspList = new ArrayList<>();
                List<String> nicknamesList = new ArrayList<>();
                try {
                    L.d("RASP", "JSONObject = " + s);
                    JSONObject jObj = new JSONObject(s);
                    //如果返回的数据不正确则退出
                    if (!jObj.getString("success").equals("1"))
                        return ;
                    JSONArray jArrry = jObj.getJSONArray("data");
                    for (int i = 0; i < jArrry.length(); i++) {
                        JSONObject jsonObject = jArrry.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String rasp_ids = jsonObject.getString("rasp_ids");
                        String nick_name = jsonObject.getString("nick_name");
                        map.put("rasp_ids", rasp_ids);
                        map.put("nick_name", nick_name);
                        nicknamesList.add(i, jsonObject.getString("nick_name"));
                        raspList.add(map);

                        /* 添加到数据库当中 */
                        try{
                            entityDao.saveRaspberry(rasp_ids, "password",nick_name,"function");
                        }catch(Exception e){
                            L.e("数据库存储异常...");
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String[] nicknames = nicknamesList.toArray(new String[nicknamesList.size()]);

                //显示对话框选择按钮
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("选择树莓派")
                        .setItems(nicknames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String rasp_ids = raspList.get(which).get("rasp_ids");
                                Constant.setCurrentRaspIds(MainActivity.this, rasp_ids);
                                startActivity(new Intent(MainActivity.this,SelectControllerActivity.class));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(MainActivity.this, "添加遥控器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 从SQLite当中获取树莓派列表
     */
    private void getRaspberryFromDB(){
        raspList = new ArrayList<>();
        EntityDao entityDao = new EntityDao(this);
        List<RaspberryEntity> list = entityDao.getRaspberry();
        L.d("MainActivity", "getRaspberryFromDB = " + list.size());
        if (list == null || list.size() == 0)
            return;
        for (int i = 0; i < list.size(); i++){
            RaspberryEntity raspberryEntity = list.get(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("nickname",raspberryEntity.getNickname());
            map.put("password",raspberryEntity.getPasswrod());
            map.put("rasp_ids",raspberryEntity.getRaspid());
            map.put("function",raspberryEntity.getFunction());
            raspList.add(map);
        }
        raspberryAdapter = new RaspberryAdapter(this, raspList);
        listView.setAdapter(raspberryAdapter);
        listView.postInvalidate();
    }

}
