package module.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-20
 * Time: 00:23
 * 显示TVProgram
 */
public class TVProgramAdapter extends BaseExpandableListAdapter{
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    /** 显示的内容 **/
    private List<String> mTvChannel;
    private String[][] mTvProgram;
    private int[] mGroupDrawables;

    public TVProgramAdapter(Context context, List<String> mTvChannel, String[][] mTvProgram, int []mGroupDrawables) {
        mContext = context;
        this.mTvChannel = mTvChannel;
        this.mTvProgram = mTvProgram;
        this.mGroupDrawables = mGroupDrawables;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mTvChannel.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTvChannel.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_tv_program_item_parent, parent, false);
        }

        final ImageView image = (ImageView) convertView.findViewById(R.id.activity_list_group_item_image);
        image.setImageResource(mGroupDrawables[groupPosition]);

        final TextView text = (TextView) convertView.findViewById(R.id.activity_list_group_item_text);
        text.setText(mTvChannel.get(groupPosition));

        final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.activity_list_group_expanded_image);
        final int resId = isExpanded ? R.drawable.minus : R.drawable.plus;
        expandedImage.setImageResource(resId);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTvProgram[groupPosition].length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mTvProgram[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_tv_program_item_child, parent, false);
        }

        final TextView text = (TextView) convertView.findViewById(R.id.activity_list_child_item_text);
        text.setText(mTvProgram[groupPosition][childPosition]);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
