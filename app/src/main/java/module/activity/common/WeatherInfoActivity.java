package module.activity.common;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.ui.BindView;

import constant.Command;
import constant.Constant;
import module.core.BaseActivity;
import utils.L;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 21:41
 * 天气状况
 */
public class WeatherInfoActivity extends BaseActivity{

    @BindView(id = R.id.activity_normal_weather_id)
    private LinearLayout contentLayout;

    @BindView(id = R.id.city)
    private TextView cityText;//城市
    @BindView(id = R.id.time)
    private TextView publishTime;//发布时间
    @BindView(id = R.id.current_temperature)
    private TextView tempText;//当前温度
    @BindView(id = R.id.humidity)
    private TextView humidityText;//当前湿度
    @BindView(id = R.id.pm_data)
    private TextView pmNumberText;//PM 数字
    @BindView(id = R.id.pm2_5_quality)
    private TextView pmText;//PM 文字说明
    @BindView(id = R.id.week_today)
    private TextView dateText;//日期
    @BindView(id = R.id.temperature)
    private TextView tempRangeText;//温度范围
    @BindView(id = R.id.wind)
    private TextView windText;//风
    @BindView(id = R.id.climate)
    private TextView weatherText;//天气状况
    //树莓派端返回数据
    @BindView(id = R.id.weather_rasp_temp)
    private TextView raspTemp;//树莓派端返回温度
    @BindView(id = R.id.weather_rasp_humidity)
    private TextView raspHumidity;//树莓派端返回湿度
    private TextView raspPublishTime;//树莓派发布时间

    private KJHttp kjHttp;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
        kjHttp = new KJHttp();
        getDataFromNet();
        getDataFromRasp("123457");
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setContentView(R.layout.activity_normal_weather);
        setActionBarView(true);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 从树莓派端获取数据
     */
    private void getDataFromRasp(String rasp_ids){
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put("action", "GET_TEMPHUMI");
        params.put("rasp_ids",rasp_ids);
        kjHttp.post(this, Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                L.d("TAG","从树莓派上获取的数据 s = " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    if(!Constant.isGetDataSuccess(jsonObject))
//                        return;
                    raspTemp.setText("当前家庭温度: " + jsonObject.getString("temp") + " ℃");
                    raspHumidity.setText("当前家庭湿度: "+jsonObject.getString("humi") + " %");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 从网上获取天气数据
     */
    private void getDataFromNet(){
        //使用的是别人的AK,正式发布的时候需要自己申请
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=A72e372de05e63c8740b2622d0ed8ab1";
        kjHttp.get(this, url, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                L.d(TAG,"Weather = " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getString("status").equals("success")){
                        L.e("天气获取返回数据异常,请检查");
                        return;
                    }

                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    JSONObject resultObject = resultArray.getJSONObject(0);
                    JSONArray weatherArray = resultObject.getJSONArray("weather_data");
                    JSONObject todayObject = weatherArray.getJSONObject(0);//今天天气状况
                    String date = todayObject.getString("date");

                    tempText.setText("温度:" + parseDateToTemp(date));
                    humidityText.setText("湿度:25%");

                    cityText.setText(resultObject.getString("currentCity"));//获取城市信息
                    pmNumberText.setText(resultObject.getString("pm25"));//获取pm25信息
                    pmText.setText(parsePM25(resultObject.getString("pm25")));//获取pm25的指数

                    dateText.setText(parseDate(todayObject.getString("date")));//日期
                    tempRangeText.setText(todayObject.getString("temperature"));//当前温度
                    windText.setText(todayObject.getString("wind"));//风速
                    weatherText.setText(todayObject.getString("weather"));//天气状况
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                L.e("Error Message = " + strMsg);
                Toast.makeText(WeatherInfoActivity.this, "天气获取失败,请检查网络设备",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 解析PM25
     * @param pm25
     * @return
     */
    private String parsePM25(String pm25){
        int pm = Integer.parseInt(pm25);
        if (pm < 50)
            return "优良";
        else if(pm < 100)
            return "中等";
        else if(pm < 150)
            return "轻度污染";
        else if(pm < 200)
            return "中度污染";
        else if(pm >= 200)
            return "重度污染";
        return "好的一逼";
    }

    /**
     * 解析date 成温度
     * @param date
     * @return
     */
    private String parseDateToTemp(String date){
        int length = date.length();
        return date.substring(11,length - 1);
    }

    private String parseDate(String date){
        return date.substring(0, 10);
    }
}
