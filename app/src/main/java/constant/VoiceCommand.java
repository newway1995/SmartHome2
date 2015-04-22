package constant;

import android.content.Context;

import org.kymjs.aframe.database.KJDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import module.database.CommandEntity;
import module.database.TVChannelEntity;
import utils.L;
import utils.StringUtils;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-09
 * Time: 16:00
 * 在这里判断输入的指令或者语音是控制哪一个电器
 */
public class VoiceCommand {
    /** 指令集合 **/
    private static List<String> commandList;

    /**
     * 解析单条语句,返回一条指令
     * @param context 上下文
     * @param source 输入源
     * @return List<String>
     */
    public static List<String> parseVoiceCommand(Context context, final String source){
        commandList = new ArrayList<>();
        parseAirCondition(context, source);
        parseBulb(context, source);
        parseFan(context, source);
        parseProjector(context, source);
        parseDoor(context, source);
        parseCurtain(context, source);

        int time = StringUtils.getInstance().getNumberBeforePattern(source);
        /** 将数据保存到数据库 **/
        saveCommandList2DB(context, commandList, time);
        return commandList;
    }

    /**
     * 保存CommandList到数据库
     * @param context
     *          上下文
     * @param commandList
     *          String
     * @param time
     *          Second
     */
    public static void saveCommandList2DB(Context context, final List<String> commandList, long time){
        CommandEntity.kjdb = KJDB.create(context);
        for (String command : commandList){
            CommandEntity entity = new CommandEntity(command, System.currentTimeMillis(), time * 1000, false);
            L.d("ThreadInfo", "1. saveCommandList2DB:" + entity.toString());
            CommandEntity.insert(entity);
        }
    }


