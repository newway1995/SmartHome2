package module.activity.common;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.kymjs.aframe.ui.BindView;

import module.core.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 01:24
 * FIXME
 */
public class AboutActivity extends SwipeBackActivity{
    @BindView(id = R.id.activity_normal_about)
    private LinearLayout contentLayout;

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
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.activity_normal_about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
