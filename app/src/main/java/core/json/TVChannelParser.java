package core.json;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.HttpCallBack;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.utils.SystemTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constant.Constant;
import module.inter.JsonProcessor;
import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-06
 * Time: 13:44
 * 解析电视节目
 */
public class TVChannelParser {

    /**
     * 根据返回的Json对象解析电视节目表
     * @param jObj
     * @return
     *      List<HashMap<String, String>>
     * @throws JSONException
     */
    public static List<HashMap<String, String>> parseTVChannelTimeForm(JSONObject jObj) throws JSONException{
        List<HashMap<String, String>> resultArray = new ArrayList<>();
        if (isSuccess(jObj))
            return null;
        JSONArray jArray = jObj.getJSONArray("result");
        int length = jArray.length();
        for (int i = 0; i < length; i++){
            HashMap<String, String> map = new HashMap<>();
            JSONObject itemObject = jArray.getJSONObject(i);
            map.put("cName", itemObject.getString("cName"));
            map.put("pName", itemObject.getString("pName"));
            map.put("time", itemObject.getString("time"));
            resultArray.add(map);
        }
        return resultArray;
    }

    /**
     * 解析电视节目的种类
     * @param cateObject
     * @return
     * @throws JSONException
     */
    public static List<HashMap<String, String>> parseTVChannelCategory(JSONObject cateObject) throws JSONException{
        List<HashMap<String, String>> resultArray = new ArrayList<>();
        if (isSuccess(cateObject))
            return null;
        JSONArray jArray = cateObject.getJSONArray("result");
        int length = jArray.length();
        for (int i = 0; i < length; i++){
            JSONObject itemObject = jArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", itemObject.getString("id"));
            map.put("name", itemObject.getString("name"));
            resultArray.add(map);
        }
        return resultArray;
    }

    /**
     * 解析属于同一category下地电视节目单,不包括时间
     * @param cateObject
     * @return
     * @throws JSONException
     */
    public static List<HashMap<String, String>> parseTVChannelForm(JSONObject cateObject) throws JSONException{
        List<HashMap<String, String>> resultArray = new ArrayList<>();
        if (isSuccess(cateObject))
            return null;
        JSONArray jArray = cateObject.getJSONArray("result");
        int length = jArray.length();
        for (int i = 0; i < length; i++){
            JSONObject itemObject = jArray.getJSONObject(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("channelName", itemObject.getString("channelName"));
            map.put("rel", itemObject.getString("rel"));
            resultArray.add(map);
        }
        return resultArray;
    }

    /**
     * 判断诗句是否返回正确
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static boolean isSuccess(JSONObject jsonObject) throws JSONException{
        if (!jsonObject.getString("error_code").equals("0")) {
            L.d("TVChannelParser", "错误原因是" + jsonObject.getString("reason"));
            return false;
        }
        return true;
    }

    //===================================

    /**
     * 从网络上获取某一天电视节目表单数据
     * @param context
     * @param code
     *          电视频道名称的简写
     * @param time
     *          想要查询的电视节目表格,可以为空
     * @param jsonProcessor
     *          处理
     */
    public static void getTVChannelTimeForm(Context context, String code, String time, final JsonProcessor jsonProcessor){
        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put("key", Constant.TV_CHANNEL_KEY);
        if (time == null || time.equals(""))
            params.put("date", SystemTool.getDataTime("yyyy-MM-dd"));
        else
            params.put("date", time);
        kjHttp.get(context, Constant.TV_CHANNEL_URL, params, new HttpCallBack() {
            @Override
            public void onLoading(long l, long l1) {

            }

            @Override
            public void onSuccess(Object o) {
                jsonProcessor.jsonProcess((JSONObject) o);
            }

            @Override
            public void onFailure(Throwable throwable, int i, String s) {

            }
        });
    }


    /**
     * 从网络上获取category的电视节目列表
     * @param context
     * @param id
     *          电视的id -- category
     * @param jsonProcessor
     *          处理
     */
    public static void getTVChannelForm(Context context, String id, final JsonProcessor jsonProcessor){
        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put("key", Constant.TV_CHANNEL_KEY);
        params.put("id", id);
        kjHttp.get(context, Constant.TV_CHANNEL_URL, params, new HttpCallBack() {
            @Override
            public void onLoading(long l, long l1) {

            }

            @Override
            public void onSuccess(Object o) {
                jsonProcessor.jsonProcess((JSONObject) o);
            }

            @Override
            public void onFailure(Throwable throwable, int i, String s) {

            }
        });
    }
}
