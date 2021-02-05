package pers.penglan.mail.model.user.mail;

import java.io.Serializable;

/**
 * @Author PENGL
 * 2020-03-27
 */
public class MailAccount implements Serializable {
    private static final long serialVersionUID = -8414845002122431083L;
    private String pk_MailAccount;
    private String pk_UserID;
    private String account;
    private String password;

    public String getPk_MailAccount() {
        return pk_MailAccount;
    }

    public void setPk_MailAccount(String pk_MailAccount) {
        this.pk_MailAccount = pk_MailAccount;
    }

    public String getPk_UserID() {
        return pk_UserID;
    }

    public void setPk_UserID(String pk_UserID) {
        this.pk_UserID = pk_UserID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
