package constant;


import utils.LockPatternUtils;
import vgod.smarthome.R;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:MyApp.java
 * @Package:constant
 * @time:下午4:02:41 2014-12-14
 * @useage:静态类Application
 */
public class MyApp extends Application{
	private static MyApp mInstance;
	private LockPatternUtils mLockPatternUtils;

	public static MyApp getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		// 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		SpeechUtility.createUtility(MyApp.this, "appid=" + getString(R.string.app_id_voice));
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}	
