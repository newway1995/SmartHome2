package module.activity.controler;


import android.view.View;
import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ControlADActivity.java
 * @Package:module.activity.controler
 * @time:下午1:25:39 2014-12-16
 * @useage:控制空调
 */
public class ControlADActivity extends BaseActivity{

	
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
        setContentView(R.layout.control_activity_ad);
        setActionBarView(true);
    }
	
}
