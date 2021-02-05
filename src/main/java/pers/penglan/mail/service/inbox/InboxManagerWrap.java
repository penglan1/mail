package pers.penglan.mail.service.inbox;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.InboxMailBaseInfoMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.InboxMailBaseInfoVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FileDeletingManager;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author PENGL
 * 2020-03-25
 */
@Service
public class InboxManagerWrap {

    @Autowired
    private InboxManager inboxManager;

    @Autowired
    private InboxMailBaseInfoMapper inboxMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 动作：收新邮件
     *
     * @return 新邮件的基本信息，邮件实体，附件列表，形式如下：
     * <pre>
     *             resultMap.put("newMailBaseInfoVOList", newMailBaseInfoVOList);
     *             resultMap.put("mailEntityVOList", mailEntityVOList);
     *             resultMap.put("attachmentVOList", attachmentVOList);
     *         </pre>
     */
    public List<InboxMailBaseInfoVO> receiveMail(MailAccountCenter mailAccountCenter, Set<String> msgUIDs) throws MessagingException {
        List<InboxMailBaseInfoVO> receivedInboxMailBaseInfoList = null;
        List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = null;
        /*判断是否需要从数据库中获取所有已经接收了的邮件*/
        if (msgUIDs.size() == 0) {
            inboxMailBaseInfoVOList = getInbox(mailAccountCenter);
            for (InboxMailBaseInfoVO baseInfoVO : inboxMailBaseInfoVOList) {
                msgUIDs.add(baseInfoVO.getMsgUID());
            }
        }

        /*从服务器中接收新邮件*/
        Map<String, Object> map = inboxManager.receiveMail(msgUIDs, mailAccountCenter);

        /*将服务器端接收到的新的 已发送邮件保存到数据库中*/
        receivedInboxMailBaseInfoList = (List<InboxMailBaseInfoVO>) map.get("inboxMailBaseInfoVOList");
        List<MailEntityVO> receivedMailEntityList = (List<MailEntityVO>) map.get("mailEntityVOList");
        List<AttachmentVO> receivedAttachmentList = (List<AttachmentVO>) map.get("attachmentVOList");
        if (receivedInboxMailBaseInfoList.size() > 0)
            inboxMailBaseInfoMapper.insertList(receivedInboxMailBaseInfoList);
        if (receivedMailEntityList.size() > 0)
            mailEntityMapper.insertList(receivedMailEntityList);
        if (receivedAttachmentList.size() > 0)
            attachmentMapper.insertList(receivedAttachmentList);

        logger.info("[动作：接收新邮件] 操作成功");

        return receivedInboxMailBaseInfoList;
    }

    /**
     * 动作：收件箱
     * <p>
     * 为了减少不必要的流量输出，已将发送给了前台的邮件可以通过邮件的UID来被过滤掉
     *
     * @return
     */
    public List<InboxMailBaseInfoVO> getInbox(MailAccountCenter mailAccountCenter) {
        List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = inboxMailBaseInfoMapper.selectByOwner(mailAccountCenter.getOwner());

        logger.info("[动作：收件箱] 操作成功");

        return inboxMailBaseInfoVOList;
    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param listToDelete
     * @throws MessagingException
     */
    public boolean setDeleteFlag(List<InboxMailBaseInfoVO> listToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        boolean deleteSuccessfully = true;

        /*先从服务器端删除*/
        Set<String> msgUIDsToDelete = new HashSet<>(listToDelete.size());
        for (InboxMailBaseInfoVO mailBaseInfoVO : listToDelete)
            msgUIDsToDelete.add(mailBaseInfoVO.getMsgUID());

        inboxManager.setDeleteFlag(msgUIDsToDelete, mailAccountCenter);

        /**
         * 从数据库中删除
         * 需要删除的内容有：inboxMainBaseInfo, attachment（包括对应的文件）, mailEntity
         */
        for (InboxMailBaseInfoVO mailBaseInfoVO : listToDelete) {
            String pk_mailEntity = mailBaseInfoVO.getPk_mailEntity();
            /*获取 mailEntity*/
            MailEntityVO mailEntityVO = mailEntityMapper.selectByPk_MailEntity(pk_mailEntity);
            /*删除 mailEntity*/
            mailEntityMapper.deleteByPk_MailEntity(pk_mailEntity);
            /*获取 attachment*/
            List<AttachmentVO> attachmentVOList = attachmentMapper.selectByPk_MailEntity(pk_mailEntity);
            /*删除 attachment*/
            attachmentMapper.deleteByPk_MailEntity(pk_mailEntity);
            for (AttachmentVO attachmentVO : attachmentVOList) {
                FileDeletingManager.deleteAttachment(attachmentVO);
            }
            /*删除 inboxMailBaseInfo*/
            inboxMailBaseInfoMapper.deleteByPk_MailBaseInfo(mailBaseInfoVO.getPk_mailBaseInfo());

        }


        logger.info("[动作：删除] 操作成功");

        return deleteSuccessfully;
    }

    /**
     * 更新邮件的基本信息
     * @param updateList
     * @param mailAccountCenter
     */
    public void updateMailBaseInfo(List<InboxMailBaseInfoVO> updateList, MailAccountCenter mailAccountCenter) {
        for (InboxMailBaseInfoVO mailBaseInfoVO : updateList) {
            /*禁止将pk_mailEntity重设*/
            mailBaseInfoVO.setPk_mailEntity(null);
            mailBaseInfoVO.setOwner(null);
            mailBaseInfoVO.setMsgUID(null);
            inboxMailBaseInfoMapper.update(mailBaseInfoVO);
        }
    }
}
