package framework.ui.animation;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;


import java.util.LinkedList;
import java.util.List;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-30
 * Time: 19:54
 *
 * 滑动退出Activity的View,自定义可以滑动的RelativeLayout
 */
public class SwipeBackLayout extends FrameLayout {
    private static final String TAG = SwipeBackLayout.class.getSimpleName();

    /**
     * 最小滑动触发速度
     */
    private final static int FLING_MIN_VELOCITX = 5000;
    /**
     * 跟踪滑动的速度
     */
    private VelocityTracker vTracker;
    /**
     * 滑动的内容
     */
    private View mContentView;

    /**
     * 最小滑动距离
     */
    private int mTouchSlop;

    /**
     * 按下的X坐标
     */
    private int downX;

    /**
     * 按下的Y坐标
     */
    private int downY;

    /**
     * 临时的X坐标
     */
    private int tempX;

    /**
     * 滑动类
     */
    private Scroller mScroller;

    /**
     * 滑动的宽度
     */
    private int viewWidth;

    /**
     * 是否正在滑动
     */
    private boolean isSliding;

    /**
     * 是否滑动完成
     */
    private boolean isFinish;

    /**
     * 阴影Drawable
     */
    private Drawable mShadowDrawable;

    /**
     * Activity
     */
    private Activity mActivity;

    /**
     * ViewPager
     */
    private List<ViewPager> mViewPagers = new LinkedList<>();

    /**
     * 构造函数
     * @param context Context
     * @param attrs AttributeSet
     */
    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     * @param context Context
     * @param attrs AttributeSet
     * @param defStyle int
     */
    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
    }


    /**
     * 将Activity的最上层的View变为this
     * @param activity Activity
     */
    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.windowBackground });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //处理ViewPager冲突问题
        ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);

        if(mViewPager != null && mViewPager.getCurrentItem() != 0){
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();

                //初始化滑动速度
                if (vTracker == null) {
                    vTracker = VelocityTracker.obtain();
                } else {
                    vTracker.clear();
                }
                vTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();

                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSliding = true;
                }

                if (moveX - downX >= 0 && isSliding) {
                    mContentView.scrollBy(deltaX, 0);
                }

                vTracker.addMovement(event);
                vTracker.computeCurrentVelocity(1000);
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;

                if (vTracker.getXVelocity() > FLING_MIN_VELOCITX) {
                    isFinish = true;
                    scrollRight();
                } else if (mContentView.getScrollX() <= -viewWidth / 2) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        return true;
    }

    /**
     * 获取SwipeBackLayout里面的ViewPager的集合
     * @param mViewPagers List<ViewPager> mViewPagers
     * @param parent ViewGroup
     */
    private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++){
            View child = parent.getChildAt(i);
            if(child instanceof ViewPager){
                mViewPagers.add((ViewPager)child);
            }else if(child instanceof ViewGroup){
                getAlLViewPager(mViewPagers, (ViewGroup)child);
            }
        }
    }


    /**
     * 返回我们touch的ViewPager
     * @param mViewPagers List<ViewPager>
     * @param ev MotionEvent
     * @return ViewPager
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
        if(mViewPagers == null || mViewPagers.size() == 0){
            return null;
        }
        Rect mRect = new Rect();
        for(ViewPager v : mViewPagers){
            v.getHitRect(mRect);

            if(mRect.contains((int)ev.getX(), (int)ev.getY())){
                return v;
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();

            getAlLViewPager(mViewPagers, this);
            Log.i(TAG, "ViewPager size = " + mViewPagers.size());
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable != null && mContentView != null) {

            int left = mContentView.getLeft()
                    - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }

    }


    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mContentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }


    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
            }
        }
    }
}