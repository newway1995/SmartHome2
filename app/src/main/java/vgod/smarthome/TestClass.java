package vgod.smarthome;

import java.util.ArrayList;
import java.util.List;

import module.database.TVChannelEntity;
import utils.L;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-08
 * Time: 00:28
 * FIXME
 */
public class TestClass {

    public static void test() {
        testInsert();
        testQueryAll();
        testQuery();
    }

    public static void testQueryAll() {
        List<TVChannelEntity> tvChannelEntities = TVChannelEntity.queryAll();
        for (TVChannelEntity entity : tvChannelEntities)
            L.d("SQLITE", "[testQueryAll] : "+entity.toString());
    }

    public static void testQuery() {
        TVChannelEntity tvChannelEntity = TVChannelEntity.query("cctv1");
        L.d("SQLITE", "[testQuery] : " + tvChannelEntity.toString());
    }

    /**
     * 插入数据
     */
    public static void testInsert() {
        List<TVChannelEntity> arrays = new ArrayList<>();
        arrays.add(new TVChannelEntity(1,"湖南卫视", "hunan"));
        arrays.add(new TVChannelEntity(2,"湖北卫视", "hubei"));
        arrays.add(new TVChannelEntity(3,"北京卫视", "btv1"));
        arrays.add(new TVChannelEntity(4,"吉林卫视", "jilin"));
        arrays.add(new TVChannelEntity(5,"中央一台", "cctv1"));
        for (TVChannelEntity entity : arrays) {
            TVChannelEntity.insert(entity);
        }
    }
}
