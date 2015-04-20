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
        testQuery();
    }


    private void testQuery(){
        List<TVChannelEntity> tvChannelEntities = TVChannelEntity.queryAll();
        L.d("SQLITE", "read from db = " + tvChannelEntities.toString());
    }
}
