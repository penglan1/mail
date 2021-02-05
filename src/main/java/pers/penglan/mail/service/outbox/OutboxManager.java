package pers.penglan.mail.service.outbox;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.penglan.mail.model.*;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FolderHelper;
import pers.penglan.mail.service.conmmon.MailReceiverImpl;
import pers.penglan.mail.service.conmmon.MessageAnalyst;
import pers.penglan.mail.service.conmmon.itf.MailReceiver;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.id.UIDCenter;
import pers.penglan.mail.utils.message.MessageUtility;

import javax.mail.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 发件管理中心
 *
 * <p>OutboxManager负责有关发件的所有操作</p>
 *
 * @Author PENGL
 * 2020-03-17
 */
@Service
public class OutboxManager {

    @Autowired
    private Logger logger;

    @Autowired
    @Qualifier("appendMessageList")
    private Properties appendMessageList;

    /**
     * 动作：发邮件
     *
     * @param messageSource 待发送邮件的内容资源
     * @return true: 发送成功； false: 发送失败
     */
    public void sendMail(MessageSource messageSource, MailAccountCenter mailAccountCenter) throws UnsupportedEncodingException, MessagingException {
        /*得到session和ransport对象*/
        Session session = mailAccountCenter.getSession();

        Message message = MessageUtility.createMessage(session, messageSource);
        //java.util.NoSuchElementException: Message does not belong to this folder
        //String mesUID = MessageUtility.getMsgUID(message, getInboxFolderWithOpen());
        //message.setFlag(Flags.Flag.DRAFT, true);

        mailAccountCenter.getTransportWithConnected().sendMessage(message, message.getAllRecipients());

        /*检查是否还需要单独保存到已发送文件夹*/
        String host = (String) mailAccountCenter.getProperties().get("mail.smtp.host");
        if (appendMessageList.containsKey(host)) {
            Folder folder = FolderHelper.getSentMessagesFolderWithOpen(mailAccountCenter);
            if (folder instanceof IMAPFolder) {
                ((IMAPFolder) folder).appendMessages(new Message[]{message});
            } else if (folder instanceof POP3Folder) {
                ((POP3Folder) folder).appendMessages(new Message[]{message});
            }

            folder.close(true);
        }



    }

    /**
     * 动作：发件箱
     *
     * <p>发件箱中保存的是发送异常的邮件</p>
     *
     * <p>使用邮件发送接口时，如果邮件发送失败，调用者应当将该邮件保存到<strong>发件箱<</strong>/p>
     *
     * <strong>为了缓存一致，不提倡使用该方法。建议在XXXXManagerWrap中操作数据库</strong>
     *
     * @return
     */
    public List<SentMailBaseInfoVO> getSentFailureMessages() {
        //////////////////////////////
        //从数据库中查询，然后将值返回
        /////////////////////////////
        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = null;

        return sentMailBaseInfoVOList;
    }

