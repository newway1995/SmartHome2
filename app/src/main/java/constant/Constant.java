package constant;

import android.content.Context;

import com.facepp.http.HttpRequests;

import org.kymjs.aframe.utils.PreferenceHelper;

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

    public static final String USER_INFO = "user_info";
    public static final String OPEN_GESTURE_PWD = "open_gesture_pwd";//是否打开手势密码
    public static final String SHOW_GESTURE_TRACK = "show_gesture_track";//是否显示手势轨迹
    public static final String NUM_FACE = "num_face";//人脸的数量
    public static final String FACE_ID = "num_face";//人脸的ID

    public static HttpRequests getHttpResults(){
        HttpRequests httpRequests = new HttpRequests(Constant.FACE_DETECT_APIKEY,Constant.FACE_DETECT_APISECRET,true,true);
        return httpRequests;
    }

    /**
     * 增加一张人脸
     * */
    public static void addFaceNum(Context context){
        int num = PreferenceHelper.readInt(context,USER_INFO,NUM_FACE,0);
        num++;
        PreferenceHelper.write(context,USER_INFO,NUM_FACE,num);
    }

    /**
     * 获取人脸数
     * */
    public static int getFaceNum(Context context){
        return PreferenceHelper.readInt(context,USER_INFO,NUM_FACE,0);
    }

    /**
     * 设置人脸的ID
     * */
    public static void setFaceID(Context context,String id){
        addFaceNum(context);
        int num = getFaceNum(context);
        PreferenceHelper.write(context,USER_INFO,FACE_ID + num,id);
    }

    /**
     * 返回人脸的ID
     * */
    public static String getFaceID(Context context,int index){
        return PreferenceHelper.readString(context,USER_INFO,FACE_ID + index,null);
    }

    /**
     * 返回所有人脸的ID
     * */
    public static String[] getFaceID(Context context){
        int num = getFaceNum(context);
        String result[] = new String[num];
        for (int i = 1; i <= num; i++)
        {
            String temp = PreferenceHelper.readString(context,USER_INFO,FACE_ID + i,null);
            result[i - 1] = temp;
        }
        return result;
    }
}
