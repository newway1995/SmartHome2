package module.core;


import org.kymjs.aframe.ui.AnnotateUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import utils.L;
import vgod.smarthome.R;

public abstract class BaseActivity extends Activity implements OnClickListener, View.OnTouchListener{
	public final String TAG = this.getClass().getSimpleName();
    protected Context context = this;

	private boolean mActionBarBack = false;
	public boolean mShowActionBar = true;

    /* 滑动返回 */
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;
    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;
    //记录手指按下时的横坐标。
    private float xDown;
    //记录手指移动时的横坐标。
    private float xMove;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

	
	/**
     * initialization data. And this method run in background thread, so you
     * shouldn't change ui
     */
    protected void initDataFromThread() {}

    /** initialization data */
    protected void initData() {}

    /** initialization widget */
    protected void initWidget() {}

    /** initialization */
    public void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDataFromThread();
            }
        }).start();
        initData();
        initWidget();
    }

    /** listened widget's click method */
    public void widgetClick(View v) {}
    
    /** set rootView*/
    public void setRootView(){
        if (mShowActionBar) getActionBar().setDisplayHomeAsUpEnabled(mShowActionBar);
        else requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootView(); // 必须放在annotate之前调用
        AnnotateUtil.initBindView(this);
        initialize();
    }
	
	@Override
	public void onClick(View v) {
		widgetClick(v);
	}
	
	/**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls) {
        showActivity(aty, cls);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Intent it) {
        showActivity(aty, it);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        showActivity(aty, cls, extras);
        aty.finish();
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }
    
    /**
     * 初始化ActionBar
     * */
    public void setActionBarView(String title,boolean isBack){
    	L.d(TAG, "title = " + title);
    	getActionBar().setTitle(title);
    	getActionBar().setDisplayHomeAsUpEnabled(isBack);    	
    	mActionBarBack = isBack;
    }
    
    /**
     * 初始化ActionBar
     * */
    public void setActionBarView(boolean isBack){
    	getActionBar().setDisplayHomeAsUpEnabled(isBack);
    	mActionBarBack = isBack;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mActionBarBack) {
				finish();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

    /**
     * 用来调试输出
     * @param d
     */
    protected void Debug(String d){
        L.d(TAG, d);
    }

    /**
     * Toast内容,默认为Short Time
     * @param t
     */
    protected void Toast(String t){
        Toast.makeText(context,t, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast内容,默认为Length Time
     * @param t
     */
    protected void ToastLong(String t){
        Toast.makeText(context,t, Toast.LENGTH_LONG).show();
    }

    //右滑返回
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                    //设置切换动画，从右边进入，左边退出
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
