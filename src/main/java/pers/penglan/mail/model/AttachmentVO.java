package pers.penglan.mail.model;

import java.io.Serializable;

/**
 * @Author PENGL
 * 2020-03-14
 */
public class AttachmentVO implements Serializable {

    private static final long serialVersionUID = -5584317232740964342L;
    /*在文本中被引用的cid*/
    private String contentID;
    /*附件的相对路径*/
    private String relativePath;
    /*主键*/
    private String pk_attachment;
    /*文件名*/
    private String filename;
    /*附件所属邮件实体的主键*/
    private String pk_mailEntity;
    /*附件的使用方式：两种值inline和attachment*/
    private String contentDisposition;
    /*附件的类型：例如 image/png*/
    private String contentType;
    /*附件大小，单位为KB*/
    private int size;

    public AttachmentVO() {
    }

    public String getContentID() {
        return contentID;
    }

    public String getContentIDWithoutAngle() {
        if (contentID != null && contentID.indexOf("<") == 0
                && contentID.lastIndexOf(">") == (contentID.length() - 1))
            return contentID.substring(1, contentID.length() - 1);
        else
            return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getPk_attachment() {
        return pk_attachment;
    }

    public void setPk_attachment(String pk_attachment) {
        this.pk_attachment = pk_attachment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPk_mailEntity() {
        return pk_mailEntity;
    }

    public void setPk_mailEntity(String pk_mailEntity) {
        this.pk_mailEntity = pk_mailEntity;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}