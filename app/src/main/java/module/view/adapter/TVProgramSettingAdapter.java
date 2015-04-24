package module.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-24
 * Time: 19:48
 * 电视节目设置界面的配置
 */
public class TVProgramSettingAdapter extends BaseAdapter{

    private List<String> data;
    private LayoutInflater mLayoutInflater;

    /**
     * 构造函数
     * @param context
     * @param data
     */
    public TVProgramSettingAdapter(Context context, List<String> data) {
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
            convertView = mLayoutInflater.inflate(R.layout.list_item_tv_program_setting, null);
        TextView channelText = (TextView)convertView.findViewById(R.id.list_item_tv_program_setting_channel);
        TextView numberText = (TextView)convertView.findViewById(R.id.list_item_tv_program_setting_number);

        channelText.setText(" ");
        numberText.setText(" ");
        return convertView;
    }
}
