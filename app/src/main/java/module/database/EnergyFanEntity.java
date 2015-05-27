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
    /** 档位 */
    private int state;

    /**
     * 反射机制需要
     */
    public EnergyFanEntity() {
    }

    /**
     * 构造函数
     * @param startTime
     *          开始时间
     * @param duration
     *          结束时间
     * @param state
     *          状态
     */
    public EnergyFanEntity(long startTime, long duration, int state) {
        this.startTime = startTime;
        this.duration = duration;
        this.state = state;
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

    /**
     * 查询某一个时间区间的数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param state 电风扇的档位
     */
    public static List<EnergyFanEntity> findByStartTime(long startTime, long endTime, int state) {
        return kjdb.findAllByWhere(EnergyFanEntity.class, "startTime >= " + startTime + " and startTime <= " + endTime + " and state = " + state);
    }

    public static List<EnergyFanEntity> findAll() {
        return kjdb.findAll(EnergyFanEntity.class);
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
