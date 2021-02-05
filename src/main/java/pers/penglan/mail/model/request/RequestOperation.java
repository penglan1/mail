package pers.penglan.mail.model.request;

import pers.penglan.mail.model.*;

/**
 * @Author PENGL
 * 2020-03-26
 */
public class RequestOperation {
    private String owner;

    private String action;

    private String[] msgUIDToDelete;

    private MailEntityVO mailEntityVO;

    private AttachmentVO[] attachmentVOs;

    private InboxMailBaseInfoVO[] inboxMailBaseInfoVOs;

    private SentMailBaseInfoVO[] sentMailBaseInfoVOs;

    private JunkMailBaseInfoVO[] junkMailBaseInfoVOs;

    private DeletedMailBaseInfoVO[] deletedMailBaseInfoVOs;

    private DraftMailBaseInfoVO[] draftMailBaseInfoVOs;

    public DraftMailBaseInfoVO[] getDraftMailBaseInfoVOs() {
        return draftMailBaseInfoVOs;
    }

    public void setDraftMailBaseInfoVOs(DraftMailBaseInfoVO[] draftMailBaseInfoVOs) {
        this.draftMailBaseInfoVOs = draftMailBaseInfoVOs;
    }

    public DeletedMailBaseInfoVO[] getDeletedMailBaseInfoVOs() {
        return deletedMailBaseInfoVOs;
    }

    public void setDeletedMailBaseInfoVOs(DeletedMailBaseInfoVO[] deletedMailBaseInfoVOs) {
        this.deletedMailBaseInfoVOs = deletedMailBaseInfoVOs;
    }

    public JunkMailBaseInfoVO[] getJunkMailBaseInfoVOs() {
        return junkMailBaseInfoVOs;
    }

    public void setJunkMailBaseInfoVOs(JunkMailBaseInfoVO[] junkMailBaseInfoVOs) {
        this.junkMailBaseInfoVOs = junkMailBaseInfoVOs;
    }

    public SentMailBaseInfoVO[] getSentMailBaseInfoVOs() {
        return sentMailBaseInfoVOs;
    }

    public void setSentMailBaseInfoVOs(SentMailBaseInfoVO[] sentMailBaseInfoVOs) {
        this.sentMailBaseInfoVOs = sentMailBaseInfoVOs;
    }

    public InboxMailBaseInfoVO[] getInboxMailBaseInfoVOs() {
        return inboxMailBaseInfoVOs;
    }

    public void setInboxMailBaseInfoVOs(InboxMailBaseInfoVO[] inboxMailBaseInfoVOs) {
        this.inboxMailBaseInfoVOs = inboxMailBaseInfoVOs;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getMsgUIDToDelete() {
        return msgUIDToDelete;
    }

    public void setMsgUIDToDelete(String[] msgUIDToDelete) {
        this.msgUIDToDelete = msgUIDToDelete;
    }

    public MailEntityVO getMailEntityVO() {
        return mailEntityVO;
    }

    public void setMailEntityVO(MailEntityVO mailEntityVO) {
        this.mailEntityVO = mailEntityVO;
    }

    public AttachmentVO[] getAttachmentVOs() {
        return attachmentVOs;
    }

    public void setAttachmentVOs(AttachmentVO[] attachmentVOs) {
        this.attachmentVOs = attachmentVOs;
    }
}
