package module.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import constant.Constant;
import module.database.ChatMsgEntity;
import utils.FileUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:08
 * 人力交互界面的Adapter
 */
public class ChatMsgAdapter extends BaseAdapter{

    //ListView视图的内容由IMsgViewType决定
    public static interface IMsgViewType
    {
        //对方发来的信息
        int IMVT_COM_MSG = 0;
        //自己发出的信息
        int IMVT_TO_MSG = 1;
    }
    private AlphaAnimation mAlphaAnimation;
    public static final String TAG = ChatMsgAdapter.class.getSimpleName();
    private List<ChatMsgEntity> data;
    private LayoutInflater mInflater;
    private Context context;

    public ChatMsgAdapter(Context context, List<ChatMsgEntity> data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
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

    /**
     * 获取项的类型
     * @param position
     * @return
     */
    public int getItemViewType(int position) {
        ChatMsgEntity entity = data.get(position);

        if (entity.getMsgType())
        {
            return IMsgViewType.IMVT_COM_MSG;
        }else{
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    /**
     * 获取项的类型数
     * @return
     */
    public int getViewTypeCount() {
        return 2;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsgEntity chatMsgEntity = data.get(position);
        boolean isComMsg = chatMsgEntity.getMsgType();
        ViewHolder viewHolder = null;

        if (convertView == null){
            if (isComMsg){
                //如果是对方发来的消息，则显示的是左气泡
                convertView = mInflater.inflate(R.layout.item_chatting_msg_text_left, null);
            }else {
                //如果是自己发出的消息，则显示的是右气泡
                convertView = mInflater.inflate(R.layout.item_chatting_msg_text_right, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            viewHolder.isComMsg = isComMsg;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvContent.setText(isComMsg == true ? chatMsgEntity.getText() : "『 “ " + chatMsgEntity.getText() + " ” 』");
        TextPaint tp = viewHolder.tvContent.getPaint();
        tp.setFakeBoldText(true);
        if (position == data.size() - 1){
            if (mAlphaAnimation == null)
                mAlphaAnimation = new AlphaAnimation(0, 1);
            mAlphaAnimation.setDuration(1000);
            viewHolder.tvContent.startAnimation(mAlphaAnimation);
        }
        return convertView;
    }

    /**
     * 通过ViewHolder的方法来显示
     */
    class ViewHolder{
        public TextView tvContent;
        public boolean isComMsg = true;//默认是对方发送的数据
    }
}
