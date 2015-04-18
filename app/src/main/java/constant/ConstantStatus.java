package constant;

import android.content.Context;

import org.kymjs.aframe.utils.PreferenceHelper;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-17
 * Time: 09:59
 * 电器的状态保存
 */
public class ConstantStatus {
    public static final String ELEC_APP_INFO = "RASP_INFO";
    //电灯状态
    public static final String BULB_SWITCH = "BULB_SWITCH";
    //电扇状态
    public static final String FAN_SWITCH = "FAN_SWITCH";
    public static final String FAN_STATUS = "FAN_STATUS";
    public static final String FAN_SHAKE = "FAN_SHAKE";
    //投影仪
    public static final String PJ_SWITCH = "PJ_SWITCH";
    //空调
    public static final String AIR_SWITCH = "AIR_SWITCH";
    public static final String AIR_STATUS = "AIR_STATUS";
    public static final String AIR_MODE = "AIR_MODE";
    //电视机
    public static final String TV_SWITCH = "TV_SWITCH";
    public static final String TV_STATUS = "TV_STATUS";


    //电灯
    public static void setBulbSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, BULB_SWITCH, str);
    }
    public static String getBulbSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, BULB_SWITCH, "off");
    }


    //风扇
    public static void setFanSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_SWITCH, str);
    }
    public static String getFanSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_SWITCH, "off");
    }
    public static void setFanShake(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_SHAKE, str);
    }
    public static String getFanShake(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_SHAKE, "off");
    }
    public static void setFanStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_STATUS, str);
    }
    public static String getFanStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_STATUS, "off");
    }


    //投影仪
    public static void setPjSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, PJ_SWITCH, str);
    }
    public static String getPjSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, PJ_SWITCH, "off");
    }


    //空调
    public static void setAirSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_SWITCH, str);
    }
    public static String getAirSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_SWITCH, "off");
    }
    public static void setAirStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_STATUS, str);
    }
    public static String getAirStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_STATUS, "off");
    }
    public static void setAirMode(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_MODE, str);
    }
    public static String getAirMode(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_MODE, "off");
    }


    //电视机
    public static void setTVSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, TV_SWITCH, str);
    }
    public static String getTVSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, TV_SWITCH, "off");
    }
    public static void setTvStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, TV_STATUS, str);
    }
    public static String getTvStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, TV_STATUS, "off");
    }
}
