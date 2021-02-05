package pers.penglan.mail.model.user.mail;

import pers.penglan.mail.model.user.User;

/**
 * 用于用户注册时的数据传递
 *
 * @Author PENGL
 * 2020-04-11
 */
public class RegisterTO {
    /*用户信息*/
    private User user;
    /*邮箱验证码*/
    private String verification;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
