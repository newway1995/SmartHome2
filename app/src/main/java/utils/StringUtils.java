package utils;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-06
 * Time: 18:07
 * 字符串帮助类
 */
public class StringUtils {
    private StringUtils(){}

    public static class ClassHolder{
        private final static StringUtils instance = new StringUtils();
    }

    public static StringUtils getInstance(){
        return ClassHolder.instance;
    }

    public boolean hasCCTV(String words){
        if (words.contains("CCTV"))
            return true;
        return false;
    }

    /**
     * 从“湖南卫视是13频道”解析出13
     * @return int
     */
    public int getChannelNumberFromString(final String source) {
        //如果是大写的情况
        if (getChNum(source) != null && getChNum(source).length() > 0){
            return (int)chnNum2Digit(getChNum(source));
        }
        int result = 0;
        if (source.contains("频道")) {
            int index = source.indexOf("频道");
            int count = 1;
            for(int i = index - 1; i >= 0; i-- ){
                char ch = source.charAt(i);
                if (ch - 48 >= 0 && ch -48 < 10) {
                    result += count * (ch - 48);
                    count *= 10;
                }else {
                    break;
                }
            }
        }
        return result;
    }


    /**
     * 从一段字符串当中获取source之前的数字
     * @param source
     * @return 秒钟数
     */
    public int getNumberBeforePattern(final String source){
        //如果是大写的情况
        if (getChNum(source) != null && getChNum(source).length() > 0){
            return (int)chnNum2Digit(getChNum(source));
        }
        int time = 0;//结果
        if (source.contains("秒钟") || source.contains("秒")){
            int index = source.indexOf("秒");
            int count = 1;
            for(int i = index - 1; i >= 0; i-- ){
                char ch = source.charAt(i);
                if (ch - 48 >= 0 && ch -48 < 10) {
                    time += count * (ch - 48);
                    count *= 10;
                }else {
                    break;
                }
            }
            return time;
        }else if(source.contains("分钟")){
            int index = source.indexOf("分钟");
            int count = 1;
            for(int i = index - 1; i >= 0; i-- ){
                char ch = source.charAt(i);
                if (ch - 48 >= 0 && ch -48 < 10) {
                    time += count * (ch - 48);
                    count *= 10;
                }else {
                    break;
                }
            }
            return time * 60;
        }
        return time;
    }


    /**
     * 读取大写数字
     * @param source
     * @return
     */
    public String getChNum(String source){
        StringBuilder sb = new StringBuilder();
        if (source.contains("秒钟") || source.contains("秒")){
            int index = source.indexOf("秒");
            for(int i = index - 1; i >= 0; i-- ){
                switch (source.charAt(i)) {
                    case '零':
                    case '一':
                    case '二':
                    case '三':
                    case '四':
                    case '五':
                    case '六':
                    case '七':
                    case '八':
                    case '九':
                    case '十':
                    case '百':
                    case '千':
                    case '万':
                    case '亿':
                        sb.append(source.charAt(i));
                        break;
                    default:
                        return sb.reverse().toString();
                }
            }
            return sb.reverse().toString();
        }else if(source.contains("分钟")){
            int index = source.indexOf("分钟");
            for(int i = index - 1; i >= 0; i-- ){
                switch (source.charAt(i)) {
                    case '零':
                    case '一':
                    case '二':
                    case '三':
                    case '四':
                    case '五':
                    case '六':
                    case '七':
                    case '八':
                    case '九':
                    case '十':
                    case '百':
                    case '千':
                    case '万':
                    case '亿':
                        sb.append(source.charAt(i));
                        break;
                    default:
                        return sb.reverse().toString();
                }
            }
            return sb.reverse().toString();
        }else if(source.contains("频道")){
            int index = source.indexOf("频道");
            for(int i = index - 1; i >= 0; i-- ){
                switch (source.charAt(i)) {
                    case '零':
                    case '一':
                    case '二':
                    case '三':
                    case '四':
                    case '五':
                    case '六':
                    case '七':
                    case '八':
                    case '九':
                    case '十':
                    case '百':
                    case '千':
                    case '万':
                    case '亿':
                        sb.append(source.charAt(i));
                        break;
                    default:
                        return sb.reverse().toString();
                }
            }
            return sb.reverse().toString();
        }
        return sb.reverse().toString();
    }

