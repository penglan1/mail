package pers.penglan.mail.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 邮件实体，代表一份完整的邮件
 *
 * @Author PENGL
 * 2020-03-14
 */
public class MailEntityVO  implements Serializable {
    private static final long serialVersionUID = 158878353522761217L;
    /*message的UID*/
    private String msgUID;
    /*邮件所属账户*/
    private String owner;
    /*主键*/
    private String pk_mailEntity;
    /*发件人*/
    private String from;
    /*接收人*/
    private String to;
    /*抄送人*/
    private String cc;
    /*暗送人*/
    private String bcc;
    /*邮件主题*/
    private String subject;
    /*邮件发送日期*/
    private String sentDate;
    /*邮件接收日期*/
    private String receivedDate;
    /*邮件文本内容*/
    private String content;
    /*邮件文本类型，通常为text/html;charset=UTF-8*/
    private String contentType;
    /*附件标志，含有附件：1，没有附件：0*/
    private int attachFlag;

    public MailEntityVO() {

    }

    public String getMsgUID() {
        return msgUID;
    }

    public void setMsgUID(String msgUID) {
        this.msgUID = msgUID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
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

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public void setAttachFlag(int attachFlag) {
        this.attachFlag = attachFlag;
    }

    public int getAttachFlag() {
        return attachFlag;
    }
}