    /**
     * 动作：已发送
     *
     * <p>已发送，会将所有成功发送的邮件返送回来</p>
     *
     * <p>使用邮件发送接口时，如果邮件发送成功，调用者应当将该邮件保存到<strong>已发送</strong></p>
     *
     * @return
     */
    public Map<String, Object> getSentMessages(Set<String> msgUIDsExcluded, MailAccountCenter mailAccountCenter) throws MessagingException {
        ///////////////////////////
        // 从服务器中查询
        ///////////////////////////
        /*获取邮件文件夹*/
        Folder folder = FolderHelper.getSentMessagesFolderWithOpen(mailAccountCenter);
        /*调用MailReceiver接收新邮件*/
        MailReceiver receiver = new MailReceiverImpl(folder, msgUIDsExcluded);
        List<MessageTO> messageTOList = receiver.getMessages();

        ////////////////////////////////////////////////////
        // 对Message进行分析，解析内容，保存附件，并将相应的信息封装
        // 到对象中
        ////////////////////////////////////////////////////
        List<MailEntityVO> mailEntityVOList = new ArrayList<MailEntityVO>(messageTOList.size());
        List<AttachmentVO> attachmentVOList = new ArrayList<AttachmentVO>(messageTOList.size());

        logger.debug(">>开始逐个分析MessageTO<<");

        for (MessageTO messageTO : messageTOList) {
            try {
                Message message = messageTO.getMessage();
                String msgUID = messageTO.getMsgUID();

                logger.debug("msgUID:" + msgUID);

                /*获得分析结果*/
                MessageAnalyst.AnalysisResult analysisResult = MessageAnalyst.analyze(message);
                MailEntityVO mailEntityVO = analysisResult.getMailEntityVO();
                List<AttachmentVO> attachmentVOs = analysisResult.getAttachmentVOs();

                logger.debug("附件数量：" + attachmentVOs.size());

                /*将msgUID属性保存到MailEntityVO中*/
                mailEntityVO.setMsgUID(msgUID);
                /*将账户信息保存到MailEntityVO中*/
                mailEntityVO.setOwner(mailAccountCenter.getAccount());

                logger.debug("Owner:" + mailEntityVO.getOwner());

                /*收集，等待后续的持久化*/
                mailEntityVOList.add(mailEntityVO);
                attachmentVOList.addAll(attachmentVOs);
            } catch (MessagingException e) {
                logger.error(ExceptionUtility.getTrace(e));
            } catch (IOException e) {
                logger.error(ExceptionUtility.getTrace(e));
            }
        }

        logger.debug(">>分析MessageTO结束<<");

        ////////////////////////////////////////////
        // 封装邮件基本信息
        ///////////////////////////////////////////
        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = new ArrayList<>(mailEntityVOList.size());
        for (MailEntityVO mailEntityVO : mailEntityVOList) {
            SentMailBaseInfoVO sentMailBaseInfoVO = new SentMailBaseInfoVO();
            sentMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            sentMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
            sentMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
            sentMailBaseInfoVO.setTo(mailEntityVO.getTo());
            sentMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
            sentMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
            sentMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
            sentMailBaseInfoVO.setStatus("success");
            sentMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());

            sentMailBaseInfoVOList.add(sentMailBaseInfoVO);
        }

        logger.debug("sentMailBaseInfo数量：" + sentMailBaseInfoVOList.size());

        /*关闭folder*/
        folder.close(true);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sentMailBaseInfoVOList", sentMailBaseInfoVOList);
        resultMap.put("mailEntityVOList", mailEntityVOList);
        resultMap.put("attachmentVOList", attachmentVOList);

        return resultMap;
    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param msgUIDsToDelete
     * @throws MessagingException
     */
    public void setDeleteFlag(Set<String> msgUIDsToDelete, MailAccountCenter mailAccountCenter) throws MessagingException {
        Folder folder = FolderHelper.getSentMessagesFolderWithOpen(mailAccountCenter);
        MailReceiver mailReceiver = new MailReceiverImpl(folder, null);
        List<MessageTO> messageTOList = mailReceiver.getMessages();

        int len = msgUIDsToDelete == null ? 0 : msgUIDsToDelete.size();
        List<Message> messagesToDelete = new ArrayList<>(len);
        int i = 0;
        for (MessageTO messageTO : messageTOList) {
            if (msgUIDsToDelete.contains(messageTO.getMsgUID())) {
                messagesToDelete.add(messageTO.getMessage());
            }
        }
        /*判断是否有待删除的邮件需要转移到已删除文件中*/
        if (messagesToDelete.size() > 0) {
            Folder deletedFolder = FolderHelper.getDeletedMessagesFolderWithOpen(mailAccountCenter);
            folder.copyMessages(messagesToDelete.toArray(new Message[0]), deletedFolder);
            deletedFolder.close(true);
        }

        for (Message message : messagesToDelete) {
            try {
                message.setFlag(Flags.Flag.DELETED, true);
            } catch (MessagingException e) {
                logger.error(ExceptionUtility.getTrace(e));
            }
        }

        folder.close(true);
    }


    ////////////////////////////////////////////
    // 私有方法
    ////////////////////////////////////////////

}
