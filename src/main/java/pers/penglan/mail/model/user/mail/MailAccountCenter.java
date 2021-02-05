package pers.penglan.mail.model.user.mail;

import javax.mail.*;
import java.util.Properties;

/**
 * 用户的邮箱账号信息
 *
 * properties中必须包含以下信息:
 * <pre>
 *     //发件：关于transport的信息
 *     mail.transport.protocol
 *     mail.smtp.host
 *     mail.smtp.port
 *     mail.smpt.auth
 *     //mail.smtp.isssl //自定义的
 *
 *     //关于用户的账号密码信息
 *     mail.user.account //自定义的
 *     mail.user.password //自定义的
 *
 *     ///////// 以下可以二选一 //////////
 *     //收件：关于store的信息
 *     mail.store.protocol
 *     mail.pop3.host
 *     mail.pop3.port
 *     mail.pop3.auth
 *     mail.pop3.isssl //自定义的
 *     或者
 *     mail.store.protocol
 *     mail.imap.host
 *     mail.imap.port
 *     mail.imap.auth
 *     mail.imap.isssl //自定义
 *     ////////// 结束 //////////////
 *
 *     //删除策略
 *     mail.delete.service //true代表本地删除时从服务端同步删除
 *     该邮件，false代表只从本地删除
 * </pre>
 *
 * @Author PENGL
 * 2020-03-18
 */
public class MailAccountCenter {

    private Properties properties;

    private boolean isPropertiesChange = false;

    private Session session = null;

    private Store store = null;

    private Transport transport = null;

    public MailAccountCenter(Properties properties) {
        this.properties = properties;
        isPropertiesChange = true;
    }



    /**
     * 获取邮箱账号
     *
     * @return
     */
    public synchronized String getAccount() {
        return properties.getProperty("mail.user.account");
    }

    /**
     * 获得Session
     *
     * <p>此方法可以保证一个用户只是用一个Session，多次调用将返回同一个Session.
     * 同时，如果账户的配置文件变化了，将会自动的创建新的Session</p>
     *
     * @return
     */
    public synchronized Session getSession() {
        if (isPropertiesChange || session == null) {
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(properties.getProperty("mail.user.account"),
                            properties.getProperty("mail.user.password"));
                }
            };
            session = Session.getInstance(properties, authenticator);
            /*将transport和store都释放掉*/
            transport = null;
            store = null;

            isPropertiesChange = false;
        }

        return session;
    }

    /**
     * 返回已经连接好了的store
     *
     * <p>此方法可以保证一个用户只是用一个Store，多次调用将返回同一个Store.
     * 同时，如果账户的配置文件变化了，将会自动的创建新的Store</p>
     *
     * @return
     */
    public synchronized Store getStoreWithConnected() throws MessagingException {
        if (isPropertiesChange || store == null || !store.isConnected()) {
            getSession();
            store = session.getStore();
            try {
                store.connect();
            } catch (MessagingException e) {
                /*尝试再次连接*/
                store.connect();
            }

            isPropertiesChange = false;
        }

        return store;
    }

    /**
     * 返回已经连接好了的transport
     *
     * <p>此方法可以保证一个用户只是用一个transport，多次调用将返回同一个transport.
     * 同时，如果账户的配置文件变化了，将会自动的创建新的transport</p>
     *
     * @return
     */
    public synchronized Transport getTransportWithConnected() throws MessagingException {
        if (isPropertiesChange || transport == null || !transport.isConnected()) {
            getSession();
            transport = session.getTransport();
            try {
                transport.connect();
            } catch (MessagingException e) {
                /*尝试再次连接*/
                transport.connect();
            }

            isPropertiesChange = false;
        }

        return transport;
    }

    public synchronized Properties getProperties() {
        return properties;
    }

    public synchronized void setProperties(Properties properties) {
        this.properties = properties;
        isPropertiesChange = true;
    }

    /**
     * 获取标志：是否需要从服务器端同步删除邮件
     *
     * @return
     */
    public synchronized boolean getDeleteFromServiceFlag() {
        boolean deleteFromServiceFlag = Boolean.valueOf(properties.getProperty("mail.delete.service", "false"));
        return deleteFromServiceFlag;
    }

    public synchronized String getOwner() {
        String mailAccount = (String) properties.get("mail.user.account");

        return mailAccount;
    }
}
