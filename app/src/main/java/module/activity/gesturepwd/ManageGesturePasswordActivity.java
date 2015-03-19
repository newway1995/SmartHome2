package module.activity.gesturepwd;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.kymjs.aframe.ui.BindView;

import constant.Constant;
import module.core.BaseActivity;
import utils.CacheHandler;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 13:49
 * 管理手势密码
 */
public class ManageGesturePasswordActivity extends BaseActivity{

    @BindView(id = R.id.gesturepwd_open_pwd , click = true)
    private ImageView openPwdView;
    @BindView(id = R.id.gesturepwd_show_track , click = true)
    private ImageView showTrackView;
    @BindView(id = R.id.gesturepwd_change_pwd , click = true)
    private RelativeLayout changePwdLayout;
    @BindView(id = R.id.gesturepwd_forget_pwd , click = true)
    private RelativeLayout forgetPwdLayout;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.gesturepwd_show_track:
                toggleImage(2);
                break;
            case R.id.gesturepwd_open_pwd:
                toggleImage(1);
                break;
            case R.id.gesturepwd_change_pwd:
                skipActivity(ManageGesturePasswordActivity.this,SettingGesturePasswordActivity.class);
                break;
            case R.id.gesturepwd_forget_pwd:
                Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 转换图片
     * */
    private void toggleImage(int obj){
        if (obj == 1){
            if (getPreference(Constant.OPEN_GESTURE_PWD)){//is true => 打开密码
                openPwdView.setBackgroundResource(R.drawable.img_switch_no);
                savePreference(Constant.OPEN_GESTURE_PWD,Constant.FALSE);
            }else{
                openPwdView.setBackgroundResource(R.drawable.img_switch_yes);
                savePreference(Constant.OPEN_GESTURE_PWD,Constant.TRUE);
            }
        }else if ( obj == 2){
            if (getPreference(Constant.SHOW_GESTURE_TRACK)){//is true => 打开密码
                showTrackView.setBackgroundResource(R.drawable.img_switch_no);
                savePreference(Constant.SHOW_GESTURE_TRACK,Constant.FALSE);
            }else{
                showTrackView.setBackgroundResource(R.drawable.img_switch_yes);
                savePreference(Constant.SHOW_GESTURE_TRACK,Constant.TRUE);
            }
        }
    }

    /**
     * 获取用户的选择
     * @return  true yes,false no
     * */
    private boolean getPreference(String key){
        if (CacheHandler.readCache(this, Constant.USER_INFO,key).equals(Constant.EMPTY_STRING))//默认情况下为空
            return CacheHandler.writeCache(this,Constant.USER_INFO,key,Constant.TRUE);
        return CacheHandler.readCache(this, Constant.USER_INFO,key).equals(Constant.TRUE);
    }

    /**
     * 保存用户的选择
     * */
    private void savePreference(String key,String value){
        CacheHandler.writeCache(this,Constant.USER_INFO,key,value);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_gesturepassword_manage);
    }
}
