package pers.penglan.mail.service.conmmon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 专门负责Message分析
 *
 * @Author PENGL
 * 2020-03-14
 */
public class MessageAnalyst {
    private static Logger logger = LogManager.getLogger("mail");

    public static AnalysisResult analyze(Message message) throws IOException, MessagingException {
        Logger logger = LogManager.getLogger("mail");

        MimeMessage msg = (MimeMessage) message;
        /*获取主题，无需再解码*/
        String subject = message.getSubject();

        /*获取发件人的邮件地址*/
        String from = "";
        InternetAddress[] address = (InternetAddress[]) message.getFrom();
        /*处理发件人为null的情况*/
        if (address != null && address.length > 0)
            from = address[0].getAddress().toString();

        /*获取To收件人的地址, 多个地址之间以逗号分隔*/
        InternetAddress[] addressesTo = (InternetAddress[]) message.getRecipients(Message.RecipientType.TO);
        /*处理草稿邮件收件人为null的情况*/
        if (addressesTo == null)
            addressesTo = new InternetAddress[0];
        StringBuilder builderTo = new StringBuilder();
        for (InternetAddress internetAddress : addressesTo)
            builderTo.append(internetAddress.toUnicodeString() + ",");
        if (builderTo.length() > 0)
            builderTo.deleteCharAt(builderTo.length() - 1);
        String To = builderTo.toString();

        /*获取Cc收件人的地址, 多个地址之间以逗号分隔*/
        InternetAddress[] addressesCc = (InternetAddress[]) message.getRecipients(Message.RecipientType.CC);
        StringBuilder builderCc = new StringBuilder();
        if (addressesCc != null) {
            for (InternetAddress internetAddress : addressesCc)
                builderCc.append(internetAddress.toUnicodeString() + ",");
            if (builderCc.length() > 0)
                builderCc.deleteCharAt(builderCc.length() - 1);
        }

        String Cc = builderCc.toString();

        /*获取Bcc收件人的地址, 多个地址之间以逗号分隔*/
        InternetAddress[] addressesBcc = (InternetAddress[]) message.getRecipients(Message.RecipientType.BCC);
        StringBuilder builderBcc = new StringBuilder();
        if (addressesBcc != null) {
            for (InternetAddress internetAddress : addressesBcc)
                builderBcc.append(internetAddress.toUnicodeString() + ",");

            builderBcc.deleteCharAt(builderBcc.length() - 1);
        }
        String Bcc = builderBcc.toString();

        /*获取发送时间*/
        Date date = message.getSentDate();
        /*处理草稿邮件发送时间为null的情况*/
        if (date == null)
            date = new Date(); /*设为当前接收时的时间*/
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String sentDate = new SimpleDateFormat(pattern).format(date);

        /*获取邮件优先级*/
        String[] priorityArr = message.getHeader("X-Priority");
        String priority = "";
        if (priorityArr != null)
            priority = priorityArr[0];


        /*是否需要需要回执*/
        boolean isNeedReply = false;
        String[] headers = message.getHeader("Disposition-Notification-To");
        if (headers != null)
            isNeedReply = true;
        /*邮件大小, 转换为KB*/
        Integer mailSize = message.getSize() / 1024;

        /*如果有附件，则保存附件*/
        List<AttachmentVO> attachmentVOList = saveAttachments(message);

        /*保存邮件的文本内容*/
        String content = getContent(message);
        /*处理可能的一种特殊情况*/
        if ("".equals(content)) {
            if (message.getContent() instanceof  String)
                content = (String) message.getContent();
        }

        /////////////////////////////////////////////////////////////////
        //创建邮件实体，设置VO对应的属性，和附件VO一起封装，作为返回结果
        /////////////////////////////////////////////////////////////////
        MailEntityVO mailEntity = new MailEntityVO();
        /*设置MailEntity的pk*/
        mailEntity.setPk_mailEntity(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        /*设置发件人*/
        mailEntity.setFrom(from);
        /*设置收件人*/
        mailEntity.setTo(To);
        /*设置抄送人*/
        mailEntity.setCc(Cc);
        /*设置按送人*/
        mailEntity.setBcc(Bcc);
        /*设置主题*/
        mailEntity.setSubject(subject);
        /*设置发送日期*/
        mailEntity.setSentDate(sentDate);
        /*设置接收日期*/
        String pattern2 = "yyyy-MM-dd HH:mm:ss";
        String receivedDate = new SimpleDateFormat(pattern2).format(new Date());
        mailEntity.setReceivedDate(receivedDate);
        /*设置是否含有附件*/
        mailEntity.setAttachFlag(attachmentVOList.size() > 0 ? 1 : 0);
        /*设置邮件正文内容*/
        mailEntity.setContent(content);
        /*设置邮件所有者，此处的值由调用者设置*/
        mailEntity.setOwner(null);

        /*设置AttachmentVo中的其余属性值*/
        for (AttachmentVO attachment : attachmentVOList) {
            logger.debug(">>附件信息<<");

            attachment.setPk_attachment(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            attachment.setPk_mailEntity(mailEntity.getPk_mailEntity());

            logger.debug("relativePath：" + attachment.getRelativePath());
            logger.debug("content-ID：" + attachment.getContentID());
            logger.debug("filename：" + attachment.getFilename());
            logger.debug("content-disposition：" + attachment.getContentDisposition());
            logger.debug("content-type：" + attachment.getContentType());
            logger.debug("size：" + attachment.getSize() + "KB");

            logger.debug(">>结束<<");
        }

        /*返回结果*/
        AnalysisResult analysisResult = new AnalysisResult(mailEntity, attachmentVOList);

        return analysisResult;
    }

    private static String getContent(Part part) throws MessagingException, IOException {
        StringBuilder builder = new StringBuilder();
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String subContent = getContent(bodyPart);
                builder.append(subContent);
            }
        } else if (part.isMimeType("text/html")) {
            String subContent = part.getContent().toString();
            builder.append(subContent);
        }

        return builder.toString();
    }

