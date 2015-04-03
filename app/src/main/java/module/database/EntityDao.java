package module.database;

import android.content.Context;

import org.kymjs.aframe.database.KJDB;

import java.util.List;

/**
 * Package: module.database
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-28
 * Time: 23:34
 * 封装数据库的操作
 */
public class EntityDao {
    private KJDB kjdb;
    public EntityDao(Context context){
        kjdb = KJDB.create(context);
    }

    /**
     * 保存用户
     * @param userEntity
     */
    public void saveUser(UserEntity userEntity){
        kjdb.save(userEntity);
    }

    /**
     * 保存用户
     * @param username
     * @param password
     * @param nickname
     */
    public void saveUser(String username, String password, String nickname){
        kjdb.save(new UserEntity(username, password, nickname));
    }

    /**
     * 获取当前用户
     * @param username
     * @return
     */
    public UserEntity getUser(String username){
        List<UserEntity> list = kjdb.findAllByWhere(UserEntity.class, "username=" + username);
        UserEntity userEntity = list.get(0);
        return userEntity;
    }

    /**
     * 删除用户
     * @param username
     */
    public void deleteUser(String username){
        kjdb.deleteByWhere(UserEntity.class, "username=" + username);
    }

    /**
     * 更新用户信息
     * @param userEntity
     * @param password
     * @param nickname
     */
    public void updateUser(UserEntity userEntity, String password, String nickname){
        kjdb.update(userEntity, "password=" + password + " AND nickname=" + nickname);
    }

    /**
     * 增加raspberry entity
     * @param raspberryEntity
     */
    public void saveRaspberry(RaspberryEntity raspberryEntity){
        kjdb.save(raspberryEntity);
    }

    /**
     * 增加raspberry entity
     * @param raspid
     * @param password
     * @param nickname
     * @param function
     */
    public void saveRaspberry(String raspid, String password, String nickname, String function){
        kjdb.save(new RaspberryEntity(raspid, password, nickname, function));
    }

    /**
     * 删除raspberry entity
     * @param raspid
     */
    public void deleteRaspberry(String raspid){
        kjdb.deleteByWhere(RaspberryEntity.class, "raspid="+raspid);
    }

    /**
     * 更新raspberry entity的信息
     * @param raspberryEntity
     * @param password
     * @param nickname
     */
    public void updateRaspberry(RaspberryEntity raspberryEntity, String password, String nickname){
        kjdb.update(raspberryEntity, "password="+password+" AND nickname="+nickname);
    }

    /**
     * 根据id返回RaspberryEntity
     * @param raspid
     * @return
     */
    public RaspberryEntity getRaspberry(String raspid){
        return kjdb.findById(raspid, RaspberryEntity.class);
    }

    /**
     * 返回树莓派的列表
     * @return
     */
    public List<RaspberryEntity> getRaspberry(){
        return kjdb.findAll(RaspberryEntity.class);
    }
}
