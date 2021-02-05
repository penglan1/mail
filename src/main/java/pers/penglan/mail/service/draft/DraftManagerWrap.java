package pers.penglan.mail.service.draft;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.DraftMailBaseInfoMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.DraftMailBaseInfoVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FileDeletingManager;

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
public class DraftManagerWrap {

    @Autowired
    private DraftManager draftManager;

    @Autowired
    private DraftMailBaseInfoMapper draftMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 动作：保存为草稿
     *
     * <p>由于无法将邮件发送到服务器端的草稿箱中，因此该方法暂时无实际用处。如果后续有办法解决，可以
     * 再将此方法的实现完善</p>
     *
     * <strong>建议在XXXManagerWrap中完成对数据库的操作</strong>
     *
     * @param messageSource
     * @param mailAccountCenter
     */
    public void saveDraft(MessageSource messageSource, MailAccountCenter mailAccountCenter)
            throws UnsupportedEncodingException, MessagingException {
        draftManager.saveDraft(messageSource, mailAccountCenter);

        logger.info("[动作：保存草稿] 操作成功");

    }

    /**
     * 动作：更新草稿（保存对草稿的修改）
     *
     * <p>由于无法将邮件发送到服务器端的草稿箱中，因此该方法暂时无实际用处。如果后续有办法解决，可以
     * 再将此方法的实现完善</p>
     *
     * <strong>建议在XXXManagerWrap中完成对数据库的操作</strong>
     *
     * @param messageSource
     * @param mailAccountCenter
     */
    public void updateDraft(MessageSource messageSource, MailAccountCenter mailAccountCenter) {
        MailEntityVO mailEntityVO = messageSource.getMailEntityVO();
        List<AttachmentVO> attachmentVOList = messageSource.getAttachmentVOList();
        /*调用删除操作，将原来的那封邮件从服务器端的草稿箱中删除*/

        /*将原来的邮件从本地数据库中删除*/

        /*将修改了之后的邮件重新添加到服务器端的草稿箱中，从而实现邮件的更新操作*/



    }

    /**
     * 接收新的草稿邮件
     * @param mailAccountCenter
     */
    public List<DraftMailBaseInfoVO> receiveDrafts(MailAccountCenter mailAccountCenter, Set<String> msgUIDs) throws MessagingException {
        /*判断是否需要从数据库中获取所有已经接收了的邮件*/
        if (msgUIDs.size() == 0) {
            List<DraftMailBaseInfoVO> draftMailBaseInfoVOList = getDrafts(mailAccountCenter);
            for (DraftMailBaseInfoVO baseInfoVO : draftMailBaseInfoVOList) {
                msgUIDs.add(baseInfoVO.getMsgUID());
            }
        }
        Map<String, Object> map = draftManager.getDrafts(msgUIDs, mailAccountCenter);

        /*将服务器端接收到的新的 已发送邮件保存到数据库中*/
        List<DraftMailBaseInfoVO> receivedDraftMailBaseInfoList = (List<DraftMailBaseInfoVO>) map.get("draftMailBaseInfoVOList");
        List<MailEntityVO> receivedMailEntityList = (List<MailEntityVO>) map.get("mailEntityVOList");
        List<AttachmentVO> receivedAttachmentList = (List<AttachmentVO>) map.get("attachmentVOList");
        if (receivedAttachmentList.size() > 0)
            attachmentMapper.insertList(receivedAttachmentList);
        if (receivedMailEntityList.size() > 0)
            mailEntityMapper.insertList(receivedMailEntityList);
        if (receivedDraftMailBaseInfoList.size() > 0)
            draftMailBaseInfoMapper.insertList(receivedDraftMailBaseInfoList);

        return receivedDraftMailBaseInfoList;
    }

    /**
     * 获取草稿
     *
     * @param mailAccountCenter
     * @return
     */
    public List<DraftMailBaseInfoVO> getDrafts(MailAccountCenter mailAccountCenter) {
        List<DraftMailBaseInfoVO> draftMailBaseInfoVOList = draftMailBaseInfoMapper.selectByOwner(mailAccountCenter.getOwner());

        logger.info("[动作：获取草稿] 操作成功");

        return draftMailBaseInfoVOList;
    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param listToDelete
     * @param mailAccountCenter
     * @throws MessagingException
     */
    public boolean setDeleteFlag(List<DraftMailBaseInfoVO> listToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        boolean deleteSuccessfully = true;
        /*先从服务器端删除*/
        Set<String> msgUIDsToDelete = new HashSet<>(listToDelete.size());
        for (DraftMailBaseInfoVO mailBaseInfoVO : listToDelete)
            msgUIDsToDelete.add(mailBaseInfoVO.getMsgUID());

        draftManager.setDeleteFlag(msgUIDsToDelete, mailAccountCenter);

        /**
         * 从数据库中删除
         * 需要删除的内容有：inboxMainBaseInfo, attachment（包括对应的文件）, mailEntity
         */
        for (DraftMailBaseInfoVO mailBaseInfoVO : listToDelete) {
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
            /*删除 draftMailBaseInfo*/
            draftMailBaseInfoMapper.deleteByPk_MailBaseInfo(mailBaseInfoVO.getPk_mailBaseInfo());

        }

        logger.info("[动作：删除] 操作成功");

        return deleteSuccessfully;
    }

    /**
     * 更新邮件的基本信息
     * @param updateList
     * @param mailAccountCenter
     */
    public void updateMailBaseInfo(List<DraftMailBaseInfoVO> updateList, MailAccountCenter mailAccountCenter) {
        for (DraftMailBaseInfoVO mailBaseInfoVO : updateList) {
            /*禁止将pk_mailEntity重设*/
            mailBaseInfoVO.setPk_mailEntity(null);
            mailBaseInfoVO.setOwner(null);
            mailBaseInfoVO.setMsgUID(null);
            draftMailBaseInfoMapper.update(mailBaseInfoVO);
        }
    }
}
