package pers.penglan.mail.service.outbox;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.mapper.SentMailBaseInfoMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.model.SentMailBaseInfoVO;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FileDeletingManager;
import pers.penglan.mail.utils.flags.MailFlag;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author PENGL
 * 2020-03-25
 */
@Service
public class OutboxManagerWrap {

    @Autowired
    private OutboxManager outboxManager;

    @Autowired
    private SentMailBaseInfoMapper sentMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 动作：发邮件
     *
     * @param messageSource 待发送邮件的内容资源
     * @return true: 发送成功； false: 发送失败
     */
    public boolean sendMail(MessageSource messageSource, MailAccountCenter mailAccountCenter)
            throws UnsupportedEncodingException, MessagingException {
        boolean sendSuccessfully = true;
        SentMailBaseInfoVO sentMailBaseInfoVO = null;

        outboxManager.sendMail(messageSource, mailAccountCenter);

        logger.info("[动作：发邮件] 邮件发送成功" + sentMailBaseInfoVO);

        return sendSuccessfully;
    }

    /**
     * 动作：发件箱
     *
     * <p>发件箱中保存的是发送异常的邮件</p>
     *
     * <p>使用邮件发送接口时，如果邮件发送失败，调用者应当将该邮件保存到<strong>发件箱<</strong>/p>
     *
     * @return
     */
    public List<SentMailBaseInfoVO> getSentFailureMessages(MailAccountCenter mailAccountCenter) {
        SentMailBaseInfoVO sentMailBaseInfoVO = new SentMailBaseInfoVO();
        sentMailBaseInfoVO.setSentDate(null);
        /*设置查询条件*/
        sentMailBaseInfoVO.setOwner(mailAccountCenter.getOwner());
        sentMailBaseInfoVO.setStatus("failure");

        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = sentMailBaseInfoMapper.selectList(sentMailBaseInfoVO);

        logger.info("[动作：发件箱] 操作成功");

        return sentMailBaseInfoVOList;
    }

    /**
     * 从服务器中检查是否有未接收的已发送邮件
     * @param mailAccountCenter
     * @return
     * @throws MessagingException
     */
    public List<SentMailBaseInfoVO> receiveMail(MailAccountCenter mailAccountCenter, Set<String> msgUIDs)
            throws MessagingException {
        /*判断是否需要从数据库中获取所有已经接收了的邮件*/
        if (msgUIDs.size() == 0) {
            List<SentMailBaseInfoVO> sentMailBaseInfoVOList = getSentMessages(mailAccountCenter);
            for (SentMailBaseInfoVO baseInfoVO : sentMailBaseInfoVOList) {
                msgUIDs.add(baseInfoVO.getMsgUID());
            }
        }
        Map<String, Object> map = outboxManager.getSentMessages(msgUIDs, mailAccountCenter);

        /*将服务器端接收到的新的 已发送邮件保存到数据库中*/
        List<SentMailBaseInfoVO> receivedSentMailBaseInfoList = (List<SentMailBaseInfoVO>) map.get("sentMailBaseInfoVOList");
        List<MailEntityVO> receivedMailEntityList = (List<MailEntityVO>) map.get("mailEntityVOList");
        List<AttachmentVO> receivedAttachmentList = (List<AttachmentVO>) map.get("attachmentVOList");
        if (receivedSentMailBaseInfoList.size() > 0)
            sentMailBaseInfoMapper.insertList(receivedSentMailBaseInfoList);
        if (receivedMailEntityList.size() > 0)
            mailEntityMapper.insertList(receivedMailEntityList);
        if (receivedAttachmentList.size() > 0)
            attachmentMapper.insertList(receivedAttachmentList);

        return receivedSentMailBaseInfoList;
    }

