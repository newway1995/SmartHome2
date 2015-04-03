package module.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.os.Handler;
import constant.Constant;
import module.activity.gesturepwd.SettingGesturePasswordActivity;
import module.activity.gesturepwd.UnLockGesturePasswordActivity;
import module.activity.security.SecurityCameraActivity;
import module.activity.user.LoginActivity;
import module.core.BaseActivity;
import module.view.svg.SvgCompletedCallBack;
import module.view.svg.SvgView;
import utils.CacheHandler;
import utils.FileUtils;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-01-03
 * Time: 12:12
 * 启动界面
 */
public class StartActivity extends BaseActivity{
    @Override
    public void setRootView() {
        mShowActionBar = false;
        super.setRootView();
        setContentView(R.layout.activity_startframe);
    }

    private void initFileSystem(){
        if (!FileUtils.getInstance().hasSDCard())
            return;
        FileUtils.getInstance().createFolder(Constant.DIR_ROOT);
        L.d("初始化软件文件系统");
    }

    @Override
    protected void initData() {
        super.initData();
        initFileSystem();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        LinearLayout container = (LinearLayout) findViewById(R.id.start_container);
        LayoutInflater inflater = getLayoutInflater();
        addSvgView(inflater,container);
    }

    private void addSvgView(LayoutInflater inflater, LinearLayout container){
        final View view = inflater.inflate(R.layout.view_item_svg,container,false);
        final SvgView svgView = (SvgView) view.findViewById(R.id.svg);

        svgView.setSvgResource(R.raw.house);
        view.setBackgroundResource(R.color.md_blue_400);
        svgView.setmCallback(new SvgCompletedCallBack() {//结束之后回调函数
            @Override
            public void onSvgCompleted() {
                //动画跳转
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CacheHandler.readCache(StartActivity.this, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME).equals("")
                                || (!CacheHandler.readCache(StartActivity.this, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME).equals("")
                                && Constant.getUsername(StartActivity.this).equals(""))){
                            skipActivity(StartActivity.this, LoginActivity.class);
                        }else{
                            L.d("StartActivity","UNLOCK BY = " + Constant.getUnlockByWhat(StartActivity.this));
                            if (Constant.getUnlockByWhat(StartActivity.this).equals(Constant.UNLOCK_BY_GESTURE))
                                skipActivity(StartActivity.this, UnLockGesturePasswordActivity.class);
                            else if (Constant.getUnlockByWhat(StartActivity.this).equals(Constant.UNLOCK_BY_FACE))
                                skipActivity(StartActivity.this, SecurityCameraActivity.class);
                        }

                    }
                },1000);
            }
        });
        container.addView(view);
        svgView.startAnimation();//开启动画
    }
}
