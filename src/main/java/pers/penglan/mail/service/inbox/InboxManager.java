package pers.penglan.mail.service.inbox;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.InboxMailBaseInfoVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageTO;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FolderHelper;
import pers.penglan.mail.service.conmmon.MailReceiverImpl;
import pers.penglan.mail.service.conmmon.MessageAnalyst;
import pers.penglan.mail.service.conmmon.itf.MailReceiver;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.flags.MailFlag;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

/**
 * 收件箱管理中心
 *
 * <strong>InboxMaster管理收件箱的所有操作，功能等</strong>
 *
 * <p>每个邮箱账户单独拥有一个InboxMaster，在用户使用期间，应该保存每一个被创建的实例，
 * 以免多次不必要的创建</p>
 *
 * @Author PENGL
 * 2020-03-14
 */
@Service
public class InboxManager {

    @Autowired
    private Logger logger;

    /**
     * 动作：收新邮件
     *
     * @return 新邮件的基本信息，邮件实体，附件列表，形式如下：
     *         <pre>
     *             resultMap.put("newMailBaseInfoVOList", newMailBaseInfoVOList);
     *             resultMap.put("mailEntityVOList", mailEntityVOList);
     *             resultMap.put("attachmentVOList", attachmentVOList);
     *         </pre>
     */
    public Map<String, Object> receiveMail(Set<String> msgUIDExcluded, MailAccountCenter mailAccountCenter) throws MessagingException {
        /*获取邮件文件夹*/
        Folder folder = FolderHelper.getInboxFolderWithOpen(mailAccountCenter);
        /*调用MailReceiver接收新邮件*/
        MailReceiver receiver = new MailReceiverImpl(folder, msgUIDExcluded);
        List<MessageTO> messageTOList = receiver.getMessages();

        //////////////////////////////////////////////////////
        //对Message进行分析，解析内容，保存附件，并将相应的信息封装
        //到对象中
        /////////////////////////////////////////////////////
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

                /*设置邮件conentType*/
                mailEntityVO.setContentType(MailFlag.TEXT_HTML_UTF8);

                /*收集*/
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
        List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = new ArrayList<InboxMailBaseInfoVO>(mailEntityVOList.size());
        for (MailEntityVO mailEntityVO : mailEntityVOList) {
            InboxMailBaseInfoVO inboxMailBaseInfoVO = new InboxMailBaseInfoVO();
            inboxMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            inboxMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
            inboxMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
            inboxMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
            inboxMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
            inboxMailBaseInfoVO.setReceivedDate(mailEntityVO.getReceivedDate());
            inboxMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
            /*新邮件，设为未读*/
            inboxMailBaseInfoVO.setReadFlag(MailFlag.UNREAD);
            inboxMailBaseInfoVO.setFlag(MailFlag.ORDINARY);
            inboxMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());

            inboxMailBaseInfoVOList.add(inboxMailBaseInfoVO);
        }

        logger.debug("mailBaseInfo数量：" + inboxMailBaseInfoVOList.size());

        /*关闭folder*/
        folder.close(true);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("inboxMailBaseInfoVOList", inboxMailBaseInfoVOList);
        resultMap.put("mailEntityVOList", mailEntityVOList);
        resultMap.put("attachmentVOList", attachmentVOList);

        return resultMap;
    }


    /**
     * 动作：收件箱
     *
     * 为了减少不必要的流量输出，已将发送给了前台的邮件可以通过邮件的UID来被过滤掉
     *
     * @return
     */
    public List<InboxMailBaseInfoVO> getInbox(MailAccountCenter mailAccountCenter) {
        ///////////////////////////
        // 从数据库中查询，然后将值返回
        //////////////////////////
        List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = null;
        return inboxMailBaseInfoVOList;
    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param msgUIDsToDelete
     * @throws MessagingException
     */
    public void setDeleteFlag(Set<String> msgUIDsToDelete, MailAccountCenter mailAccountCenter) throws MessagingException {
        Folder folder = FolderHelper.getInboxFolderWithOpen(mailAccountCenter);
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
