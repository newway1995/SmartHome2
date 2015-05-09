package module.database;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.database.annotate.Id;

import java.util.List;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-05-09
 * Time: 14:17
 * 电风扇能耗
 */
public class EnergyFanEntity {
    public static KJDB kjdb;

    @Id()
    private int id;
    /** 开始时间 */
    private long startTime;
    /** 持续时间 */
    private long duration;

    public EnergyFanEntity() {
    }

    public EnergyFanEntity(long startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public static void insert(EnergyFanEntity entity) {
        kjdb.save(entity);
    }

    /**
     * 查询某一个时间区间的数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public static List<EnergyFanEntity> findByStartTime(long startTime, long endTime) {
        return kjdb.findAllByWhere(EnergyFanEntity.class, "startTime > " + startTime + " and startTime < " + endTime);
    }

    public static List<EnergyFanEntity> findAll() {
        return kjdb.findAll(EnergyFanEntity.class);
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
