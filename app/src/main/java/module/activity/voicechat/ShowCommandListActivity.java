package module.activity.voicechat;

import android.view.View;
import android.widget.ListView;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.ui.BindView;

import java.util.List;

import module.core.BaseActivity;
import module.database.CommandEntity;
import module.view.adapter.CommandAdapter;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-15
 * Time: 18:35
 * 显示
 */
public class ShowCommandListActivity extends BaseActivity{

    private List<CommandEntity> commandList;
    private CommandAdapter mAdapter;

    @BindView(id = R.id.command_list)
    private ListView mListView;

    @Override
    protected void initData() {
        super.initData();
        CommandEntity.kjdb = KJDB.create(context);
        commandList = CommandEntity.queryAll();
        mAdapter = new CommandAdapter(this, commandList);
        mListView.setAdapter(mAdapter);
        timerThread.start();
    }

    private Thread timerThread = new Thread(){
        @Override
        public void run(){
            mListView.postInvalidate();
        }
    };

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
        setContentView(R.layout.activity_show_command_list);
        setActionBarView(true);
    }
}
