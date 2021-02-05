package pers.penglan.mail.service.delete;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.DeletedMailBaseInfoVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageTO;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FolderHelper;
import pers.penglan.mail.service.conmmon.MailReceiverImpl;
import pers.penglan.mail.service.conmmon.MessageAnalyst;
import pers.penglan.mail.service.conmmon.itf.MailReceiver;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

/**
 * 删除管理中心
 *
 * <strong>负责管理有关删除的操作，包括删除特定邮件，查看已删除邮件等</strong>
 *
 * <p>每个邮箱账户应该单独使用一个DeletingManager</p>
 *
 * @Author PENGL
 * 2020-03-19
 */
@Service
public class DeletingManager {
    @Autowired
    protected Logger logger;

    /**
     * 动作：已删除
     *
     * <p>返回的格式如下<pre>
     *     resultMap.put("deletedMailBaseInfoVOList", deletedMailBaseInfoVOList);
     *     resultMap.put("mailEntityVOList", mailEntityVOList);
     *     resultMap.put("attachmentVOList", attachmentVOList);
     * </pre></p>
     *
     * @return
     */
    public Map<String, Object> receiveDeletedMessages(Set<String> msgUIDsExcluded, MailAccountCenter mailAccountCenter) throws MessagingException {
        ///////////////////////////
        // 从服务器中查询
        ///////////////////////////
        /*获取邮件文件夹*/
        Folder folder = FolderHelper.getDeletedMessagesFolderWithOpen(mailAccountCenter);
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
        List<DeletedMailBaseInfoVO> deletedMailBaseInfoVOList = new ArrayList<>(mailEntityVOList.size());
        for (MailEntityVO mailEntityVO : mailEntityVOList) {
            DeletedMailBaseInfoVO deletedMailBaseInfoVO = new DeletedMailBaseInfoVO();
            deletedMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            deletedMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
            deletedMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
            deletedMailBaseInfoVO.setTo(mailEntityVO.getTo());
            deletedMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
            deletedMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
            deletedMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
            deletedMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());

            deletedMailBaseInfoVOList.add(deletedMailBaseInfoVO);
        }

        logger.debug("deletedMailBaseInfo数量：" + deletedMailBaseInfoVOList.size());

        /*关闭folder*/
        folder.close(true);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("deletedMailBaseInfoVOList", deletedMailBaseInfoVOList);
        resultMap.put("mailEntityVOList", mailEntityVOList);
        resultMap.put("attachmentVOList", attachmentVOList);

        return resultMap;
    }

    /**
     * 动作：永久删除邮件
     *
     * <p>调用该接口将会使指定的邮件彻底删除</p>
     *
     * @param msgUIDToDelete 待彻底删除的邮件的基本信息
     */
    public void deleteMailFromService(Set<String> msgUIDToDelete, MailAccountCenter mailAccountCenter) throws MessagingException {
        ////////////////////////////////////////////////////////////
        // 从服务器端删除邮件
        ////////////////////////////////////////////////////////////
        Folder folder = FolderHelper.getDeletedMessagesFolderWithOpen(mailAccountCenter);
        MailReceiver mailReceiver = new MailReceiverImpl(folder, null);
        List<MessageTO> messageTOList = mailReceiver.getMessages();

        for (MessageTO messageTO : messageTOList) {
            try {
                /*检测是否需要从服务器端同步删除*/
                if (msgUIDToDelete.contains(messageTO.getMsgUID())) {
                    messageTO.getMessage().setFlag(Flags.Flag.DELETED, true);
                }
            } catch (MessagingException e) {
                logger.error(ExceptionUtility.getTrace(e));
            }
        }

        folder.close(true);
    }

    //////////////////////////
    // 私有方法
    /////////////////////////

}
