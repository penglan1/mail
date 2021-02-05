package pers.penglan.mail.service.delete;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.DeletedMailBaseInfoMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.DeletedMailBaseInfoVO;
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
public class DeletingManagerWrap {

    @Autowired
    private DeletingManager deletingManager;

    @Autowired
    private DeletedMailBaseInfoMapper deletedMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 动作：已删除
     *
     * <p>返回的格式如下<pre>
     *     resultMap.put("deletedMailBaseInfoVOList", deletedMailBaseInfoVOList);
     *     resultMap.put("mailEntityVOList", mailEntityVOList);
     *     resultMap.put("attachmentVOList", attachmentVOList);
     * </pre></p>
     *
     * @param mailAccountCenter
     * @return
     */
    public List<DeletedMailBaseInfoVO> receiveDeletedMessages(MailAccountCenter mailAccountCenter, Set<String> msgUIDs)
            throws MessagingException {
        /*判断是否需要从数据库中获取所有已经接收了的邮件*/
        if (msgUIDs.size() == 0) {
            List<DeletedMailBaseInfoVO> deletedMailBaseInfoVOList = getDeletedMail(mailAccountCenter);
            for (DeletedMailBaseInfoVO baseInfoVO : deletedMailBaseInfoVOList) {
                msgUIDs.add(baseInfoVO.getMsgUID());
            }
        }
        Map<String, Object> map = deletingManager.receiveDeletedMessages(msgUIDs, mailAccountCenter);

        /*将服务器端接收到的新的 已发送邮件保存到数据库中*/
        List<DeletedMailBaseInfoVO> receivedDeletedMailList = (List<DeletedMailBaseInfoVO>) map.get("deletedMailBaseInfoVOList");
        List<MailEntityVO> receivedMailEntityList = (List<MailEntityVO>) map.get("mailEntityVOList");
        List<AttachmentVO> receivedAttachmentList = (List<AttachmentVO>) map.get("attachmentVOList");
        if (receivedDeletedMailList.size() > 0)
            deletedMailBaseInfoMapper.insertList(receivedDeletedMailList);
        if (receivedMailEntityList.size() > 0)
            mailEntityMapper.insertList(receivedMailEntityList);
        if (receivedAttachmentList.size() > 0)
            attachmentMapper.insertList(receivedAttachmentList);

        return receivedDeletedMailList;

    }

    public List<DeletedMailBaseInfoVO> getDeletedMail(MailAccountCenter mailAccountCenter) {
        /*获取本地的垃圾邮件*/
        DeletedMailBaseInfoVO deletedMailBaseInfoVO = new DeletedMailBaseInfoVO();
        deletedMailBaseInfoVO.setOwner(mailAccountCenter.getOwner());
        List<DeletedMailBaseInfoVO> deletedMailBaseInfoVOList = deletedMailBaseInfoMapper.select(deletedMailBaseInfoVO);

        return deletedMailBaseInfoVOList;
    }

    /**
     * 动作：永久删除邮件
     *
     * <p>调用该接口将会使指定的邮件彻底删除</p>
     *
     * @param listToDelete  待彻底删除的邮件的基本信息
     * @param mailAccountCenter
     */
    public boolean deleteMailFromService(List<DeletedMailBaseInfoVO> listToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        boolean deleteSuccessfully = true;

        /*先从服务器端删除*/
        Set<String> msgUIDsToDelete = new HashSet<>(listToDelete.size());
        for (DeletedMailBaseInfoVO mailBaseInfoVO : listToDelete)
            msgUIDsToDelete.add(mailBaseInfoVO.getMsgUID());

        deletingManager.deleteMailFromService(msgUIDsToDelete, mailAccountCenter);

        /**
         * 从数据库中删除
         * 需要删除的内容有：inboxMainBaseInfo, attachment（包括对应的文件）, mailEntity
         */
        for (DeletedMailBaseInfoVO mailBaseInfoVO : listToDelete) {
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
            deletedMailBaseInfoMapper.deleteByPk_MailBaseInfo(mailBaseInfoVO.getPk_mailBaseInfo());

        }

        logger.info("[动作：删除] 操作成功");

        return deleteSuccessfully;
    }
}
