package pers.penglan.mail.service.config;

import java.util.Properties;

/**
 * @Author PENGL
 * 2020-03-21
 */
public class AccountChoose {
    public static Properties getQQProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", "imap.qq.com");
        properties.setProperty("mail.imap.port", "993");  // mail.pop3.port -> 995
        properties.setProperty("mail.imap.ssl.enable", "true");
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.auth", "true");

        properties.setProperty("mail.user.account", "penglanm@qq.com");
        properties.setProperty("mail.user.password", "tfrsfyhizmspdbcc");

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.qq.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");

        return properties;
    }

    public static Properties get163Properties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", "imap.163.com");
        properties.setProperty("mail.imap.port", "993");
        properties.setProperty("mail.imap.ssl.enable", "true");
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.auth", "true");

        properties.setProperty("mail.user.account", "penglanm@163.com");
        properties.setProperty("mail.user.password", "3pengpengpeng");

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.163.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");

        return properties;
    }
}