    /**
     * 与风扇相关的指令
     */
    public static void parseFan(Context context, final String source){
        //open fan
        String []open_bulb = context.getResources().getStringArray(R.array.open_fan);
        for (String item : open_bulb) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                break;
            }
        }

        //close fan
        String []close_bulb = context.getResources().getStringArray(R.array.close_fan);
        for (String item : close_bulb) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                break;
            }
        }


        //add fan
        String []add_fan = context.getResources().getStringArray(R.array.add_fan);
        for (String item : add_fan) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_ADD);
                break;
            }
        }

        //add fan
        String []shake_fan = context.getResources().getStringArray(R.array.shake_fan);
        for (String item : shake_fan) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SHAKE);
                break;
            }
        }

        //mode_1
        String []mode_1 = context.getResources().getStringArray(R.array.fan_mode1);
        for (String item : mode_1) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                break;
            }
        }

        //mode_2
        String []mode_2 = context.getResources().getStringArray(R.array.fan_mode2);
        for (String item : mode_2) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                commandList.add(Command.FAN_ADD);
                break;
            }
        }


        //mode_3
        String []mode_3 = context.getResources().getStringArray(R.array.fan_mode3);
        for (String item : mode_3) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                commandList.add(Command.FAN_ADD);
                commandList.add(Command.FAN_ADD);
                break;
            }
        }
    }

    /**
     * 与空调相关的指令
     */
    public static void parseAirCondition(Context context, final String source){
        //open ac
        String []open_ac = context.getResources().getStringArray(R.array.open_air_condition);
        for (int i = 0; i < open_ac.length; i++)
        {
            if (source.contains(open_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_SWITCH);
                break;
            }
        }

        //close ac
        String []close_ac = context.getResources().getStringArray(R.array.close_air_condition);
        for (int i = 0; i < close_ac.length; i++)
        {
            if (source.contains(close_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_SWITCH);
                break;
            }
        }

        //down ac
        String []down_ac = context.getResources().getStringArray(R.array.down_air_condition);
        for (int i = 0; i < down_ac.length; i++)
        {
            if (source.contains(down_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_DOWN);
                break;
            }
        }

        //up ac
        String []up_ac = context.getResources().getStringArray(R.array.up_air_condition);
        for (int i = 0; i < up_ac.length; i++)
        {
            if (source.contains(up_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_UP);
                break;
            }
        }

        //cold mode ac
        String []cold_ac = context.getResources().getStringArray(R.array.cold_mode);
        for (int i = 0; i < cold_ac.length; i++)
        {
            if (source.contains(cold_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_COLD);
                break;
            }
        }

        //hot mode ac
        String []hot_ac = context.getResources().getStringArray(R.array.hot_mode);
        for (int i = 0; i < hot_ac.length; i++)
        {
            if (source.contains(hot_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_HOT);
                break;
            }
        }

        //drop mode ac
        String []drop_ac = context.getResources().getStringArray(R.array.drop_mode);
        for (int i = 0; i < drop_ac.length; i++)
        {
            if (source.contains(drop_ac[i]))
            {
                commandList.add(Command.AIRCONDITION_WETOUT);
                break;
            }
        }

        //temp
        String []temp_ac = context.getResources().getStringArray(R.array.air_condition_degree);
        for (int i = 0; i < temp_ac.length; i++)
        {
            if (!source.contains("空调"))
                break;
            if (!commandList.contains(Command.AIRCONDITION_SWITCH))
                commandList.add(Command.AIRCONDITION_SWITCH);
            if (source.contains(temp_ac[i]))
            {
                //调节度数
                for (int j = 0; j < i; j++)
                    commandList.add(Command.AIRCONDITION_UP);
            }
        }
    }

    /**
     * 与电灯相关的指令
     */
    public static void parseBulb(Context context, final String source){
        //open bulb
        String []open_bulb = context.getResources().getStringArray(R.array.open_bulb);
        for (int i = 0; i < open_bulb.length; i++)
        {
            if (source.contains(open_bulb[i]))
            {
                commandList.add(Command.LIGHT_OPEN);
                break;
            }
        }

        //close bulb
        String []close_bulb = context.getResources().getStringArray(R.array.close_bulb);
        for (int i = 0; i < close_bulb.length; i++)
        {
            if (source.contains(close_bulb[i]))
            {
                commandList.add(Command.LIGHT_OPEN);
                break;
            }
        }
    }

    /**
     * 投影仪变换
     */
    public static void parseProjector(Context context, final String source){
        //open pj
        String []open_pj = context.getResources().getStringArray(R.array.open_projectot);
        for (int i = 0; i < open_pj.length; i++)
        {
            if (source.contains(open_pj[i]))
            {
                commandList.add(Command.PROJECTOR_OPEN);
                break;
            }
        }

        //close pj
        String []close_pj = context.getResources().getStringArray(R.array.close_projector);
        for (int i = 0; i < close_pj.length; i++)
        {
            if (source.contains(close_pj[i]))
            {
                commandList.add(Command.PROJECTOR_OPEN);
                break;
            }
        }

        //up pj
        String []up_pj = context.getResources().getStringArray(R.array.up_projector);
        for (int i = 0; i < up_pj.length; i++)
        {
            if (source.contains(up_pj[i]))
            {
                commandList.add(Command.PROJECTOR_UP_ZOOM);
                break;
            }
        }

        //down pj
        String []down_pj = context.getResources().getStringArray(R.array.up_projector);
        for (int i = 0; i < down_pj.length; i++)
        {
            if (source.contains(down_pj[i]))
            {
                commandList.add(Command.PROJECTOR_DOWN_ZOOM);
                break;
            }
        }
    }

    /**
     * 开门相关的指令
     */
    public static void parseDoor(Context context, final String source) {
        //door_open
        String []door_open = context.getResources().getStringArray(R.array.door_open);
        for (String item : door_open) {
            if (source.contains(item)) {
                commandList.add(Command.DOOR_SWITCH);
                break;
            }
        }

        //door_close
        String []door_close = context.getResources().getStringArray(R.array.door_close);
        for (String item : door_close) {
            if (source.contains(item)) {
                commandList.add(Command.DOOR_SWITCH);
                break;
            }
        }
    }

    /**
     * 开窗相关的指令
     */
    public static void parseCurtain(Context context, final String source) {
        //door_open
        String []curtain_open = context.getResources().getStringArray(R.array.curtain_open);
        for (String item : curtain_open) {
            if (source.contains(item)) {
                commandList.add(Command.CURTAIN_SWITCH);
                break;
            }
        }

        //door_close
        String []curtain_close = context.getResources().getStringArray(R.array.curtain_close);
        for (String item : curtain_close) {
            if (source.contains(item)) {
                commandList.add(Command.CURTAIN_SWITCH);
                break;
            }
        }
    }


    /**
     * 是否显示电器状态
     * @return boolean
     */
    public static boolean isShowElecStatus(Context context, final String source){
        String content[] = context.getResources().getStringArray(R.array.current_elec_status);
        for (String str : content){
            if (source.contains(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否显示当前指令结合
     * @param context
     * @param source
     * @return
     */
    public static boolean isShowCommandList(Context context, final String source){
        String content[] = context.getResources().getStringArray(R.array.current_command_list);
        for (String str : content){
            if (source.contains(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 用户设置自己喜欢的电视节目
     */
    public static void parseTVProgramHabit(Context context, final String source) {
        String[] content = context.getResources().getStringArray(R.array.tv_program_list);

    }

    /**
     * 电视节目选择
     */
    public static HashMap<String, String> parseTVProgramSelect(Context context, final String source) {
        /** 电视节目列表 */
        String[] content = context.getResources().getStringArray(R.array.tv_program_list);
        if (isContainArray(context, source, R.array.tv_program_select) != null) {
            HashMap<String, String> map = new HashMap<>();
            /** 选择电视节目 */
            for (String item : content) {
                if (source.contains(item)) {
                    /** 如果用户选择查看 XX 频道,则进行处理 */
                    TVChannelEntity.kjdb = KJDB.create(context);
                    int number = TVChannelEntity.getNumberByChannelText(item);
                    map.put("channelText", item);
                    map.put("number", number+"");
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * 电视节目键值对设置<湖南卫视是13频道> .+是\\d{0,3}频道
     * @param context 上下文
     * @param source  输入源
     * @return HashMap
     */
    public static HashMap<String, String> parseTVProgramSetting(Context context, final String source) {
        String channelText = isContainArray(context, source, R.array.tv_program_list);
        if (Constant.REGEX_PROGRAM_SETTING.matcher(source).matches() && channelText != null) {
            HashMap<String, String> map = new HashMap<>();
            /** 放置频道Text《湖南卫视》 */
            map.put("channelText", channelText);
            int number = StringUtils.getInstance().getChannelNumberFromString(source);
            /** 放置频道Number 13 */
            map.put("number", number+"");
            /** 读取简写值 */
            String channelRel = context.getResources().getStringArray(R.array.tv_program_rel_list)[getIndexInArray(context, source, R.array.tv_program_list)];
            map.put("channelRel", channelRel);
            return map;
        }
        return null;
    }

    /**
     * source 是否包含某一个动作
     * @param context 上下文
     * @param source 输入源
     * @param arrayId 数组
     * @return 如果包含返回 true， 否则返回 false
     */
    public static String isContainArray(Context context, final String source, int arrayId) {
        String[] content = context.getResources().getStringArray(arrayId);
        for (String item : content) {
            if (source.contains(item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取index在数组当中
     * @param context 上下文
     * @param source 输入源
     * @param arrayId id
     * @return index
     */
    public static int getIndexInArray(Context context, final String source, int arrayId) {
        String[] content = context.getResources().getStringArray(arrayId);
        for (int i = 0; i < content.length; i++) {
            if (source.contains(content[i])) {
                return i;
            }
        }
        return -1;
    }
}
