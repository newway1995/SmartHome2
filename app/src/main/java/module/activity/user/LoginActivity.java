package module.activity.user;

import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.utils.PreferenceHelper;

import constant.Command;
import constant.Constant;
import core.detect.FaceCompare;
import core.detect.NetResultHandler;
import module.activity.gesturepwd.SettingGesturePasswordActivity;
import framework.base.SwipeBackActivity;
import utils.CacheHandler;
import utils.L;
import vgod.smarthome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:LoginActivity.java
 * @Package:module.activity.user
 * @time:下午6:22:25 2014-12-15
 * @useage:登录界面
 * 
 */
public class LoginActivity extends SwipeBackActivity{
    @BindView(id = R.id.user_activity_login_id)
    private LinearLayout contentLayout;

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

    private KJHttp kjHttp;
    /* 人脸检测 */
    private FaceCompare faceCompare;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        kjHttp = new KJHttp();
        faceCompare = new FaceCompare();
        testData();
    }

    private void testData(){
        L.d("SharedPreference","IS_FIRST_OPEN_ME = " + PreferenceHelper.readString(context, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME, "default"));
        L.d("SharedPreference", "UnlockByWhat = " + Constant.getUnlockByWhat(context));
    }

    @Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.user_activity_login_login:
			login(usernameText.getText().toString().trim(), passwordText.getText().toString().trim());
			break;
		case R.id.user_activity_login_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.user_activity_login_forget_pwd:
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
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


    /**
     * 登录
     * @param username UserName
     * @param password Password
     */
    private void login(final String username, final String password){
        final Dialog dialog = createLoadingDialog();
        dialog.show();

        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put("action", "LOGIN");
        params.put("user_name", username);
        params.put("password", password);
        kjHttp.post(Command.COMMAND_HTTP_URL, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    L.d("Login", "JsonObject = " + jsonObject.toString());
                    if (!jsonObject.getString("success").equals("1")) {
                        Toast(jsonObject.getString("message"));
                        return;
                    }
                    //如果是首次使用软件
                    if (CacheHandler.readCache(LoginActivity.this, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME).equals("")) {
                        CacheHandler.writeCache(LoginActivity.this, Constant.USER_INFO, Constant.IS_FIRST_OPEN_ME, Constant.TRUE);
                        testData();
                        skipActivity(LoginActivity.this, SettingGesturePasswordActivity.class);
                        LoginActivity.this.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //第一次登陆
                if (Constant.getPersonName(LoginActivity.this) == null || !Constant.getPersonName(LoginActivity.this).equals(username)) {
                    Constant.setPersonName(LoginActivity.this, username);
                    //在手机里面保存返回的数据
                    faceCompare.createPerson(username, new NetResultHandler() {
                        @Override
                        public void resultHandler(JSONObject rst) {
                            String person_id = null;
                            try {
                                person_id = rst.getString("person_id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Constant.setPersonId(LoginActivity.this, person_id);
                        }
                    });
                }
                Constant.setPassword(LoginActivity.this, password);
                Constant.setUsername(LoginActivity.this, username);
                dialog.cancel();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                L.d("Login Error = " + strMsg);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "登录失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
    }


    /**
     * 得到自定义的progressDialog
     * @return Dialog
     */
    private Dialog createLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }
	
}
