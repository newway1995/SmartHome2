package constant;

import android.content.Context;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-09
 * Time: 16:00
 * 在这里判断输入的指令或者语音是控制哪一个电器
 */
public class VoiceCommand {
    //语音指令的类别,DEFAULT 为默认,不处理
    public enum VoiceCommandCategory{
        AIRCONDITION, BULB, FAN, PROJECTOR, HOTTER, TELEVISION, DEFAULT
    }

    /**
     * 解析单条语句,返回一条指令
     * @param context
     * @param source
     * @return
     */
    public static String parseVoiceCommand(Context context, final String source){
        switch (getVoiceCommandCategory(source))
        {
            case BULB://解析电灯
                if (isOpenBulb(context, source))
                    return Command.LIGHT_OPEN;
                break;
            case FAN:
                break;
            case AIRCONDITION:
                break;
            case PROJECTOR:
                break;
            case TELEVISION:
                break;
            case HOTTER:
                break;
            case DEFAULT:
                break;
        }
        return null;
    }


    /**
     * 解析短句,开灯的判断
     * @param context
     * @param source
     * @return
     */
    public static boolean isOpenBulb(Context context, final String source){
        //获取到所有开灯的短句
        String []commandLists = context.getResources().getStringArray(R.array.open_bulb);
        for (String itemStr : commandLists) // 判断是否符合开灯
        {
            if (source.contains(itemStr))
                return true;
        }
        return false;
    }

    /**
     * 返回指令的类别
     * @param source
     * @return
     */
    public static VoiceCommandCategory getVoiceCommandCategory(final String source){
        if (source.contains("灯"))//可能是灯
            return VoiceCommandCategory.BULB;
        else if (source.contains("空调"))
            return VoiceCommandCategory.AIRCONDITION;
        else if (source.contains("风扇"))
            return VoiceCommandCategory.FAN;
        else if (source.contains("投影仪"))
            return VoiceCommandCategory.PROJECTOR;
        else if (source.contains("电视"))
            return VoiceCommandCategory.TELEVISION;
        else if (source.contains("热水器"))
            return VoiceCommandCategory.HOTTER;
        return VoiceCommandCategory.DEFAULT;
    }
}
