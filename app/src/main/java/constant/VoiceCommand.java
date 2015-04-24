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
    /** 返回内容 */
    private static List<String> resultsList;
    /** 多久之后执行,因为需要在后面使用到 */
    private static int time = 0;
    /** 保存前一次的时间 */
    private static int lastTime = time;

    /**
     * 解析全部语句,返回所有指令
     * @param context 上下文
     * @param source 输入源
     * @return List<String> 返回的是FeedBack数据
     */
    public static List<String> parseVoiceCommand(Context context, final String source) {
        commandList = new ArrayList<>();
        resultsList = new ArrayList<>();
        String[] commands = source.split("，");
        for (String item : commands) {
            parseVoiceItemCommand(context, item);
        }
        return resultsList;
    }

    /**
     * 解析单条语句,返回一条指令
     * @param context 上下文
     * @param source 输入源
     * @return List<String>
     */
    public static List<String> parseVoiceItemCommand(Context context, final String source){
        /** 定时时间 */
        time = StringUtils.getInstance().getNumberBeforePattern(source);

        commandList = new ArrayList<>();
        //resultsList = new ArrayList<>();
        parseAirCondition(context, source);
        parseBulb(context, source);
        parseFan(context, source);
        parseProjector(context, source);
        parseDoor(context, source);
        parseCurtain(context, source);
        startPerform(context, source);
        parseTelevision(context, source);
        parseHeater(context, source);

        /** 将数据保存到数据库 **/
        if (commandList != null) {
            if (time == 0 && lastTime != 0) {
                saveCommandList2DB(context, commandList, lastTime);
            } else {
                saveCommandList2DB(context, commandList, time);
            }
        }
        lastTime = time;//经过了修改

        return commandList;
    }

    /**
     * 获取反馈,必须在执行完parseVoiceCommand之后调用
     * @return 反馈的内容List
     */
    public static String getVoiceFeedback() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("好的,我知道了");
        for (String item : resultsList) {
            stringBuilder.append(item);
        }
        stringBuilder.append(".");
        return stringBuilder.toString();
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
            L.d("parseVoiceCommand", "1. saveCommandList2DB:" + entity.toString());
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
                commandList.add(Command.FAN_ADD);
                resultsList.add(",风扇" + (time == 0 ? "已经" : "马上") + "打开了");
                break;
            }
        }

        //close fan
        String []close_bulb = context.getResources().getStringArray(R.array.close_fan);
        for (String item : close_bulb) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SWITCH);
                resultsList.add(",风扇" + (time == 0 ? "已经" : "马上") + "关闭了");
                break;
            }
        }


        //add fan
        String []add_fan = context.getResources().getStringArray(R.array.add_fan);
        for (String item : add_fan) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_ADD);
                resultsList.add(",风扇" + (time == 0 ? "已经" : "马上") + "调大了");
                break;
            }
        }

        //shake fan
        String []shake_fan = context.getResources().getStringArray(R.array.shake_fan);
        for (String item : shake_fan) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_SHAKE);
                resultsList.add(",风扇" + (time == 0 ? "已经" : "马上") + "摇头了");
                break;
            }
        }

        //mode_1
        String []mode_1 = context.getResources().getStringArray(R.array.fan_mode1);
        for (String item : mode_1) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_ADD);
                resultsList.add("," + (time == 0 ? "已经" : "马上") + "帮您把风扇调大了一档");
                break;
            }
        }

        //mode_2
        String []mode_2 = context.getResources().getStringArray(R.array.fan_mode2);
        for (String item : mode_2) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_ADD);
                commandList.add(Command.FAN_ADD);
                resultsList.add("," + (time == 0 ? "已经" : "马上") + "帮您把风扇调大了两档");
                break;
            }
        }


        //mode_3
        String []mode_3 = context.getResources().getStringArray(R.array.fan_mode3);
        for (String item : mode_3) {
            if (source.contains(item))
            {
                commandList.add(Command.FAN_ADD);
                commandList.add(Command.FAN_ADD);
                commandList.add(Command.FAN_ADD);
                resultsList.add("," + (time == 0 ? "已经" : "马上") + "帮您把风扇调大了三档");
                break;
            }
        }

        //time
        String []times = context.getResources().getStringArray(R.array.fan_time);
        for (String item : times) {
            if (source.contains(item)) {
                commandList.add(Command.FAN_TIME);
                resultsList.add("," + (time == 0 ? "已经" : "马上") + "帮您将风扇定时");
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
                resultsList.add(",空调" + (time == 0 ? "已经" : "马上") + "打开了");
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
                resultsList.add(",空调" + (time == 0 ? "已经" : "马上") + "关闭了");
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
                resultsList.add(",空调温度" + (time == 0 ? "已经" : "马上") + "调小了");
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
                resultsList.add(",空调温度" + (time == 0 ? "已经" : "马上") + "调大了");
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
                resultsList.add(",空调当前为制冷模式");
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
                resultsList.add(",空调当前为制热模式");
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
                resultsList.add(",空调当前为除湿模式");
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
                resultsList.add(",电灯" + (time == 0 ? "已经" : "马上") + "打开了");
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
                resultsList.add(",电灯" + (time == 0 ? "已经" : "马上") + "关闭了");
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
                resultsList.add(",投影仪" + (time == 0 ? "已经" : "马上") + "打开了");
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
                resultsList.add(",投影仪" + (time == 0 ? "已经" : "马上") + "关闭了");
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
                resultsList.add(",投影仪焦距正在拉远");
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
                resultsList.add(",投影仪焦距正在拉近");
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
                resultsList.add(",门" + (time == 0 ? "已经" : "马上") + "打开");
                break;
            }
        }

        //door_close
        String []door_close = context.getResources().getStringArray(R.array.door_close);
        for (String item : door_close) {
            if (source.contains(item)) {
                commandList.add(Command.DOOR_SWITCH);
                resultsList.add(",门" + (time == 0 ? "已经" : "马上") + "关闭");
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
                resultsList.add(",窗户" + (time == 0 ? "已经" : "马上") + "打开");
                break;
            }
        }

        //door_close
        String []curtain_close = context.getResources().getStringArray(R.array.curtain_close);
        for (String item : curtain_close) {
            if (source.contains(item)) {
                commandList.add(Command.CURTAIN_SWITCH);
                resultsList.add(",窗户" + (time == 0 ? "已经" : "马上") + "关闭");
                break;
            }
        }
    }

    /**
     * 处理电视机的逻辑
     * @param context 上下文
     * @param source 输入源
     */
    public static void parseTelevision(Context context, final String source) {
        /** 开 */
        String[] tv_open = context.getResources().getStringArray(R.array.tv_open);
        for (String item : tv_open) {
            if (source.contains(item)) {
                commandList.add(Command.TELEVISION_SWITCH);
                resultsList.add(",电视机" + (time == 0 ? "已经" : "马上") + "打开");
            }
        }

        /** 关 */
        String[] tv_close = context.getResources().getStringArray(R.array.tv_close);
        for (String item : tv_close) {
            if (source.contains(item)) {
                commandList.add(Command.TELEVISION_SWITCH);
                resultsList.add(",电视机" + (time == 0 ? "已经" : "马上") + "关闭");
            }
        }
    }


    /**
     * 处理热水器的逻辑
     */
    public static void parseHeater(Context context, final String source) {
        String[] heaterOpen = context.getResources().getStringArray(R.array.heater_open);
        for (String item : heaterOpen) {
            if (source.contains(item)) {
                commandList.add(Command.HEATER_SWITCH);
                resultsList.add(",热水器" + (time == 0 ? "已经" : "马上") + "开始工作了");
            }
        }

        String[] heaterClose = context.getResources().getStringArray(R.array.heater_close);
        for (String item : heaterClose) {
            if (source.contains(item)) {
                commandList.add(Command.HEATER_SWITCH);
                resultsList.add(",热水器马上停止工作");
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
            //不包含这种电视台
            map.put("error", "error");
            return map;
        }
        return null;//不属于这种类型的语句
    }

    /**
     * 返回电视节目信息,这里需要造一个假数据
     */
    public static String getTVProgramInfo(Context context, final String str) {
        String[] tvLists = context.getResources().getStringArray(R.array.tv_program_list);
        for (String item : tvLists) {
            if (str.contains("cctv1") || str.contains("CCTV1")) {
                return "动物世界 : 2015-04-26 00:35\n生活早参考 : 02:28\n朝闻天下 : 06:00\n生活早参考 : 08:35\n新闻30分 : 12:00\n微笑妈妈 : 12:30\n新闻联播 : 18:59\n前情提要《别让我看见》 : 19:35\n晚间新闻 : 21:59\n撒贝宁时间 : 22:37\n";
            } else if (str.contains("湖北卫视节目")) {
                return "电视剧：独狼(23~29) : 00:45\n湖北新闻 : 07:00\n天生我财 : 07:56\n电视剧：婆媳的战国时代(35、40) : 12:00\n电视剧：红色(3~5) : 14:15\n新闻联播 : 18:59\n电视剧：爱的秘笈(9、15) : 19:35";
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
     * 开始演示
     */
    public static void startPerform(Context context, final String source) {
        String[] content = context.getResources().getStringArray(R.array.start_perform);
        for (String item : content) {
            /** 开始演示 */
            if (source.contains(item)) {
                commandList.add(Command.TELEVISION_SWITCH);
                resultsList.add(",电视已经打开");
                commandList.add(Command.FAN_SWITCH);
                resultsList.add(",电风扇已经关闭");
                commandList.add(Command.LIGHT_OPEN);
                resultsList.add(",电灯已经打开");
                break;
            }
        }

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
