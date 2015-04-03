package module.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-31
 * Time: 14:24
 * 树莓派列表显示Adapter
 */
public class RaspberryAdapter extends BaseAdapter{
    private final static String TAG = "DoctorAdapter";
    ArrayList<HashMap<String, String >> list;
    private LayoutInflater inflater;

    public RaspberryAdapter(Context context,ArrayList<HashMap<String, String>> list){
        this.list = list;
        Log.d(TAG, list.toString());
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_raspberry, null);
        TextView nicknameText = (TextView)convertView.findViewById(R.id.list_item_rasp_nickname);
        TextView functionText = (TextView)convertView.findViewById(R.id.list_item_rasp_function);

        nicknameText.setText(list.get(position).get("nickname"));
        functionText.setText(list.get(position).get("function"));
        return convertView;
    }
}
