package utils;

import java.util.regex.Pattern;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-07-09
 * Time: 22:43
 * 正则表达式
 */
public class PatternUtils {

    /**
     * 检测是否符合正则表达式
     * @param patternStr 正则表达式内容
     * @param content 需要匹配的内容
     * @return true | false
     */
    public static boolean contains(String patternStr, String content) {
        Pattern pattern = Pattern.compile(patternStr);
        return pattern.matcher(content).matches();
    }

    /**
     * 灯的相关操作
     * @param content 检测的字符串
     * @return true | false
     */
    public static boolean openBulb(String content) {
        return contains("/(灯.{0,4}开)|(开.{0,4}灯)/", content);
    }

    public static boolean closeBulb(String content) {
        return contains("/(灯.{0,4}关)|(关.{0,4}灯)/", content);
    }

    /**
     * 风扇的相关操作
     * @param content content
     * @return true | false
     */
    public static boolean openFan(String content) {
        return contains("/(风扇.{0,4}开)|(开.{0,4}风扇)/", content);
    }

    public static boolean closeFan(String content) {
        return contains("/(风扇.{0,4}关)|(关.{0,4}风扇)/", content);
    }

    public static boolean upFan(String content) {
        return contains("/(风扇.{0,4}大)|(大.{0,4}风扇)/", content);
    }

    public static boolean shakeFan(String content) {
        return contains("/(风扇.{0,4}摇头)|(摇头.{0,4}风扇)/", content);
    }

    public static boolean timingFan(String content) {
        return contains("/(风扇.{0,4}定时)|(定时.{0,4}风扇)/", content);
    }

    /**
     * 电视的相关操作
     * @param content 内容
     * @return true | false
     */
    public static boolean openTV(String content) {
        return contains("/(电视.{0,4}开)|(开.{0,4}电视)/", content);
    }

    public static boolean closeTV(String content) {
        return contains("/(电视.{0,4}关)|(关.{0,4}电视)/", content);
    }

    public static boolean upVolumnTV(String content) {
        return contains("/(电视.{0,2}音量.{0,2}[高大])/", content);
    }

    public static boolean downVolumnTV(String content) {
        return contains("/(电视.{0,2}音量.{0,2}[低小])/", content);
    }

    public static boolean upChannelTV(String content) {
        return contains("/(电视.{0,2}[(频道)(节目)].{0,2}[高大])/", content);
    }

    public static boolean downChannelTV(String content) {
        return contains("/(电视.{0,2}[(频道)(节目)].{0,2}[小低])/", content);
    }

    /**
     * 空调的相关操作
     * @param content 内容
     * @return true | false
     */
    public static boolean openAir(String content) {
        return contains("/(空调.{0,4}开)|(开.{0,4}空调)/", content);
    }


    public static boolean closeAir(String content) {
        return contains("/(空调.{0,4}关)|(关.{0,4}空调)/", content);
    }

    public static boolean autoAir(String content) {
        return contains("/(空调.{0,4}自动模式)/", content);
    }

    public static boolean heatAir(String content) {
        return contains("/(空调.{0,4}制热)|(制热.{0,4}空调)/", content);
    }

    public static boolean coldAir(String content) {
        return contains("/(空调.{0,4}制冷)|(制冷.{0,4}空调)/", content);
    }

    public static boolean dropAir(String content) {
        return contains("/(空调.{0,4}除湿)|(除湿.{0,4}空调)/", content);
    }

    public static boolean upAir(String content) {
        return contains("/(空调.{0,4}[高大])/", content);
    }

    public static boolean downAir(String content) {
        return contains("/(空调.{0,4}[低小])/", content);
    }
}
