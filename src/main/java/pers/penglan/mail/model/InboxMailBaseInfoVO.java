package pers.penglan.mail.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收件箱中邮件的基本信息
 *
 * @Author PENGL
 * 2020-03-14
 */
public class InboxMailBaseInfoVO implements Serializable {

    private static final long serialVersionUID = 543904178552569358L;
    /*主键*/
    private String pk_mailBaseInfo;
    /*对应的邮件实体的主键*/
    private String pk_mailEntity;
    /*发件人*/
    private String from;

    /*邮件接收对象*/
    private String to;
    /*邮件主题*/
    private String subject;
    /*发送日期*/
    private String sentDate;
    /*接收日期*/
    private String receivedDate;
    /*邮件所属账号*/
    private String owner;
    /*是否已读标记，已读：Y, 未读：N*/
    private String readFlag;
    /*邮件标记等级, 暂时只支持1（即星标邮件）*/
    private int flag;
    /*邮件的msgUID*/
    private String msgUID;

    public InboxMailBaseInfoVO() {
    }

    @Override
    public String toString() {
        return "InboxMailBaseInfoVO{" +
                "pk_mailBaseInfo='" + pk_mailBaseInfo + '\'' +
                ", pk_mailEntity='" + pk_mailEntity + '\'' +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate='" + sentDate + '\'' +
                ", receivedDate='" + receivedDate + '\'' +
                ", owner='" + owner + '\'' +
                ", readFlag='" + readFlag + '\'' +
                ", flag=" + flag +
                ", msgUID='" + msgUID + '\'' +
                '}';
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgUID() {
        return msgUID;
    }

    public void setMsgUID(String msgUID) {
        this.msgUID = msgUID;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }
}
