package pers.penglan.mail.service.conmmon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.penglan.mail.model.MessageTO;
import pers.penglan.mail.service.conmmon.itf.MailReceiver;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.message.MessageUtility;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 邮件接收
 *
 * @Author PENGL
 * 2020-03-14
 */
public class MailReceiverImpl implements MailReceiver {
    private static Logger logger = LogManager.getLogger("mail");

    /**
     * 收件箱文件夹对象，由其使用者提供
     */
    private Folder folder;

    /**
     * 需要被过滤的message的Uid，由其使用者提供
     * 如果不需要过滤，设置成空集或者null即可
     */
    private Set<String> msgUIDExcluded;

    //////////////////////////////////////////////////
    //唯一的构造方法
    //////////////////////////////////////////////////

    /**
     *
     * @param folder message文件夹，将会从此文件夹中收取邮件消息
     * @param msgUIDExcluded 需要被过滤的邮件消息的Uid，如果不需要过滤，可以传送为空集或者是null
     */
    public MailReceiverImpl(Folder folder, Set<String> msgUIDExcluded) {
        this.folder = folder;
        this.msgUIDExcluded = msgUIDExcluded;
    }

    public List<MessageTO> getMessages() throws MessagingException {
        Message[] messages = folder.getMessages();

        List<MessageTO> list = new ArrayList<MessageTO>(messages.length);

        for (int i = 0; i < messages.length; i++) {
            try {
                String UID = MessageUtility.getMsgUID(messages[i], folder);
                if (msgUIDExcluded != null && !msgUIDExcluded.contains(UID))
                    list.add(new MessageTO(messages[i], UID));
                else if (msgUIDExcluded == null)
                    list.add(new MessageTO(messages[i], UID));
            } catch (MessagingException e) {
                logger.error(ExceptionUtility.getTrace(e));
            }
        }

        return list;
    }
}
