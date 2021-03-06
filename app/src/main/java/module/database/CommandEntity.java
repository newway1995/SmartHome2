package module.database;

import android.util.Log;

import org.kymjs.aframe.database.KJDB;
import org.kymjs.aframe.database.annotate.Id;

import java.util.List;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-15
 * Time: 18:40
 * FIXME
 */
public class CommandEntity {
    public static KJDB kjdb;

    @Id()
    private int id;
    /** 指令 **/
    private String command;
    /** 当前时间 **/
    private long currentTime;
    /** 定时时间 **/
    private long timer;
    /** 是否被执行 **/
    private boolean isExecuted;

    public CommandEntity() {
    }

    /**
     * 构造函数
     * @param command
     * @param currentTime
     * @param timer
     */
    public CommandEntity(String command, long currentTime, long timer, boolean isExecuted) {
        this.command = command;
        this.currentTime = currentTime;
        this.timer = timer;
        this.isExecuted = isExecuted;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setIsExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }

    /**
     * 插入数据
     * @param entity ChatMsgEntity
     */
    public static void insert(CommandEntity entity){
        kjdb.save(entity);
    }

    /**
     * 删除 by id
     * @param id
     */
    public static void delete(int id){
        kjdb.deleteById(CommandEntity.class, id);
    }

    /**
     * 删除All
     */
    public static void deleteAll(){
        kjdb.delete(CommandEntity.class);
    }


    /**
     * 更新
     * @param entity
     */
    public static void update(CommandEntity entity){
        kjdb.update(entity);
    }

    /**
     * 查询返回一条数据
     * @param id
     * @return
     */
    public static CommandEntity query(int id){
        return kjdb.findById(id, CommandEntity.class);
    }


    /**
     * 查询所有的数据
     * @return
     */
    public static List<CommandEntity> queryAll(){
        return kjdb.findAll(CommandEntity.class);
    }

    /**
     * 查询没有执行的command
     * @return
     */
    public static List<CommandEntity> queryAllByNotExecuted(){
        List<CommandEntity> commandEntityList = kjdb.findAllByWhere(CommandEntity.class, "isExecuted=0");
        for (CommandEntity entity : commandEntityList){
            Log.d("ThreadInfo", "2.queryAllByNotExecuted:" + entity.toString());
        }
        return commandEntityList;
    }

    public String toString(){
        return "command = " + command + ",timer = " + timer + ", isExecuted = " + isExecuted;
    }

}
