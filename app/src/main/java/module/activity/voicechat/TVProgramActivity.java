package module.activity.voicechat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.BindView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;

import constant.Command;
import constant.MyTimer;
import framework.base.BaseActivity;
import module.adapter.TVProgramAdapter;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-07
 * Time: 11:27
 * 电视节目表
 */
public class TVProgramActivity extends BaseActivity{

    /** 测试数据 **/
    private List<String> mGroups = new ArrayList<>();

    private int[] mGroupDrawables = {
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
    };

    private String[][] mChilds = {
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
            {"中华民族 : 00:15","人口 : 00:39", "吉尼斯中国之夜 : 01:01","生活早参考 : 02:00","天天饮食 : 02:32", "精彩一刻 : 02:49","2015出彩中国人-4 : 04:00","朝闻天下 : 06:00","生活早参考 : 08:35", "电视剧：地火19/38 : 09:29","新闻30分 : 12:00","电视剧：离婚前规则28/34 : 13:12","第一动画乐园（下午版）: 17:0", "新闻联播 : 19:00","电视剧：王大花的革命生涯30/40 : 19:30", "晚间新闻 : 22:00", "走遍中国 : 23:32"},
    };

    private final static String PROGRAM_API = "http://apis.haoservice.com/lifeservice/TVGuide/getChannel";
    private final static String API_KEY = "2924818cd7ca44cfa7a04704308a5871";
    private KJHttp kjHttp;

    @BindView(id = R.id.tv_program_content)
    private LinearLayout contentLayout;
    /** listView **/
    @BindView(id = R.id.activity_tv_program_list)
    private FloatingGroupExpandableListView mlistView;

    /** 可回收变量 **/
    private LayoutInflater inflater;
    private List<HashMap<String, String>> tvChannels;
    private List<HashMap<String, String>> tvProgram;
    private View header;
    private View footer;
    private TVProgramAdapter mAdapter;
    private WrapperExpandableListAdapter wrapperAdapter;
    private MyTimer myTimer;

