package pers.penglan.mail.service.junk;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.JunkMailBaseInfoVO;
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
 * 垃圾邮件管理中心
 *
 * @Author PENGL
 * 2020-03-20
 */
@Service
public class JunkManager {
    @Autowired
    private Logger logger;

    /**
     * 动作：获取垃圾邮件
     *
     * @return
     */
    public Map<String, Object> receiveJunk(Set<String> msgUIDsExcluded, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        ///////////////////////////
        // 从服务器中查询
        ///////////////////////////
        /*获取邮件文件夹*/
        Folder folder = FolderHelper.getJunkFolderWithOpen(mailAccountCenter);
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
        List<JunkMailBaseInfoVO> junkMailBaseInfoVOList = new ArrayList<>(mailEntityVOList.size());
        for (MailEntityVO mailEntityVO : mailEntityVOList) {
            JunkMailBaseInfoVO junkMailBaseInfoVO = new JunkMailBaseInfoVO();
            junkMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            junkMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
            junkMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
            junkMailBaseInfoVO.setTo(mailEntityVO.getTo());
            junkMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
            junkMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
            junkMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
            junkMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());
            junkMailBaseInfoVO.setFlag(MailFlag.ORDINARY);

            junkMailBaseInfoVOList.add(junkMailBaseInfoVO);
        }

        logger.debug("junkMailBaseInfo数量：" + junkMailBaseInfoVOList.size());

        /*关闭folder*/
        folder.close(true);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("junkMailBaseInfoVOList", junkMailBaseInfoVOList);
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
    public void setDeleteFlag(Set<String> msgUIDsToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        Folder folder = FolderHelper.getJunkFolderWithOpen(mailAccountCenter);
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
