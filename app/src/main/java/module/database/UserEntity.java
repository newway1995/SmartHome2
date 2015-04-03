package module.database;

import org.kymjs.aframe.database.annotate.Id;

/**
 * Package: module.database
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-28
 * Time: 23:27
 * 保存用户信息
 */
public class UserEntity {
    @Id()
    private String username;
    private String password;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 构造函数
     * @param username
     * @param password
     * @param nickname
     */
    public UserEntity(String username, String password, String nickname) {

        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public UserEntity(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
