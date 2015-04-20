package module.activity.voicechat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.HashMap;
import java.util.List;

import module.core.BaseActivity;
import module.view.adapter.TVProgramAdapter;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-07
 * Time: 11:27
 * 电视节目表
 */
public class TVProgramActivity extends BaseActivity{

    /** 测试数据 **/
    private String[] mGroups = {
            "Cupcake",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat"
    };

    private int[] mGroupDrawables = {
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator,
            R.drawable.child_indicator
    };

    private String[][] mChilds = {
            {"1.5"},
            {"1.6"},
            {"2.0","2.0.1","2.1"},
            {"2.2","2.2.1","2.2.2","2.2.3"},
            {"2.3","2.3.1","2.3.2","2.3.3","2.3.4","2.3.5","2.3.6","2.3.7"},
            {"3.0","3.1","3.2","3.2.1","3.2.2","3.2.3","3.2.4","3.2.5","3.2.6"},
            {"4.0", "4.0.1", "4.0.2", "4.0.3", "4.0.4"},
            {"4.1", "4.1.1", "4.1.2", "4.2", "4.2.1", "4.2.2", "4.3", "4.3.1"},
            {"4.4"}
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

    @Override
    protected void initData() {
        super.initData();
        kjHttp = new KJHttp();
        getDataFromNet();
        mAdapter = new TVProgramAdapter(this, mGroups, mChilds, mGroupDrawables);
        wrapperAdapter = new WrapperExpandableListAdapter(mAdapter);
        mlistView.setAdapter(wrapperAdapter);

        for(int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            mlistView.expandGroup(i);
        }

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
        getChannel("1");
        getChannel("2");
        getProgram("1","");
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
                    /** 数据更新 **/
                    parseTVChannel(jObj);
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
