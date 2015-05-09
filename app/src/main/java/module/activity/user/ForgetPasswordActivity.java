package module.activity.user;

import org.kymjs.aframe.ui.BindView;

import module.core.SwipeBackActivity;
import vgod.smarthome.R;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:ForgetPasswordActivity.java
 * @Package:module.activity.user
 * @time:下午6:23:17 2014-12-15
 * @useage:忘记密码
 */
public class ForgetPasswordActivity extends SwipeBackActivity{

    @BindView(id = R.id.user_forget_password_id)
    private LinearLayout contentLayout;

	@BindView(id = R.id.user_activity_forget_telephone)
	private EditText informationText;
	@BindView(id = R.id.user_activity_forget_next , click = true)
	private TextView nextView;
    @BindView(id = R.id.user_activity_forget_verify)
    private EditText verifyText;

    /* 是否在输入电话号码 */
    private boolean isPhone = true;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
    }

    @Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.user_activity_forget_next:
            isPhone = !isPhone;
            switchInput();
			break;
		default:
			break;
		}
	}

    /**
     * 切换输入
     */
    private void switchInput(){
        if (isPhone == true){
            informationText.setVisibility(View.VISIBLE);
            verifyText.setVisibility(View.GONE);
            nextView.setText(getString(R.string.next));
        }else {
            informationText.setVisibility(View.GONE);
            verifyText.setVisibility(View.VISIBLE);
            nextView.setText(getString(R.string.ok));
        }
    }

	@Override
	public void setRootView() {
		super.setRootView();
		setContentView(R.layout.user_activity_forget_password);
        setActionBarView(true);
	}

	@Override
	public void setActionBarView(String title, boolean isBack) {
		super.setActionBarView(title, isBack);
	}

    @Override
    protected void onPause() {
        if (!isPhone){
            switchInput();
            isPhone = true;
        }
        else
            super.onPause();
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