    @Override
    protected void initData() {
        super.initData();
        kjHttp = new KJHttp();
        myTimer = new MyTimer(context);//发送指令
        tvProgram = new ArrayList<>();
        tvChannels = new ArrayList<>();
        getDataFromNet();
        mAdapter = new TVProgramAdapter(this, mGroups, mChilds, mGroupDrawables);
        wrapperAdapter = new WrapperExpandableListAdapter(mAdapter);
        mlistView.setAdapter(wrapperAdapter);


        mlistView.setOnScrollFloatingGroupListener(new FloatingGroupExpandableListView.OnScrollFloatingGroupListener() {

            @Override
            public void onScrollFloatingGroupListener(View floatingGroupView, int scrollY) {
                float interpolation = -scrollY / (float) floatingGroupView.getHeight();

                // Changing from RGB(162,201,85) to RGB(255,255,255)
                final int greenToWhiteRed = (int) (162 + 93 * interpolation);
                final int greenToWhiteGreen = (int) (201 + 54 * interpolation);
                final int greenToWhiteBlue = (int) (85 + 170 * interpolation);
                final int greenToWhiteColor = Color.argb(255, greenToWhiteRed, greenToWhiteGreen, greenToWhiteBlue);

                // Changing from RGB(255,255,255) to RGB(0,0,0)
                final int whiteToBlackRed = (int) (255 - 255 * interpolation);
                final int whiteToBlackGreen = (int) (255 - 255 * interpolation);
                final int whiteToBlackBlue = (int) (255 - 255 * interpolation);
                final int whiteToBlackColor = Color.argb(255, whiteToBlackRed, whiteToBlackGreen, whiteToBlackBlue);

                final ImageView image = (ImageView) floatingGroupView.findViewById(R.id.activity_list_group_item_image);
                image.setBackgroundColor(greenToWhiteColor);

                final Drawable imageDrawable = image.getDrawable().mutate();
                imageDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);

                final View background = floatingGroupView.findViewById(R.id.activity_list_group_item_background);
                background.setBackgroundColor(greenToWhiteColor);

                final TextView text = (TextView) floatingGroupView.findViewById(R.id.activity_list_group_item_text);
                text.setTextColor(whiteToBlackColor);

                final ImageView expanded = (ImageView) floatingGroupView.findViewById(R.id.activity_list_group_expanded_image);
                final Drawable expandedDrawable = expanded.getDrawable().mutate();
                expandedDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);
            }
        });
        /**ListView的点击事件*/
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        controlTVOpen();
    }

    /**
     * 模拟电视节打开
     */
    private void controlTVOpen() {
        if (getIntent() == null) {
            return;
        }
        myTimer.setTimer(false);
        myTimer.sendCommand(Command.TELEVISION_SWITCH);//开电视机
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myTimer.sendCommand(Command.TELEVISION_FIVE);//开电视机
            }
        }, 5000);
        Toast("即将为您打开电视机");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        contentLayout.setOnTouchListener(this);
        inflater = getLayoutInflater();
        header = inflater.inflate(R.layout.list_tv_program_header, mlistView, false);
        mlistView.addHeaderView(header);
        footer = inflater.inflate(R.layout.list_tv_program_footer, mlistView, false);
        mlistView.addFooterView(footer);
        mlistView.setChildDivider(new ColorDrawable(getResources().getColor(R.color.md_deep_purple_200)));
    }

    /**
     * 从网络上获取数据
     */
    private void getDataFromNet(){
        tvChannels.clear();
        tvProgram.clear();
        mGroups.clear();
        getChannel("1");
        //getChannel("2");
        //getProgram("1","");
    }

    /**
     * 从网络上获取电视频道列表
     * @param id
     *          电视界面种类id
     */
    private void getChannel(String id){
        KJStringParams params = new KJStringParams();
        params.put("key", API_KEY);
        params.put("id",id);
        kjHttp.get(context, PROGRAM_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                /** Json数据 **/
                JSONObject jObj;
                try {
                    jObj = new JSONObject(s);
                    L.d(TAG, "电视频道列表 = " + s);
                    /** 数据更新 **/
                    parseTVChannel(jObj);

                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 解析数据
     * @param jObj
     *          数据来源
     * @throws JSONException
     */
    private void parseTVChannel(JSONObject jObj)throws JSONException{
        JSONArray jArray;
        if (jObj.getString("error_code").equals("0") && jObj.getString("reason").equals("Success")){
            jArray = jObj.getJSONArray("result");
            for (int i = 0; i < jArray.length(); i++){
                JSONObject item = jArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("channelName", item.getString("channelName"));
                map.put("rel", item.getString("rel"));
                map.put("url", item.getString("url"));
                mGroups.add(item.getString("channelName"));
                /** 装载数据 **/
                tvChannels.add(map);
            }
        }
    }


    /**
     * 从网络上获取电视节目列表
     * @param code
     *          频道代码,必填
     * @param date
     *          查询日期（默认为当天,格式:yyyy-MM-dd,非必填
     */
    private void getProgram(String code, String date){
        KJStringParams params = new KJStringParams();
        params.put("key", API_KEY);
        params.put("code",code);
        params.put("date",date);
        kjHttp.get(context, PROGRAM_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                /** Json数据 **/
                JSONObject jObj;
                try {
                    jObj = new JSONObject(s);
                    L.d(TAG, "电视节目列表 = " + s);
                    /** 数据更新 **/
                    parseProgram(jObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 解析电视频道数据
     * @param jObj
     *          数据来源
     * @throws JSONException
     */
    private void parseProgram(JSONObject jObj) throws JSONException{
        JSONArray jArray;
        if (jObj.getString("error_code").equals("0") && jObj.getString("reason").equals("Success")){
            jArray = jObj.getJSONArray("result");
            for (int i = 0; i < jArray.length(); i++){
                JSONObject item = jArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("pName", item.getString("pName"));
                map.put("time", item.getString("time"));
                map.put("cName", item.getString("cName"));
                /** 装载数据 **/
                tvProgram.add(map);
            }
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_tv_program);
        setActionBarView(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGroupDrawables = null;
        mGroups = null;
        mChilds = null;
        contentLayout = null;
        mlistView = null;
        inflater = null;
        header = null;
        footer = null;
        mAdapter = null;
        wrapperAdapter = null;
        setContentView(R.layout.null_view);
        finish();
        System.gc();
    }
}