    /**
     * 保存附件
     *
     * @param part
     * @return
     * @throws Exception
     */
    private static List<AttachmentVO> saveAttachments(Part part) throws IOException, MessagingException {
        List<AttachmentVO> list = new ArrayList<AttachmentVO>();
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            /*依次解析每一个复杂邮件体*/
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                /*每个邮件体又有可能是多个邮件体组成的复杂体*/
                if (bodyPart.isMimeType("multipart/*")) {
                    List<AttachmentVO> subList = saveAttachments(bodyPart);
                    list.addAll(subList);
                } else if (disposition != null) {
                    /*开始保存附件内容*/
                    AttachmentVO attachment = new AttachmentVO();
                    /*保存disposition*/
                    attachment.setContentDisposition(disposition);
                    if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                        ;
                    } else if (disposition.equalsIgnoreCase(Part.INLINE)) {
                        /*对于Inline的情况，需要额外再获取其Content-ID，用于寻找文本内容中对该资源的引用*/
                        String cid = bodyPart.getHeader("Content-ID")[0];
                        /**
                         * Content-ID: <6a6ae54f$2$170d3fdd79d$Coremail$penglanm$163.com>
                         * 需要将内容两边的括号去掉,从而使其和文本内容中的引用一致，EG:
                         * src="cid:6a6ae54f$2$170d3fdd79d$Coremail$penglanm$163.com" style="width:
                         *
                         * <strong>使用的时候，记得要在前面增加"cid:"</strong>
                         */
                        /*保存cid*/
                        attachment.setContentID(cid);

                    } else {
                        Exception e = new RuntimeException("附件的Disposition既不是" + Part.INLINE + ", 也不是" + Part.ATTACHMENT);
                        logger.error(ExceptionUtility.getTrace(e));
                    }

                    InputStream is = bodyPart.getInputStream();
                    /*此处得到的有可能是原始的为解码的Ascii码字符串*/
                    String filename = bodyPart.getFileName();
                    /*检测是否已经需要解码，需要，则解码，不需要，则原值返回*/
                    filename = MimeUtility.decodeText(filename);
                    /*保存附件的文件名称*/
                    attachment.setFilename(filename);

                    /*尝试获取文件扩展名*/
                    String fileExtension = null;
                    if (filename.lastIndexOf(".") != -1) {
                        fileExtension = filename.substring(filename.lastIndexOf("."));
                    }
                    String relativePath = saveFile(is, fileExtension);
                    /*保存relativePath*/
                    attachment.setRelativePath(relativePath);

                    /*设置Content-Type*/
                    String contentType = bodyPart.getContentType();
                    attachment.setContentType(contentType);

                    /*设置附件大小*/
                    int size = bodyPart.getSize() / 1024;
                    attachment.setSize(size);

                    list.add(attachment);
                }
            }
        }

        return list;
    }

    private static String saveFile(InputStream inputStream, String fileExtension) throws IOException {
        String pattern = "yyyy/MM";
        String subDir = new SimpleDateFormat(pattern).format(new Date());
        String filePath = FileSavingManager.savingFile(inputStream, subDir, fileExtension);

        return filePath;
    }


    public static class AnalysisResult {
        private MailEntityVO mailEntityVO;
        private List<AttachmentVO> attachmentVOs;

        public AnalysisResult(MailEntityVO mailEntityVO, List<AttachmentVO> attachmentVOs) {
            this.mailEntityVO = mailEntityVO;
            this.attachmentVOs = attachmentVOs;
        }

        public MailEntityVO getMailEntityVO() {
            return mailEntityVO;
        }

        public void setMailEntityVO(MailEntityVO mailEntityVO) {
            this.mailEntityVO = mailEntityVO;
        }

        public List<AttachmentVO> getAttachmentVOs() {
            return attachmentVOs;
        }

        public void setAttachmentVOs(List<AttachmentVO> attachmentVOs) {
            this.attachmentVOs = attachmentVOs;
        }
    }
}
