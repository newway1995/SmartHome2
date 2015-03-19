package constant;


import utils.LockPatternUtils;
import android.app.Application;

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
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}	
