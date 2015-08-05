package module.activity.energy;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.kymjs.aframe.ui.BindView;

import framework.base.SwipeBackActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-08-02
 * Time: 21:38
 * FIXME
 */
public class TVDetailActivity extends SwipeBackActivity{
    @BindView(id = R.id.activity_tv_detail_content)
    private RelativeLayout contentLayout;

    @BindView(id = R.id.tv_detail_add, click = true)
    private ImageView addView;
    @BindView(id = R.id.tv_detail_desc, click = true)
    private ImageView descView;
    @BindView(id = R.id.tv_detail_cancel, click = true)
    private ImageView cancelView;
    @BindView(id = R.id.tv_detail_ok, click = true)
    private ImageView okView;
    @BindView(id = R.id.tv_detail_time)
    private TextView timeText;

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_tv_detail);
        setActionBarView(true);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        contentLayout.setOnTouchListener(this);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        String time = timeText.getText().toString();//时间
        int timeInt = Integer.parseInt(time);
        switch (v.getId()) {
            case R.id.tv_detail_add:
                if (timeInt < 99) {
                    timeText.setText(String.format("%02d", ++timeInt));
                }
                break;
            case R.id.tv_detail_desc:
                if (timeInt < 99) {
                    timeText.setText(String.format("%02d", --timeInt));
                }
                break;
            case R.id.tv_detail_cancel:
                timeText.setText("00");
                break;
            case R.id.tv_detail_ok:
                Toast("请求发送成功, " + time + "分钟之后执行操作");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        contentLayout = null;
        addView = null;
        descView = null;
        cancelView = null;
        okView = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
