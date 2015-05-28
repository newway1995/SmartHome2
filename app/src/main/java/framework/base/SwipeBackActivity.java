package framework.base;

import android.content.Intent;
import android.view.LayoutInflater;

import framework.ui.animation.SwipeBackLayout;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-06
 * Time: 15:00
 * 滑动退出Activity
 */
public class SwipeBackActivity extends BaseActivity{
    protected SwipeBackLayout layout;

    @Override
    public void setRootView() {
        super.setRootView();
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.alisa_swipe_base, null);
        layout.attachToActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }


    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
