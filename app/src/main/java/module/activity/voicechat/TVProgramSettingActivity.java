package module.activity.voicechat;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

import module.core.BaseActivity;
import module.view.adapter.TVProgramSettingAdapter;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-24
 * Time: 19:31
 * 电视节目设置Activity
 */
public class TVProgramSettingActivity extends BaseActivity{
    @BindView(id = R.id.tv_program_setting_contentLayout)
    private LinearLayout contentLayout;
    @BindView(id = R.id.tv_program_setting_confirm, click = true)
    private Button confirmBtn;
    @BindView(id = R.id.tv_program_setting_listView)
    private ListView mListView;

    private TVProgramSettingAdapter mAdapter;
    private List<String> mData;
    @Override
    protected void initData() {
        super.initData();
        mData = new ArrayList<>();
        initListView();
    }

    /**
     * 初始化列表
     */
    private void initListView() {
        for (int i = 0; i < 20; i++) {
            mData.add("12");
        }
        mAdapter = new TVProgramSettingAdapter(this, mData);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 保存数据
     */
    private void saveData() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_tv_program_setting);
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.tv_program_setting_confirm) {
            saveData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
