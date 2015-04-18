package module.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import module.database.CommandEntity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-15
 * Time: 18:56
 * 指令集
 */
public class CommandAdapter extends BaseAdapter{

    private List<CommandEntity> data;
    private LayoutInflater mLayoutInflater;

    /**
     * 构造函数
     * @param context
     * @param data
     */
    public CommandAdapter(Context context, List<CommandEntity> data) {
        this.data = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.list_item_command, null);
        TextView commandText = (TextView)convertView.findViewById(R.id.list_item_command_command);
        TextView timerText = (TextView)convertView.findViewById(R.id.list_item_command_timer);

        commandText.setText(data.get(position).getCommand());

        long timer = data.get(position).getTimer();
        long afterTimer = System.currentTimeMillis() / 1000 - (timer + data.get(position).getCurrentTime() / 1000);
        timerText.setText(afterTimer + " 秒后执行");
        return convertView;
    }
}
