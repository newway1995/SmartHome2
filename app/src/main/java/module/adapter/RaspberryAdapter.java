package module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import framework.ui.material.ColorGenerator;
import framework.ui.material.TextDrawable;
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
    /**
     * Drawable
     */
    private TextDrawable.IBuilder mDrawableBuilder;
    /**
     * 颜色生成器
     */
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public RaspberryAdapter(Context context,ArrayList<HashMap<String, String>> list){
        this.list = list;
        //Log.d(TAG, list.toString());
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
        ImageView imageView = (ImageView)convertView.findViewById(R.id.list_item_rasp_img);

        mDrawableBuilder = TextDrawable.builder()
                                        .beginConfig()
                                        .withBorder(4)
                                        .endConfig()
                                        .roundRect(10);
        imageView.setImageDrawable(mDrawableBuilder.build("R", mColorGenerator.getRandomColor()));
        nicknameText.setText(list.get(position).get("nickname"));
        functionText.setText(list.get(position).get("function"));
        return convertView;
    }
}
