package utils;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-06
 * Time: 18:07
 * FIXME
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
}
