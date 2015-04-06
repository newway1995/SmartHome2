package constant;

import android.content.Context;
import android.widget.Toast;

import com.facepp.http.HttpRequests;

import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.utils.PreferenceHelper;

import module.activity.controler.ControlADActivity;
import utils.L;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:Constant.java
 * @Package:constant
 * @time:下午4:09:32 2014-12-14
 * @useage:常量类
 */
public class Constant {
    public static final String APP_NAME = "SmartHome";//应用名称
    public static final String FACE_DETECT_SERVER = "http://api.cn.faceplusplus.com/";//人脸检测服务器地址
    public static final String FACE_DETECT_APIKEY = "7ad998186db0343811b66fa7d6c78233";
    public static final String FACE_DETECT_APISECRET = "Ok99JGecsH8ghXF_5dTKvlIKSbmTYH_N";
    public static final String DIR_ROOT = "SmartHome";//所有应用的根目录
    public static final String APP_ROOT = "facedetect";//人脸识别的目录
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String EMPTY_STRING = "";//空字符串

    public static final String IS_FIRST_OPEN_ME = "is_first_open_me";//是否为第一次打开

    /* 用户相关的信息 */
    public static final String USER_INFO = "user_info";//表名
    public static final String USERNAME = "username";//用户名
    public static final String PASSWORD = "password";//密码
    public static final String PERSON_ID = "person_id";//人的id
    public static final String PERSON_NAME = "person_name";//人得名字
    public static final String OPEN_GESTURE_PWD = "open_gesture_pwd";//是否打开手势密码
    public static final String SHOW_GESTURE_TRACK = "show_gesture_track";//是否显示手势轨迹
    public static final String NUM_FACE = "num_face";//人脸的数量
    public static final String FACE_ID = "num_face";//人脸的ID

    /**
     * 天气接口常量
     */
    public static final String HTTP_WEATHER_CITY_ID = "http://61.4.185.48:81/g/";
    public static final String HTTP_WEATHER_DETAIL_INFO = "http://m.weather.com.cn/data/101230101.html";

    /**
     * 人脸检测常量
     */
    public static final int UPDATE_FACE_RECT = 0;
    public static final int CAMERA_HAS_STARTED_PREVIEW = 1;

    /* 当前选择的rasp_ids */
    public static final String CURRENT_RASP_IDS = "CURRENT_RASP_IDS";

    /* 电视节目表的接口 */
    public static final String TV_CHANNEL_URL = "http://apis.haoservice.com/lifeservice/TVGuide/getProgram";
    public static final String TV_CHANNEL_KEY = "2924818cd7ca44cfa7a04704308a5871";

    /**
     * 设置rasp_ids
     * @param context
     * @param ids
     */
    public static void setCurrentRaspIds(Context context, String ids){
        PreferenceHelper.write(context, USER_INFO, CURRENT_RASP_IDS,ids);
    }

    /**
     * 获取当前rasp_ids
     * @param context
     * @return
     */
    public static String getCurrentRaspIds(Context context){
        return PreferenceHelper.readString(context, USER_INFO, CURRENT_RASP_IDS,"123456");
    }

    /**
     * 登录注册接口
     */
    public static final String HTTP_SINA_API = "http://1.newway.sinaapp.com/index.php";

    public static HttpRequests getHttpResults(){
        HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET,true,true);
        return httpRequests;
    }

    /* App开锁方式 */
    public static final String UNLOCK_BY_WHAT = "unlock_by_what";
    public static final String UNLOCK_BY_FACE = "unlock_by_face";
    public static final String UNLOCK_BY_GESTURE = "unlock_by_gesture";

    /**
     * 设置开锁的方式,0表示手势,1表示人脸
     * @param s
     * @param context
     */
    public static void setUnlockByWhat(Context context, int s){
        if (s == 0)
            PreferenceHelper.write(context, USER_INFO, UNLOCK_BY_WHAT,UNLOCK_BY_GESTURE);
        else if(s == 1)
            PreferenceHelper.write(context, USER_INFO, UNLOCK_BY_WHAT,UNLOCK_BY_FACE);
        L.d("UNLOCK", "By "+getUnlockByWhat(context));
    }

    /**
     * 获取依靠什么解锁
     * @return
     */
    public static String getUnlockByWhat(Context context){
        if (PreferenceHelper.readString(context, USER_INFO, UNLOCK_BY_WHAT,UNLOCK_BY_GESTURE) == UNLOCK_BY_GESTURE)
            return UNLOCK_BY_GESTURE;
        return UNLOCK_BY_FACE;
    }

    /**
     * 设置人脸的ID
     * */
    public static void setFaceID(Context context,String id){
        PreferenceHelper.write(context,USER_INFO,FACE_ID,id);
    }

    /**
     * 返回人脸的ID
     * */
    public static String getFaceID(Context context){
        return PreferenceHelper.readString(context,USER_INFO,FACE_ID,null);
    }


    /**
     * 返回person的名称,如果没有则返回空null
     * @param context
     * @return
     */
    public static String getPersonName(Context context){
        return PreferenceHelper.readString(context, USER_INFO, PERSON_NAME, null);
    }

    /**
     * 设置person的name
     * @param context
     * @param person_name
     */
    public static void setPersonName(Context context, String person_name){
        PreferenceHelper.write(context, USER_INFO, PERSON_NAME, person_name);
    }

    /**
     * 返回person的id,如果没有则返回空null
     * @param context
     * @return
     */
    public static String getPersonId(Context context){
        return PreferenceHelper.readString(context, USER_INFO, PERSON_ID, null);
    }

    /**
     * 设置person的name
     * @param context
     * @param person_id
     */
    public static void setPersonId(Context context, String person_id){
        PreferenceHelper.write(context, USER_INFO, PERSON_ID, person_id);
    }

    /**
     * 获取,默认为空
     * @param context
     * @return
     */
    public static String getPassword(Context context){
        return PreferenceHelper.readString(context, USER_INFO, PASSWORD,null);
    }

    /**
     * 设置密码
     * @param context
     * @param password
     */
    public static void setPassword(Context context, String password){
        PreferenceHelper.write(context, USER_INFO, PASSWORD, password);
    }

    /**
     * 获取账号,默认为空
     * @param context
     * @return
     */
    public static String getUsername(Context context){
        return PreferenceHelper.readString(context, USER_INFO, USERNAME,null);
    }

    /**
     * 设置账号
     * @param context
     * @param username
     */
    public static void setUsername(Context context, String username){
        PreferenceHelper.write(context, USER_INFO, USERNAME, username);
    }

    /**
     * 发送指令
     * @param command
     */
    public void sendCommand(String command){
        KJHttp kjHttp = new KJHttp();
        KJStringParams params = new KJStringParams();
        params.put(Command.COMMAND_DEVICE, Command.PHONE);
        params.put(Command.COMMAND, command);
        kjHttp.post(Constant.HTTP_SINA_API, params, new StringCallBack() {
            @Override
            public void onSuccess(String s) {
                L.d("指令发送成功...s = " + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public static boolean isGetDataSuccess(JSONObject jsonObject){
        try{
            if (!jsonObject.getString("success").equals("1"))
                return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
