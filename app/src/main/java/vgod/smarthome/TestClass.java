package vgod.smarthome;

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
    private TVChannelEntity entity;

    public TestClass() {
        testInsert();
        testQuery();
    }

    /**
     * 测试数据库
     */
    private void testInsert(){
        for (int i = 0; i < 100; i++){
            entity = new TVChannelEntity(i,"湖南卫视: " + i);
            TVChannelEntity.insert(entity);
        }
    }

    private void testQuery(){
        List<TVChannelEntity> tvChannelEntities = TVChannelEntity.queryAll();
        L.d("SQLITE", "read from db = " + tvChannelEntities.toString());
    }
}
