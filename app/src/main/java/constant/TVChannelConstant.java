package constant;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-09
 * Time: 15:19
 * 电视节目表
 */
public class TVChannelConstant {
    //央视节目表
    public static final String CCTV1 = "cctv1";//CCTV-1 综合
    public static final String CCTV2 = "cctv2";//CCTV-2 财经
    public static final String CCTV3 = "cctv3";//CCTV-3 综艺
    public static final String CCTV4 = "cctv4";//CCTV-4 (亚洲)
    public static final String CCTV5 = "cctv5";//CCTV-5 体育
    public static final String CCTV6 = "cctv6";//CCTV-6 电影
    public static final String CCTV7 = "cctv7";//CCTV-7 军事农业
    public static final String CCTV8 = "cctv8";//CCTV-8 电视剧
    public static final String CCTV9 = "cctvjilu";//CCTV-9 纪录
    public static final String CCTV10 = "cctv10";//CCTV-10 科教
    public static final String CCTV11 = "cctv11";//CCTV-11 戏曲
    public static final String CCTV12 = "cctv12";//CCTV-12 社会与法
    public static final String CCTV13= "cctv13";//CCTV-13 新闻
    public static final String CCTV14 = "cctvchild";//CCTV-14 少儿
    public static final String CCTV15 = "cctv15";//CCTV-15 音乐

    //卫视频道
    public static final String hunan = "hunan";//湖南卫视

    public static String WhichChannel(final String source){
        String lowerSource = source.toLowerCase();
        //央视
        if (lowerSource.contains("cctv")){
            if (lowerSource.contains("cctv1"))
                return CCTV1;
            else if (lowerSource.contains("cctv2"))
                return CCTV2;
            else if (lowerSource.contains("cctv3"))
                return CCTV3;
            else if (lowerSource.contains("cctv4"))
                return CCTV4;
            else if (lowerSource.contains("cctv5"))
                return CCTV5;
            else if (lowerSource.contains("cctv6"))
                return CCTV6;
            else if (lowerSource.contains("cctv7"))
                return CCTV7;
            else if (lowerSource.contains("cctv8"))
                return CCTV8;
            else if (lowerSource.contains("cctv9"))
                return CCTV9;
            else if (lowerSource.contains("cctv10"))
                return CCTV10;
            else if (lowerSource.contains("cctv11"))
                return CCTV11;
            else if (lowerSource.contains("cctv12"))
                return CCTV12;
            else if (lowerSource.contains("cctv13"))
                return CCTV13;
            else if (lowerSource.contains("cctv14"))
                return CCTV14;
            else if (lowerSource.contains("cctv15"))
                return CCTV15;
        }else {//卫视查询
            return hunan;
        }
        return null;
    }
}
