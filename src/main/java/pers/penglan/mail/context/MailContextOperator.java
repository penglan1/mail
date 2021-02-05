package pers.penglan.mail.context;

import pers.penglan.mail.controller.processor.InfoProcessorController;
import pers.penglan.mail.mapper.user.mail.AccountPropertyMapper;
import pers.penglan.mail.mapper.user.mail.MailAccountMapper;
import pers.penglan.mail.model.user.mail.AccountProperty;
import pers.penglan.mail.model.user.mail.MailAccount;
import pers.penglan.mail.model.user.mail.MailAccountCenter;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * 邮件服务上下文
 * <strong>可以通过该类获取邮件服务上下文中的公用功能</strong>
 *
 * @Author PENGL
 * 2020-04-11
 */
public class MailContextOperator {
    /*系统邮件账号*/
    public static final String SYSTEM_MAIL_ACCOUNT_KEY = "systemMailAccountCenter";

    /**
     * 获取系统邮件账号中心
     * @param context
     * @param mailAccountMapper
     * @param accountPropertyMapper
     * @return
     */
    public static MailAccountCenter getSystemMailAccountCenter(ServletContext context,
                                                               MailAccountMapper mailAccountMapper,
                                                               AccountPropertyMapper accountPropertyMapper) {
        if (context.getAttribute(SYSTEM_MAIL_ACCOUNT_KEY) != null)
            return (MailAccountCenter) context.getAttribute(SYSTEM_MAIL_ACCOUNT_KEY);
        /*开始创建系统邮箱*/
        MailAccount mailAccount = mailAccountMapper.selectByUserIDAndAccount("penglan_mail_system",  "PL_Mail_System@126.com");
        List<AccountProperty> accountPropertyList = accountPropertyMapper.selectByPKMailAccount(mailAccount.getPk_MailAccount());

        MailAccountCenter mailAccountCenter = InfoProcessorController.createMailAccountCenter(mailAccount, accountPropertyList);
        /*放入全局上下文*/
        context.setAttribute(SYSTEM_MAIL_ACCOUNT_KEY, mailAccountCenter);

        return (MailAccountCenter) context.getAttribute(SYSTEM_MAIL_ACCOUNT_KEY);
    }
}
