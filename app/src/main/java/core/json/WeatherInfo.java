package core.json;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.HttpCallBack;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.StringCallBack;

import constant.Constant;
import module.database.WeatherDetail;
import module.inter.JsonProcessor;
import module.inter.StringProcessor;
import utils.L;

/**
 * Package: core.json
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 12:34
 * 获取天气的信息
 */
public class WeatherInfo {

    /**
     * 获取当前城市的ID
     * @param context
     * @param stringProcessor
     */
    public static void getCityIdFromNet(Context context, StringProcessor stringProcessor){
        KJHttp kjHttp = new KJHttp();
        kjHttp.get(context, Constant.HTTP_WEATHER_CITY_ID, new StringCallBack() {
            @Override
            public void onSuccess(String s) {

            }
        });
    }

    /**
     * 获取天气状况
     * @param context
     * @param jsonProcessor
     */
    public static void getWeatherFromNet (Context context, final JsonProcessor jsonProcessor){
        KJHttp kjHttp = new KJHttp();
        kjHttp.get(context, Constant.HTTP_WEATHER_DETAIL_INFO, new HttpCallBack() {
            @Override
            public void onLoading(long l, long l2) {
                L.d("正在获取天气状况");
            }

            @Override
            public void onSuccess(Object o) {
                JSONObject jsonObject = (JSONObject)o;
                try {
                    jsonProcessor.jsonProcess(jsonObject.getJSONObject("weatherinfo"));
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, int i, String s) {
                L.e("天气获取失败");
            }
        });
    }

    /**
     * 解析天气信息
     * @param jObj
     * @return
     * @throws JSONException
     */
    public static WeatherDetail parseWeatherDetail (JSONObject jObj) throws JSONException{
        String city = jObj.getString("city");
        String date_y = jObj.getString("date_y");
        String week = jObj.getString("week");
        String temp[] = new String[]{jObj.getString("temp1"),jObj.getString("temp2"),jObj.getString("temp3"),jObj.getString("temp4"),jObj.getString("temp5"),jObj.getString("temp6")};
        String weather[] = new String[]{jObj.getString("weather1"),jObj.getString("weather2"),jObj.getString("weather3"),jObj.getString("weather4"),jObj.getString("weather5"),jObj.getString("weather6")};
        String wind[] = new String[]{jObj.getString("wind1"),jObj.getString("wind2"),jObj.getString("wind3"),jObj.getString("wind4"),jObj.getString("wind5"),jObj.getString("wind6")};
        String index_tr = jObj.getString("index_tr");
        String index_co = jObj.getString("index_co");
        WeatherDetail weatherDetail = new WeatherDetail(city, date_y, week, temp, weather, wind, index_tr, index_co);
        return weatherDetail;
    }
}
