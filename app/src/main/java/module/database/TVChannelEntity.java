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
    int id;
    int number;
    String channelText;
    String channelRel;

    public static KJDB kjdb;

    public TVChannelEntity() {}

    /**
     * 构造函数
     * @param number int
     * @param channelText String
     * @param channelRel String
     * @return Entity
     */
    public TVChannelEntity(int number, String channelText, String channelRel) {
        this.number = number;
        this.channelText = channelText;
        this.channelRel = channelRel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getChannelRel() {
        return channelRel;
    }

    public void setChannelRel(String channelRel) {
        this.channelRel = channelRel;
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
     * @param number int
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
     * @param channelText String
     */
    public static void delete(String channelText){
        kjdb.deleteByWhere(TVChannelEntity.class, "channelText=" + "'" + channelText + "'");
    }

    /**
     * 更新
     * @param entity
     *          TVChannelEntity
     */
    public static void update(TVChannelEntity entity){
        if (query(entity.getNumber()) != null)
            kjdb.update(entity);
        else
            insert(entity);
    }

    /**
     * 查询返回一条数据
     * @param number int
     * @return
     */
    public static TVChannelEntity query(int number){
        List<TVChannelEntity> entitys = kjdb.findAllByWhere(TVChannelEntity.class, "number=" + number + " ORDER BY id DESC");
        if (entitys != null && entitys.size() > 0)
            return entitys.get(0);
        return null;
    }

    /**
     * 查询返回一条数据
     * @param channelRel String
     * @return
     */
    public static TVChannelEntity query(String channelRel) {
        return kjdb.findAllByWhere(TVChannelEntity.class, "channelRel='" + channelRel + "'ORDER BY id DESC").get(0);
    }

    /**
     * 根据channelText获取number <湖南卫视 =》 13>
     * @param channelText    节目
     * @return 频道 int
     */
    public static int getNumberByChannelText(String channelText) {
        List<TVChannelEntity> entitys = kjdb.findAllByWhere(TVChannelEntity.class, "channelText='" + channelText + "' ORDER BY id DESC");
        if (entitys != null && entitys.size() != 0) {
            return entitys.get(0).getNumber();
        }
        return -1;
    }

    /**
     * 查询所有的数据
     * @return List<TVChannelEntity>
     */
    public static List<TVChannelEntity> queryAll(){
        return kjdb.findAll(TVChannelEntity.class);
    }

    public String toString() {
        return "id = " + id + ", number = " + number + ", channel = " + channelText + ", rel = " + channelRel;
    }
}
