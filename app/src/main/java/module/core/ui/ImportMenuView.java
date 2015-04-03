package module.core.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import module.activity.MainActivity;
import module.core.ui.RippleLayout.RippleFinishListener;

import module.inter.NormalProcessor;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-26
 * Time: 22:08
 * 类似于百度阅读的添加界面
 */
public class ImportMenuView extends RelativeLayout implements RippleFinishListener,View.OnClickListener{

    Context context=null;

    RippleLayout rl_close,first_ball,rl_pc;
    View view_shadow;

    public ImportMenuView(Context context) {
        super(context);
    }
    public ImportMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public ImportMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.view_import_menu, this, true);

        rl_close = (RippleLayout)findViewById(R.id.rl_close);
        first_ball = (RippleLayout)findViewById(R.id.first_ball);
        rl_pc = (RippleLayout)findViewById(R.id.rl_pc);

        rl_close.setRippleFinishListener(this);
        first_ball.setRippleFinishListener(this);
        rl_pc.setRippleFinishListener(this);

        view_shadow = (View)findViewById(R.id.v_shadow);
        view_shadow.setOnClickListener(this);

        setVisibility(View.GONE);

        this.context = context;
    }

    public void animation(Context context){

        Animation first_ball_anim = AnimationUtils.loadAnimation(context, R.anim.collistion_import_qrcode_button);
        first_ball.startAnimation(first_ball_anim);

        Animation rl_pc_anim = AnimationUtils.loadAnimation(context, R.anim.collistion_import_pc_button);
        rl_pc.startAnimation(rl_pc_anim);


        Animation view_fade_in_anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view_shadow.startAnimation(view_fade_in_anim);
    }

    public void animationOut(){
        Animation first_ball_anim = AnimationUtils.loadAnimation(context, R.anim.collistion_import_qrcorde_button_out);
        first_ball.startAnimation(first_ball_anim);

        Animation rl_pc_anim = AnimationUtils.loadAnimation(context, R.anim.collistion_import_pc_button_out);
        rl_pc.startAnimation(rl_pc_anim);


        first_ball_anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation view_fade_in_anim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                rl_closeUnVisiableAnimation();
                view_shadow.startAnimation(view_fade_in_anim);
            }
        });
    }

    public void rl_closeVisiableAnimation(){
        ObjectAnimator object = ObjectAnimator.ofFloat(rl_close, "rotation", 0, -45);
        object.setDuration(250);
        object.setRepeatCount(0);
        object.start();
    }

    public void rl_closeUnVisiableAnimation(){

        ObjectAnimator object = ObjectAnimator.ofFloat(rl_close, "rotation", -45, 0);
        object.setDuration(200);
        object.setRepeatCount(0);
        object.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MainActivity.ripple.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
                setEnabled(false);
                setFocusable(false);
                MainActivity.ripple.bringToFront();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        object.start();
    }


    @Override
    public void rippleFinish(int id) {
        switch(id){
            case R.id.rl_close:
                animationOut();
                break;
            case R.id.first_ball:
                animationOut();
                if (firstProcessor != null)
                    firstProcessor.onProcess();
                break;
            case R.id.rl_pc:
                animationOut();
                if (secondProcessor != null)
                    secondProcessor.onProcess();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        animationOut();
    }

    private NormalProcessor firstProcessor;
    public void setFirstProcessor(NormalProcessor processor){
        firstProcessor = processor;
    }

    private NormalProcessor secondProcessor;
    public void setSecondProcessor(NormalProcessor processor){
        secondProcessor = processor;
    }
}

