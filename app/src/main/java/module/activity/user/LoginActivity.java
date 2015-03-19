package module.activity.user;

import org.kymjs.aframe.ui.BindView;

import vgod.smarthome.R;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import module.core.BaseActivity;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:LoginActivity.java
 * @Package:module.activity.user
 * @time:下午6:22:25 2014-12-15
 * @useage:登录界面
 * 
 */
public class LoginActivity extends BaseActivity{
	@BindView(id=R.id.user_activity_login_username)
	private EditText usernameText;//用户名
	@BindView(id=R.id.user_activity_login_password)
	private EditText passwordText;//密码
	@BindView(id=R.id.user_activity_login_login , click=true)
	private TextView loginView;//登录
	@BindView(id=R.id.user_activity_login_register , click=true)
	private TextView registerText;//注册
	@BindView(id=R.id.user_activity_login_forget_pwd , click=true)
	private TextView forgetPwdText;//忘记密码
			

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.user_activity_login_login:
			Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
			break;
		case R.id.user_activity_login_register:
			Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
			break;
		case R.id.user_activity_login_forget_pwd:
			Toast.makeText(this, "change password", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void setRootView() {
		super.setRootView();
		setContentView(R.layout.user_activity_login);
		setActionBarView(true);
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
