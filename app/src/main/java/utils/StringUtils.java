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
     * 从一段字符串当中获取source之前的数字
     * @param source
     * @return 秒钟数
     */
    public int getNumberBeforePattern(final String source){
        int time = 0;//结果
        if (source.contains("秒钟") || source.contains("秒")){
            int index = source.indexOf("秒");
            int count = 1;
            for(int i = index - 1; i >= 0; i-- ){
                char ch = upInt2DownInt(source.charAt(i));
                if (!(ch - 48 >= 0 && ch - 48 <= 10))
                    break;
                time += count * (ch - 48);
                count *= 10;
            }
            return time;
        }else if(source.contains("分钟")){
            int index = source.indexOf("分钟");
            int count = 1;
            for(int i = index - 1; i >= 0; i-- ){
                char ch = upInt2DownInt(source.charAt(i));
                if (!(ch - 48 >= 0 && ch - 48 <= 10))
                    break;
                time += count * (ch - 48);
                count *= 10;
            }
            return time * 60;
        }
        return time;
    }

    /**
     * 大写数字变小写
     * @return
     */
    private char upInt2DownInt(char ch){
        switch (ch)
        {
            case '一':
                return '1';
            case '二':
                return '2';
            case '三':
                return '3';
            case '四':
                return '4';
            case '五':
                return '5';
            case '六':
                return '6';
            case '七':
                return '7';
            case '八':
                return '8';
            case '九':
                return '9';
            case '十':
                return ':';
        }
        return ' ';
    }
}
