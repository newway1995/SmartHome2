package module.database;


import org.kymjs.aframe.database.annotate.Id;

/**
 * Package: module.database
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-28
 * Time: 23:15
 * 树莓派实体
 */
public class RaspberryEntity {
    @Id()
    private String raspid;
    private String nickname;
    private String passwrod;
    private String function;

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }

    public String getRaspid() {
        return raspid;
    }

    public void setRaspid(String raspid) {
        this.raspid = raspid;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 构造函数
     * @param raspid
     * @param nickname
     */
    public RaspberryEntity(String raspid, String password, String nickname, String function) {
        setPasswrod(password);
        this.function = function;
        this.raspid = raspid;
        this.nickname = nickname;
    }

    public RaspberryEntity(){}


}
