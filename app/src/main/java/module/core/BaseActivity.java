package module.core;


import org.kymjs.aframe.ui.AnnotateUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import utils.L;

public abstract class BaseActivity extends Activity implements OnClickListener{
	public final String TAG = this.getClass().getSimpleName();
	private boolean mActionBarBack = false;
	public boolean mShowActionBar = true;
	
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

}
