package module.activity.voicechat;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
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

    private final String URL_TV_PROGRAM = "https://php-westar.rhcloud.com/program.html";

    @BindView(id = R.id.tv_program_content)
    private LinearLayout contentLayout;

    @BindView(id = R.id.tv_program_webView)
    private WebView contentWebView;

    private Activity mActivity = this;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        /* 电视节目的名称 */
        String tv_code = getIntent().getStringExtra("code");

        contentWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //super.onProgressChanged(view, newProgress);
                mActivity.setProgress(newProgress * 1000);
            }
        });

        contentWebView.loadUrl(URL_TV_PROGRAM + "?code=" + tv_code);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_tv_program);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
    }
}