    /**
     * 动作：已发送
     *
     * <p>已发送，会将所有成功发送的邮件返送回来</p>
     *
     * @return
     */
    public List<SentMailBaseInfoVO> getSentMessages(MailAccountCenter mailAccountCenter) {
        SentMailBaseInfoVO sentMailBaseInfoVO = new SentMailBaseInfoVO();
        /*将日期的默认值覆盖掉*/
        sentMailBaseInfoVO.setSentDate(null);
        sentMailBaseInfoVO.setFlag(-1);
        sentMailBaseInfoVO.setStatus("success");
        sentMailBaseInfoVO.setOwner(mailAccountCenter.getOwner());

        /*从数据库中获取已发送成功的邮件*/
        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = sentMailBaseInfoMapper.selectList(sentMailBaseInfoVO);

        logger.info("[动作：已发送] 操作成功");

        return sentMailBaseInfoVOList;
    }

    /**
     * 更新邮件的基本信息
     * @param updateList
     * @param mailAccountCenter
     */
    public void updateMailBaseInfo(List<SentMailBaseInfoVO> updateList, MailAccountCenter mailAccountCenter) {
        for (SentMailBaseInfoVO mailBaseInfoVO : updateList) {
            /*禁止重设一些中要的属性*/
            mailBaseInfoVO.setPk_mailEntity(null);
            mailBaseInfoVO.setOwner(null);
            mailBaseInfoVO.setMsgUID(null);
            sentMailBaseInfoMapper.update(mailBaseInfoVO);
        }
    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param listToDelete
     * @throws MessagingException
     */
    public boolean setDeleteFlag(List<SentMailBaseInfoVO> listToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        boolean deleteSuccessfully = true;

        /*先从服务器端删除*/
        Set<String> msgUIDsToDelete = new HashSet<>(listToDelete.size());
        for (SentMailBaseInfoVO mailBaseInfoVO : listToDelete)
            msgUIDsToDelete.add(mailBaseInfoVO.getMsgUID());

        outboxManager.setDeleteFlag(msgUIDsToDelete, mailAccountCenter);

        /**
         * 从数据库中删除
         * 需要删除的内容有：sentMainBaseInfo, attachment（包括对应的文件）, mailEntity
         */
        for (SentMailBaseInfoVO mailBaseInfoVO : listToDelete) {
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
            /*删除 sentMailBaseInfo*/
            sentMailBaseInfoMapper.deleteByPk_MailBaseInfo(mailBaseInfoVO.getPk_mailBaseInfo());

        }


        return deleteSuccessfully;
    }

    public static SentMailBaseInfoVO createFailedSentMailBaseInfoVO(MailEntityVO mailEntityVO) {
        SentMailBaseInfoVO sentMailBaseInfoVO = new SentMailBaseInfoVO();
        sentMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        sentMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
        sentMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
        sentMailBaseInfoVO.setTo(mailEntityVO.getTo());
        sentMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
        sentMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
        sentMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
        sentMailBaseInfoVO.setStatus(MailFlag.FAILURE);
        sentMailBaseInfoVO.setFlag(MailFlag.ORDINARY);
        sentMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());

        return sentMailBaseInfoVO;
    }

    public static SentMailBaseInfoVO createSuccessSentMailBaseInfoVO(MailEntityVO mailEntityVO) {
        SentMailBaseInfoVO sentMailBaseInfoVO = new SentMailBaseInfoVO();
        sentMailBaseInfoVO.setPk_mailBaseInfo(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        sentMailBaseInfoVO.setPk_mailEntity(mailEntityVO.getPk_mailEntity());
        sentMailBaseInfoVO.setFrom(mailEntityVO.getFrom());
        sentMailBaseInfoVO.setTo(mailEntityVO.getTo());
        sentMailBaseInfoVO.setOwner(mailEntityVO.getOwner());
        sentMailBaseInfoVO.setSentDate(mailEntityVO.getSentDate());
        sentMailBaseInfoVO.setSubject(mailEntityVO.getSubject());
        sentMailBaseInfoVO.setStatus(MailFlag.SUCCESS);
        sentMailBaseInfoVO.setFlag(MailFlag.ORDINARY);
        sentMailBaseInfoVO.setMsgUID(mailEntityVO.getMsgUID());

        return sentMailBaseInfoVO;
    }
}
