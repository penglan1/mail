package pers.penglan.mail.model.user.mail;

/**
 * 此TO用于客户端添加邮箱账号，或者修改邮箱设置时的数据传输
 *
 * @Author PENGL
 * 2020-03-27
 */
public class ClientMailAccountTO {
    private MailAccount mailAccount;
    private AccountProperty[] accountProperties;

    public MailAccount getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(MailAccount mailAccount) {
        this.mailAccount = mailAccount;
    }

    public AccountProperty[] getAccountProperties() {
        return accountProperties;
    }

    public void setAccountProperties(AccountProperty[] accountProperties) {
        this.accountProperties = accountProperties;
    }
}
