package pers.penglan.mail.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 已删除邮件的基本信息
 *
 * @Author PENGL
 * 2020-03-14
 */
public class DeletedMailBaseInfoVO implements Serializable {

    private static final long serialVersionUID = -7313767588775390998L;
    /*主键*/
    private String pk_mailBaseInfo;
    /*对应的邮件实体的主键*/
    private String pk_mailEntity;
    /*发件人*/
    private String from;
    /*接收人*/
    private String to;
    /*邮件主题*/
    private String subject;
    /*发送日期*/
    private String sentDate;
    /*邮件所属账号*/
    private String owner;
    /*msgUID*/
    private String msgUID;

    public DeletedMailBaseInfoVO() {

    }

    public String getMsgUID() {
        return msgUID;
    }

    public void setMsgUID(String msgUID) {
        this.msgUID = msgUID;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPk_mailBaseInfo() {
        return pk_mailBaseInfo;
    }

    public void setPk_mailBaseInfo(String pk_mailBaseInfo) {
        this.pk_mailBaseInfo = pk_mailBaseInfo;
    }

    public String getPk_mailEntity() {
        return pk_mailEntity;
    }

    public void setPk_mailEntity(String pk_mailEntity) {
        this.pk_mailEntity = pk_mailEntity;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
