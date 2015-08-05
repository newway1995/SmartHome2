package module.activity.energy;

import android.view.View;


import framework.base.SwipeBackActivity;
import android.widget.LinearLayout;

import org.kymjs.aframe.ui.BindView;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-08-02
 * Time: 13:15
 * 向用户推荐电器
 */
public class ElecRecommendActivity extends SwipeBackActivity{
    @BindView(id = R.id.energy_activity_ele)
    private LinearLayout contentLayout;

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.energy_activity_recommand);
    }

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contentLayout = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
