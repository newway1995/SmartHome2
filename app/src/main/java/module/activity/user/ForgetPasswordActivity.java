package module.activity.user;

import org.kymjs.aframe.ui.BindView;

import vgod.smarthome.R;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import module.core.BaseActivity;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ForgetPasswordActivity.java
 * @Package:module.activity.user
 * @time:下午6:23:17 2014-12-15
 * @useage:忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity{
	
	@BindView(id = R.id.user_activity_forget_telephone)
	private EditText informationText;
	@BindView(id = R.id.user_activity_forget_next , click = true)
	private TextView nextView;
		

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.user_activity_forget_next:
			
			break;
		default:
			break;
		}
	}

	@Override
	public void setRootView() {
		super.setRootView();
		setContentView(R.layout.user_activity_forget_password);
	}

	@Override
	public void setActionBarView(String title, boolean isBack) {
		super.setActionBarView(title, isBack);
	}
}
