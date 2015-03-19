package module.core.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:TouchDisableView.java
 * @Package:com.special.ResideMenu
 * @time:上午12:55:50 2014-12-14
 * @useage:使得点击的区域不会滑动
 */
class TouchDisableView extends ViewGroup {

    private View mContent;

    //	private int mMode;
    private boolean mTouchDisabled = false;

    public TouchDisableView(Context context) {
        this(context, null);
    }

    public TouchDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContent(View v) {
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        /**
         * 使得父节点和子节点一样的大小
         * */
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    /**
     * 如果为true则子节点自己处理事件不再交房给外层布局，否则则父组件来实现
     * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mTouchDisabled;
    }

    void setTouchDisable(boolean disableTouch) {
        mTouchDisabled = disableTouch;
    }

    boolean isTouchDisabled() {
        return mTouchDisabled;
    }
}
