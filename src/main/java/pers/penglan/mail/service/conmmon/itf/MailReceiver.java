package pers.penglan.mail.service.conmmon.itf;

import pers.penglan.mail.model.MessageTO;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 邮件接收的统一操作接口
 *
 * <p>所有的接收邮件操作必须实现此接口</p>
 *
 * @Author PENGL
 * 2020-03-14
 */
public interface MailReceiver {
    /**
     * 接收邮件
     *
     * @return
     */
    public List<MessageTO> getMessages() throws MessagingException;

}
