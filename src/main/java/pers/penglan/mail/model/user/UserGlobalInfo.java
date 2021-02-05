package pers.penglan.mail.model.user;

import pers.penglan.mail.model.user.mail.MailAccountCenter;

import java.util.Map;

/**
 * 用户全局信息
 *
 * @Author PENGL
 * 2020-03-26
 */
public class UserGlobalInfo {
    private Map<String, MailAccountCenter> mailAccountMap;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    private MailAccountCenter currentMailAccountCenter;

    public synchronized Map<String, MailAccountCenter> getAccountMap() {
        return mailAccountMap;
    }

    public synchronized void setAccountMap(Map<String, MailAccountCenter> mailAccountMap) {
        this.mailAccountMap = mailAccountMap;
    }

    public synchronized String getUserID() {
        return user.getPk_UserID();
    }

    public synchronized String getUserPassword() {
        return user.getPassword();
    }

    /**
     * 检验该账号是否存在
     * @param mailAccount
     * @return true: 账号存在，合法；false: 账号不存在，非法
     */
    public synchronized boolean checkAccount(String mailAccount) {
        boolean result = false;
        if (mailAccountMap.get(mailAccount) != null ) {
            result = true;
        }

        return result;
    }

    public synchronized MailAccountCenter getMailAccountInfo(String mailAccount) {
        return mailAccountMap.get(mailAccount);
    }

    /**
     * 设置当前正在使用的账号
     * @param mailAccount
     * @return
     */
    public synchronized boolean setCurrentMailAccount(String mailAccount) {
        boolean flag = false;
        flag = checkAccount(mailAccount);
        if (flag) {
            currentMailAccountCenter = mailAccountMap.get(mailAccount);
        }

        return flag;
    }

    /**
     * 获取当前正在使用的账号
     * @return
     */
    public synchronized MailAccountCenter getCurrentMailAccountCenter() {
        return currentMailAccountCenter;
    }


}