    /**
     * 一万转10000
     * @param chnNum
     * @return
     */
    public long chnNum2Digit(String chnNum) {
        // initialize map
        java.util.Map<String, Integer> unitMap = new java.util.HashMap<String, Integer>();
        unitMap.put("个", 1);// 仅在数据存储时使用
        unitMap.put("十", 10);
        unitMap.put("百", 100);
        unitMap.put("千", 1000);
        unitMap.put("万", 10000);
        unitMap.put("亿", 100000000);

        java.util.Map<String, Integer> numMap = new java.util.HashMap<String, Integer>();
        numMap.put("零", 0);
        numMap.put("一", 1);
        numMap.put("二", 2);
        numMap.put("三", 3);
        numMap.put("四", 4);
        numMap.put("五", 5);
        numMap.put("六", 6);
        numMap.put("七", 7);
        numMap.put("八", 8);
        numMap.put("九", 9);

        // 数据存储结构：
        // 单位 数量
        // "个" num
        // "十" num
        // "百" num
        // "千" num
        // "万" num
        // "亿" num
        java.util.Map<String, Long> dataMap = new java.util.LinkedHashMap<String, Long>();

        // 保存"亿"或"万"之前存在的多位数
        java.util.List<Long> multiNumList = new java.util.ArrayList<Long>();

        long tempNum = 0;
        for (int i = 0; i < chnNum.length(); i++) {
            char bit = chnNum.charAt(i);

            // 因为"亿"或"万"存在多位情况，所以需要进行判断
            boolean isExist = false;
            // 存在"亿"或"万"情况(不计算当前索引位)
            if ((chnNum.indexOf('亿', i) != -1 || chnNum.indexOf('万', i) != -1)
                    && chnNum.charAt(i) != '亿' && chnNum.charAt(i) != '万') {
                isExist = true;
            }

            // 数字
            if (numMap.containsKey(bit + "")) {
                if (i != chnNum.length() - 1) {
                    tempNum = tempNum + numMap.get(bit + "");
                }
                // 最末位数字情况
                else {
                    dataMap.put("个", Long.valueOf(numMap.get(bit + "") + ""));
                    tempNum = 0;
                }
            } else if (bit == '亿') {
                // 存在"万亿"情况，取出"万"位值*10000,0000后重新put到map
                if (i - 1 >= 0 && chnNum.charAt(i - 1) == '万') {
                    Long dataValue = dataMap.get("万");
                    if (dataValue != null && dataValue > 0) {
                        dataMap.put("万", dataValue * unitMap.get(bit + ""));
                    }
                    continue;
                }

                // 最后一位数进list等待处理
                if (tempNum != 0) {
                    multiNumList.add(tempNum);
                }
                // 处理"亿"之前的多位数
                long sum = 0;
                for (Long num : multiNumList) {
                    sum += num;
                }
                multiNumList.clear();
                dataMap.put("亿", sum);
                tempNum = 0;
            } else if (bit == '万') {
                // 最后一位数进list等待处理
                if (tempNum != 0) {
                    multiNumList.add(tempNum);
                }
                // 处理"万"之前的多位数
                long sum = 0;
                for (Long num : multiNumList) {
                    sum += num;
                }
                multiNumList.clear();
                dataMap.put("万", sum);
                tempNum = 0;
            } else if (bit == '千' && tempNum > 0) {
                // 存在"亿"或"万"情况，临时变量值*"千"单位值进list等待处理
                if (isExist) {
                    multiNumList.add(tempNum * unitMap.get(bit + ""));
                    tempNum = 0;
                }
                // 不存在"亿"或"万"情况，临时变量值put到map
                else {
                    dataMap.put("千", tempNum);
                    tempNum = 0;
                }
            } else if (bit == '百' && tempNum > 0) {
                // 存在"亿"或"万"情况，临时变量值*"百"单位值进list等待处理
                if (isExist) {
                    multiNumList.add(tempNum * unitMap.get(bit + ""));
                    tempNum = 0;
                }
                // 不存在"亿"或"万"情况，临时变量值put到map
                else {
                    dataMap.put("百", tempNum);
                    tempNum = 0;
                }
            } else if (bit == '十') {
                // 存在"亿"或"万"情况，临时变量值*"十"单位值进list等待处理
                if (isExist) {
                    if (tempNum != 0) {
                        multiNumList.add(tempNum * unitMap.get(bit + ""));
                        tempNum = 0;
                    }
                    // 将"十"转换成"一十"
                    else {
                        tempNum = 1 * unitMap.get(bit + "");
                    }
                }
                // 不存在"亿"或"万"情况，临时变量值put到map
                else {
                    if (tempNum != 0) {
                        dataMap.put("十", tempNum);
                    }
                    // 将"十"转换成"一十"
                    else {
                        dataMap.put("十", 1l);
                    }
                    tempNum = 0;
                }
            }
        }

        long sum = 0;
        java.util.Set<String> keys = dataMap.keySet();
        for (String key : keys) {
            Integer unitValue = unitMap.get(key);
            Long dataValue = dataMap.get(key);
            sum += unitValue * dataValue;
        }
        return sum;
    }
}
