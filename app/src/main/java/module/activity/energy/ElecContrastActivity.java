package module.activity.energy;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.kymjs.aframe.ui.BindView;

import framework.base.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-08-02
 * Time: 13:19
 * FIXME
 */
public class ElecContrastActivity extends SwipeBackActivity{
    @BindView(id = R.id.energy_activity_contrast_layout)
    private LinearLayout contentLayout;

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.energy_activity_contrast);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_energy_contrast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_elec_contrast) {
            showActivity(ElecContrastActivity.this, ElecRecommendActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bgView.setImageResource(R.drawable.contrast);
//            }
//        }, 1000);
        super.initData();
        contentLayout.setOnTouchListener(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        contentLayout = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
