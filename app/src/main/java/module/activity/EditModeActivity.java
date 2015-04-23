package module.activity;

import android.view.View;

import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-23
 * Time: 10:25
 * 编辑模式
 */
public class EditModeActivity extends BaseActivity{
    @Override
    protected void initData() {
        super.initData();
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
        setContentView(R.layout.activity_edit_mode);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
