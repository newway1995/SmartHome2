package module.database;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.database.annotate.Id;

import java.util.List;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-08
 * Time: 00:11
 * 电视节目的保存表
 */
public class TVChannelEntity {
    @Id()
    int number;
    String channelText;

    public static KJDB kjdb;

    public TVChannelEntity() {}

    /**
     * 构造函数
     * @param number
     * @param channelText
     */
    public TVChannelEntity(int number, String channelText) {
        this.number = number;
        this.channelText = channelText;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getChannelText() {
        return channelText;
    }

    public void setChannelText(String channelText) {
        this.channelText = channelText;
    }

    /**
     * 插入数据
     * @param entity TVChannelEntity
     */
    public static void insert(TVChannelEntity entity){
        kjdb.save(entity);
    }

    /**
     * 删除 by number
     * @param number
     */
    public static void delete(int number){
        kjdb.deleteById(TVChannelEntity.class, number);
    }

    /**
     * 删除All
     */
    public static void deleteAll(){
        kjdb.delete(TVChannelEntity.class);
    }

    /**
     * 删除 by 文字
     * @param channelText
     */
    public static void delete(String channelText){
        kjdb.deleteByWhere(TVChannelEntity.class, "channelText=" + channelText);
    }

    /**
     * 更新
     * @param entity
     */
    public static void update(TVChannelEntity entity){
        kjdb.update(entity);
    }

    /**
     * 查询返回一条数据
     * @param number
     * @return
     */
    public static TVChannelEntity query(int number){
        return kjdb.findById(number, TVChannelEntity.class);
    }


    /**
     * 查询所有的数据
     * @return
     */
    public static List<TVChannelEntity> queryAll(){
        return kjdb.findAll(TVChannelEntity.class);
    }
}
