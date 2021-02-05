package pers.penglan.mail.model;

import java.util.List;

/**
 * 待发送邮件的信息资源
 *
 * <p>该对象封装了需要发送的邮件的所有内容和附件等资源</p>
 *
 * @Author PENGL
 * 2020-03-17
 */
public class MessageSource {
    private MailEntityVO mailEntityVO;
    private List<AttachmentVO> attachmentVOList;

    public MessageSource(MailEntityVO mailEntityVO, List<AttachmentVO> attachmentVOList) {
        this.mailEntityVO = mailEntityVO;
        this.attachmentVOList = attachmentVOList;
    }

    public MailEntityVO getMailEntityVO() {
        return mailEntityVO;
    }

    public void setMailEntityVO(MailEntityVO mailEntityVO) {
        this.mailEntityVO = mailEntityVO;
    }

    public List<AttachmentVO> getAttachmentVOList() {
        return attachmentVOList;
    }

    public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }
}
