package pers.penglan.mail.service.junk;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.JunkMailBaseInfoMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.JunkMailBaseInfoVO;
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
public class JunkManagerWrap {
    @Autowired
    private JunkManager junkManager;

    @Autowired
    private JunkMailBaseInfoMapper junkMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 动作：获取垃圾邮件
     *
     * @param mailAccountCenter
     * @return
     */
    public List<JunkMailBaseInfoVO> getJunk(MailAccountCenter mailAccountCenter) {
        /*获取本地的垃圾邮件*/
        JunkMailBaseInfoVO junkMailBaseInfoVO = new JunkMailBaseInfoVO();
        junkMailBaseInfoVO.setFlag(-1);
        junkMailBaseInfoVO.setOwner(mailAccountCenter.getOwner());
        List<JunkMailBaseInfoVO> junkMailBaseInfoVOList = junkMailBaseInfoMapper.select(junkMailBaseInfoVO);

        return junkMailBaseInfoVOList;
    }

    public List<JunkMailBaseInfoVO> receiveJunk(MailAccountCenter mailAccountCenter, Set<String> msgUIDs)
            throws MessagingException {
        /*判断是否需要从数据库中获取所有已经接收了的邮件*/
        if (msgUIDs.size() == 0) {
            List<JunkMailBaseInfoVO> junkMailBaseInfoVOList = getJunk(mailAccountCenter);
            for (JunkMailBaseInfoVO baseInfoVO : junkMailBaseInfoVOList) {
                msgUIDs.add(baseInfoVO.getMsgUID());
            }
        }
        Map<String, Object> map = junkManager.receiveJunk(msgUIDs, mailAccountCenter);

        /*将服务器端接收到的新的 已发送邮件保存到数据库中*/
        List<JunkMailBaseInfoVO> receivedJunkMailBaseInfoList = (List<JunkMailBaseInfoVO>) map.get("junkMailBaseInfoVOList");
        List<MailEntityVO> receivedMailEntityList = (List<MailEntityVO>) map.get("mailEntityVOList");
        List<AttachmentVO> receivedAttachmentList = (List<AttachmentVO>) map.get("attachmentVOList");
        if (receivedJunkMailBaseInfoList.size() > 0)
            junkMailBaseInfoMapper.insertList(receivedJunkMailBaseInfoList);
        if (receivedMailEntityList.size() > 0)
            mailEntityMapper.insertList(receivedMailEntityList);
        if (receivedAttachmentList.size() > 0)
            attachmentMapper.insertList(receivedAttachmentList);

        return receivedJunkMailBaseInfoList;

    }

    /**
     * 设置删除标记，将邮件加入到已删除中
     *
     * @param listToDelete
     * @param mailAccountCenter
     * @throws MessagingException
     */
    public boolean setDeleteFlag(List<JunkMailBaseInfoVO> listToDelete, MailAccountCenter mailAccountCenter)
            throws MessagingException {
        boolean deleteSuccessfully = true;
        /*先从服务器端删除*/
        Set<String> msgUIDsToDelete = new HashSet<>(listToDelete.size());
        for (JunkMailBaseInfoVO mailBaseInfoVO : listToDelete)
            msgUIDsToDelete.add(mailBaseInfoVO.getMsgUID());

        junkManager.setDeleteFlag(msgUIDsToDelete, mailAccountCenter);

        /**
         * 从数据库中删除
         * 需要删除的内容有：inboxMainBaseInfo, attachment（包括对应的文件）, mailEntity
         */
        for (JunkMailBaseInfoVO mailBaseInfoVO : listToDelete) {
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
            /*删除 junkMailBaseInfo*/
            junkMailBaseInfoMapper.deleteByPk_MailBaseInfo(mailBaseInfoVO.getPk_mailBaseInfo());

        }

        logger.info("[动作：删除] 操作成功");

        return deleteSuccessfully;
    }

    /**
     * 更新邮件的基本信息
     * @param updateList
     * @param mailAccountCenter
     */
    public void updateMailBaseInfo(List<JunkMailBaseInfoVO> updateList, MailAccountCenter mailAccountCenter) {
        for (JunkMailBaseInfoVO mailBaseInfoVO : updateList) {
            /*禁止将pk_mailEntity重设*/
            mailBaseInfoVO.setPk_mailEntity(null);
            mailBaseInfoVO.setOwner(null);
            mailBaseInfoVO.setMsgUID(null);
            junkMailBaseInfoMapper.update(mailBaseInfoVO);
        }
    }
}
