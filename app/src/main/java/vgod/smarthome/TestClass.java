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
        arrays.add(new TVChannelEntity(1,"CCTV-1 综合", "cctv1"));
        arrays.add(new TVChannelEntity(2,"CCTV-2 财经", "cctv2"));
        arrays.add(new TVChannelEntity(3,"CCTV-3 综艺", "cctv3"));
        arrays.add(new TVChannelEntity(5,"CCTV-4 (亚洲)", "cctv4"));
        arrays.add(new TVChannelEntity(6,"CCTV-4 (欧洲)", "cctveurope"));
        arrays.add(new TVChannelEntity(7,"CCTV-4 (美洲)", "cctvamerica"));
        arrays.add(new TVChannelEntity(8,"CCTV-5 (体育)", "cctv5"));
        arrays.add(new TVChannelEntity(9,"CCTV-6 (电影)", "cctv6"));
        arrays.add(new TVChannelEntity(10,"CCTV-7 (农业)", "cctv7"));
        for (TVChannelEntity entity : arrays) {
            TVChannelEntity.insert(entity);
        }
    }
}
