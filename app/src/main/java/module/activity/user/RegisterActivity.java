package module.activity.user;

import constant.Command;
import constant.Constant;
import framework.base.SwipeBackActivity;
import vgod.smarthome.R;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.BindView;


/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:RegisterActivity.java
 * @Package:module.activity.user
 * @time:下午6:22:48 2014-12-15
 * @useage:注册界面
 */
public class RegisterActivity extends SwipeBackActivity{
    @BindView(id = R.id.user_activity_register_id)
    private LinearLayout contentLayout;

    private KJHttp kjHttp;

    @BindView(id = R.id.user_activity_register_pwd)
    private EditText passwordText;
    @BindView(id = R.id.user_activity_register_nickname)
    private EditText nicknameText;
    @BindView(id = R.id.user_activity_register_email)
    private EditText emailText;
    @BindView(id = R.id.user_activity_register_submit, click = true)
    private TextView submit;

	@Override
	protected void initData() {
        kjHttp = new KJHttp();
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
        switch (v.getId())
        {
            case R.id.user_activity_register_submit:
                register(nicknameText.getText().toString().trim(),
                        passwordText.getText().toString().trim(),
                        emailText.getText().toString().trim());
                break;
        }
	}

	@Override
	public void setRootView() {
		super.setRootView();
		setContentView(R.layout.user_activity_register);
	}

	@Override
	public void setActionBarView(String title, boolean isBack) {
		super.setActionBarView(title, isBack);
	}

    /**
     * 注册
     * @param username
     * @param password
     * @param email
     */
    private void register(String username, String password, String email){
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put("action", "REGISTER");
        params.put("user_name", username);
        params.put("password", password);
        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(RegisterActivity.this, "注册失败,请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        });
    }
	
}
