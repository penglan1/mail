package pers.penglan.mail.model.user;

import java.io.Serializable;

/**
 * 登入用户的信息
 *
 * @Author PENGL
 * 2020-03-27
 */
public class User implements Serializable {
    private static final long serialVersionUID = -4785754147113295763L;
    /*用户ID, 即用户登入账号*/
    private String pk_UserID;
    /*密码*/
    private String password;
    /*用户昵称*/
    private String nickname;
    /*用户账号注册时间*/
    private String registeredTime;
    /*绑定的邮箱账号*/
    private String bandingMailAccount;

    public String getPk_UserID() {
        return pk_UserID;
    }

    public void setPk_UserID(String pk_UserID) {
        this.pk_UserID = pk_UserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    public String getBandingMailAccount() {
        return bandingMailAccount;
    }

    public void setBandingMailAccount(String bandingMailAccount) {
        this.bandingMailAccount = bandingMailAccount;
    }
}
