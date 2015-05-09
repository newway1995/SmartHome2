package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-09
 * Time: 14:17
 * 时间处理
 */
public class TimeUtils {
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取过去三十天的起始时间
     * @return
     *          HashMap {startTime , endTime}
     */
    public static HashMap<String, Long> getCurrentMonthTime() {
        HashMap<String, Long> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date date = calendar.getTime();
        map.put("startTime = ", date.getTime() / 1000);
        map.put("endTime", date.getTime() / 1000 + 30 * 24 * 60 * 60);
        return map;
    }

    /**
     * 获取过去三十天每一天的日期
     * @return
     *          ArrayList
     */
    public static ArrayList<String> getCurrentMonthDay() {
        ArrayList<String> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        for (int i = 0; i < 30; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            data.add((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        }
        return data;
    }

    /**
     * 获取从今天开始计数的前面十二个月的名字
     * @return
     *          比如今天是5月{5,4,3,2,1,12,11,10,9,8,7,6}
     */
    public static ArrayList<String> getLastMonthName() {
        ArrayList<String> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        for (int i = 0; i < 12; i++) {
            if (i != 0) {
                calendar.add(Calendar.MONTH, -1);
            }
            data.add((calendar.get(Calendar.MONTH) + 1) + "月");
        }
        return data;
    }

    /**
     * 获取过去十二个月的起始时间和截止时间
     * @param i 过去几个月 i > -1 && i <12
     * @return HashMap
     */
    public static HashMap<String, Long> getLastMonthTime(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalArgumentException("i cannot less than 0 and more than 11");
        }
        HashMap<String, Long> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 0, 0, 0, 0);
        calendar.add(Calendar.MONTH, -i);
        Date date = calendar.getTime();
        map.put("startTime", date.getTime() / 1000);
        if (i != 0) {
            map.put("endTime",date.getTime() / 1000 + 30 * 24 * 60 * 60);
        } else {
            map.put("endTime", getCurrentTime() / 1000);
        }
        return map;
    }

}
