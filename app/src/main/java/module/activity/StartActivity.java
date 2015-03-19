package module.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.os.Handler;
import constant.Constant;
import module.activity.gesturepwd.SettingGesturePasswordActivity;
import module.activity.gesturepwd.UnLockGesturePasswordActivity;
import module.core.BaseActivity;
import module.view.svg.SvgCompletedCallBack;
import module.view.svg.SvgView;
import utils.CacheHandler;
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

    @Override
    protected void initData() {
        super.initData();
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
                        if (CacheHandler.readCache(StartActivity.this, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME).equals("")){
                            skipActivity(StartActivity.this, SettingGesturePasswordActivity.class);
                            CacheHandler.writeCache(StartActivity.this, Constant.USER_INFO , Constant.IS_FIRST_OPEN_ME,Constant.TRUE);
                        }else{
                            skipActivity(StartActivity.this, UnLockGesturePasswordActivity.class);
                        }

                    }
                },1000);
            }
        });
        container.addView(view);
        svgView.startAnimation();//开启动画
    }
}
