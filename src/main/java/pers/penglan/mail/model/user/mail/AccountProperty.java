package pers.penglan.mail.model.user.mail;

import java.io.Serializable;

/**
 * @Author PENGL
 * 2020-03-27
 */
public class AccountProperty implements Serializable {
    private static final long serialVersionUID = -6743503959790355186L;
    private String pk_AccountProperty;
    private String pk_MailAccount;
    private String protocol;
    private String host;
    private String port;
    private String auth;
    private String isSSL;

    public String getPk_AccountProperty() {
        return pk_AccountProperty;
    }

    public void setPk_AccountProperty(String pk_AccountProperty) {
        this.pk_AccountProperty = pk_AccountProperty;
    }

    public String getPk_MailAccount() {
        return pk_MailAccount;
    }

    public void setPk_MailAccount(String pk_MailAccount) {
        this.pk_MailAccount = pk_MailAccount;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getIsSSL() {
        return isSSL;
    }

    public void setIsSSL(String isSSL) {
        this.isSSL = isSSL;
    }
}
