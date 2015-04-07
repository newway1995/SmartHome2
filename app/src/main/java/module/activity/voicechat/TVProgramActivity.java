package module.activity.voicechat;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import org.kymjs.aframe.ui.BindView;

import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-07
 * Time: 11:27
 * 电视节目表
 */
public class TVProgramActivity extends BaseActivity{

    @BindView(id = R.id.tv_program_content)
    private LinearLayout contentLayout;

    @BindView(id = R.id.tv_program_webView)
    private WebView contentWebView;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        /* 电视节目的名称 */
        String tv_code = getIntent().getStringExtra("code");
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        setContentView(R.layout.activity_tv_program);
    }

    @Override
    public void setRootView() {
        super.setRootView();

    }
}
