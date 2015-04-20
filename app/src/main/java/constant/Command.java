package constant;

/**
 * Package: constant
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-20
 * Time: 21:00
 * 手机向树莓派发送的指令集
 */
public class Command {

    public static final String PHONE = "PHONE";
    public static final String RASPBERRY = "RASPBERRY";
    public static final String COMMAND_DEVICE = "device";
    public static final String COMMAND = "command";
    public static final String TIME = "time";
    public static final String COMMAND_HTTP_URL = "http://1.newway.sinaapp.com/index.php";

    /**
     * 空调指令集
     */
    public static final String AIRCONDITION_SWITCH = "AIR_SWITCH";
    public static final String AIRCONDITION_UP = "AIR_UP";
    public static final String AIRCONDITION_DOWN = "AIR_DOWN";
    public static final String AIRCONDITION_HOT = "AIR_HOT";//制热
    public static final String AIRCONDITION_COLD = "AIR_COLD";//制冷
    public static final String AIRCONDITION_WETOUT = "AIR_WETOUT";//除湿

    /**
     * 电视机指令集
     */
    public static final String TELEVISION_UP_VOL = "TELEVISION_UP_VOL";
    public static final String TELEVISION_DOWN_VOL = "TELEVISION_DOWN_VOL";
    public static final String TELEVISION_UP_CHANNEL = "TELEVISION_UP_CHANNEL";
    public static final String TELEVISION_DOWN_CHANNEL = "TELEVISION_DOWN_CHANNEL";
    public static final String TELEVISION_ONE = "TELEVISION_1";
    public static final String TELEVISION_TWO = "TELEVISION_2";
    public static final String TELEVISION_THREE = "TELEVISION_3";
    public static final String TELEVISION_FOUR = "TELEVISION_4";
    public static final String TELEVISION_FIVE = "TELEVISION_5";
    public static final String TELEVISION_SIX = "TELEVISION_6";
    public static final String TELEVISION_SEVEN = "TELEVISION_7";
    public static final String TELEVISION_EIGHT = "TELEVISION_8";
    public static final String TELEVISION_NINE = "TELEVISION_9";
    public static final String TELEVISION_ZERO = "TELEVISION_0";
    public static final String TELEVISION_SWITCH = "TELEVISION_SWITCH";
    public static final String TELEVISION_OK = "TELEVISION_OK";

    /**
     * 电风扇指令集
     */
    public static final String FAN_ADD = "FAN_ADD";
    public static final String FAN_SWITCH = "FAN_SWITCH";
    public static final String FAN_SHAKE = "FAN_SHAKE";

    /**
     * 电灯指令集
     */
    public static final String LIGHT_OPEN = "BULB_SWITCH";

    /**
     * 投影仪指令集
     */
    public static final String PROJECTOR_UP_ZOOM = "PJ_PROJECTOR_UP_ZOOM";//焦距拉近
    public static final String PROJECTOR_DOWN_ZOOM = "PJ_PROJECTOR_DOWN_ZOOM";//焦距拉远
    public static final String PROJECTOR_OPEN = "PJ_PROJECTOR_OPEN";//开关
}
