package module.view.animation;

import android.app.Activity;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-20
 * Time: 13:46
 * 动画效果
 */
public abstract class BaseEffect {

    public abstract void prepareAnimation(final Activity destActivity);

    public abstract void prepare(Activity currActivity);

    public abstract void clean(Activity activity);

    public abstract void cancel();

    public abstract void animate(final Activity destActivity, final int duration);
}
