package module.database;

import org.kymjs.aframe.database.KJDB;

import java.util.List;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-10
 * Time: 15:02
 * 电视机能耗数据存储
 */
public class EnergyTVEntity {
    public static KJDB kjdb;

    private int id;
    /** 开始时间 */
    private long startTime;
    /** 持续时间 */
    private long duration;

    public EnergyTVEntity() {
    }

    public EnergyTVEntity(int id, long startTime, long duration) {
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public static void insert(EnergyTVEntity entity) {
        kjdb.save(entity);
    }

    /**
     * 查询某一个时间区间的数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static List<EnergyTVEntity> findByStartTime(long startTime, long endTime) {
        return kjdb.findAllByWhere(EnergyTVEntity.class, "startTime >= " + startTime + " and startTime <= " + endTime);
    }

    public static List<EnergyTVEntity> findAll() {
        return kjdb.findAll(EnergyTVEntity.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
