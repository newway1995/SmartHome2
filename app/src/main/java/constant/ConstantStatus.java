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
    public static final String DEFAULT_INTEGER_VALUE = "-1";
    //电灯状态
    public static final String BULB_SWITCH = "BULB_SWITCH";
    //电扇状态
    public static final String FAN_SWITCH = "FAN_SWITCH";
    public static final String FAN_STATUS = "FAN_STATUS";
    public static final String FAN_SHAKE = "FAN_SHAKE";
    public static final String FAN_ZERO_DANG = "1";
    public static final String FAN_ONE_DANG = "1";
    public static final String FAN_TWO_DANG = "2";
    public static final String FAN_THREE_DANG = "3";

    //投影仪
    public static final String PJ_SWITCH = "PJ_SWITCH";
    //空调
    public static final String AIR_SWITCH = "AIR_SWITCH";
    public static final String AIR_STATUS = "AIR_STATUS";
    public static final String AIR_MODE = "AIR_MODE";
    //电视机
    public static final String TV_SWITCH = "TV_SWITCH";
    public static final String TV_STATUS = "TV_STATUS";
    //门
    public static final String DOOR_SWITCH = "DOOR_SWITCH";
    //窗帘
    public static final String CURTAIN_SWITCH = "CURTAIN_SWITCH";
    //热水器状态
    public static final String HEATER_SWITCH = "HEATER_SWITCH";
    public static final String HEATER_TEMP = "HEATER_TEMP";
    
    public static final String SWITCH_ON = "on";
    public static final String SWITCH_OFF = "off";

    //热水器
    public static void setHeaterSwitch(Context context, String str) {
        PreferenceHelper.write(context, ELEC_APP_INFO, HEATER_SWITCH, str);
    }
    public static String getHeaterSwitch(Context context) {
        return PreferenceHelper.readString(context, ELEC_APP_INFO, HEATER_SWITCH, SWITCH_OFF);
    }
    public static void setHeaterTemp(Context context,  int temp) {
        PreferenceHelper.write(context, ELEC_APP_INFO, HEATER_TEMP, temp);
    }
    public static int getHeaterTemp(Context context) {
        return PreferenceHelper.readInt(context, ELEC_APP_INFO, HEATER_TEMP, 0);
    }


    //电灯
    public static void setBulbSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, BULB_SWITCH, str);
    }
    public static String getBulbSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, BULB_SWITCH, SWITCH_OFF);
    }


    //风扇
    public static void setFanSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_SWITCH, str);
    }
    public static String getFanSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_SWITCH, SWITCH_OFF);
    }
    public static void setFanShake(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_SHAKE, str);
    }
    public static String getFanShake(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_SHAKE, SWITCH_OFF);
    }
    public static void setFanStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, FAN_STATUS, str);
    }
    public static String getFanStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, FAN_STATUS, DEFAULT_INTEGER_VALUE);
    }


    //投影仪
    public static void setPjSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, PJ_SWITCH, str);
    }
    public static String getPjSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, PJ_SWITCH, SWITCH_OFF);
    }


    //空调
    public static void setAirSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_SWITCH, str);
    }
    public static String getAirSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_SWITCH, SWITCH_OFF);
    }
    public static void setAirStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_STATUS, str);
    }
    public static String getAirStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_STATUS, DEFAULT_INTEGER_VALUE);
    }
    public static void setAirMode(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, AIR_MODE, str);
    }
    public static String getAirMode(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, AIR_MODE, SWITCH_OFF);
    }


    //电视机
    public static void setTVSwitch(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, TV_SWITCH, str);
    }
    public static String getTVSwitch(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, TV_SWITCH, SWITCH_OFF);
    }
    public static void setTvStatus(Context context, String str){
        PreferenceHelper.write(context, ELEC_APP_INFO, TV_STATUS, str);
    }
    public static String getTvStatus(Context context){
        return PreferenceHelper.readString(context, ELEC_APP_INFO, TV_STATUS, DEFAULT_INTEGER_VALUE);
    }


    //door
    public static void setDoorSwitch(Context context, String str) {
        PreferenceHelper.write(context, ELEC_APP_INFO, DOOR_SWITCH, str);
    }
    public static String getDoorSwitch(Context context) {
        return PreferenceHelper.readString(context, ELEC_APP_INFO, DOOR_SWITCH, DEFAULT_INTEGER_VALUE);
    }
    //curtain
    public static void setCurtainSwitch(Context context, String str) {
        PreferenceHelper.write(context, ELEC_APP_INFO, CURTAIN_SWITCH, str);
    }
    public static String getCurtainSwitch(Context context) {
        return PreferenceHelper.readString(context, ELEC_APP_INFO, CURTAIN_SWITCH, DEFAULT_INTEGER_VALUE);
    }

    /**
     * 获取所有的状态
     * @param context
     * @return
     */
    public static String getAllStatus(Context context){
        StringBuilder stringBuilder = new StringBuilder();
        //判断电灯状态
        if (getBulbSwitch(context).equals(SWITCH_ON))
            stringBuilder.append("电灯状态: 开\n\n");
        else if (getBulbSwitch(context).equals(SWITCH_OFF))
            stringBuilder.append("电灯状态: 关\n\n");

        //判断投影仪状态
        if (getPjSwitch(context).equals(SWITCH_ON))
            stringBuilder.append("投影仪状态: 开\n\n");
        else if (getPjSwitch(context).equals(SWITCH_OFF))
            stringBuilder.append("投影仪状态: 关\n\n");

        //判断电风扇状态
        if (getFanSwitch(context).equals(SWITCH_ON)){
            stringBuilder.append("电风扇状态: 开\n");
            stringBuilder.append("\t\t电风扇档位为: " + getFanStatus(context) + "\n");
            stringBuilder.append("\t\t电风扇是否摇头为: " + (getFanShake(context).equals(SWITCH_ON) ? "是" : "否" + "\n\n"));
        }else if (getFanSwitch(context).equals(SWITCH_OFF)){
            stringBuilder.append("电风扇状态: 关\n\n");
        }

        //判断电视机状态
        if (getTVSwitch(context).equals(SWITCH_ON)){
            stringBuilder.append("电视机状态: 开\n");
            stringBuilder.append("\t\t电视机节目为: " + getTvStatus(context) + "\n\n");
        }else if (getTVSwitch(context).equals(SWITCH_OFF)){
            stringBuilder.append("电视机状态: 关\n\n");
        }

        //判断空调状态
        if (getAirSwitch(context).equals(SWITCH_ON)){
            stringBuilder.append("空调状态: 开\n");
            stringBuilder.append("\t\t空调模式: " + getAirMode(context));
            stringBuilder.append("\n\t\t空调档位为: " + getAirStatus(context) + "\n\n");
        }else if (getAirSwitch(context).equals(SWITCH_OFF)){
            stringBuilder.append("空调状态: 关\n\n");
        }

        //判断门的状态
        stringBuilder.append("门禁状态: ");
        stringBuilder.append((getDoorSwitch(context).equals(SWITCH_ON) ? "开" : "关"));
        //判断窗帘的状态
        stringBuilder.append("\n\n窗帘状态: ");
        stringBuilder.append((getCurtainSwitch(context).equals(SWITCH_ON) ? "开" : "关"));
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
    }

}
