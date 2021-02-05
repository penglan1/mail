package pers.penglan.mail.utils.message;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.service.conmmon.FileSavingManager;
import pers.penglan.mail.utils.flags.MailFlag;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * message处理辅助工具
 * 
 * @Author PENGL
 * 2020-03-20
 */
public class MessageUtility {

    /**
     * 获取消息的UID
     *
     * <strong>别人掉过的坑</strong>
     * 为了获取UID，先进行类型转换，这样取的UID是不需要下载每一封邮件的，而且速度很高，即使是几千封邮件也很快完成
     * 千万不要用Message.getMessageID();这个方法，这个方法会去下载邮件头，是一个很耗时的过程
     * 不推荐验完首封邮件和尾封邮件就跳出循环的方法，因为有可能新邮件夹在中间的时候
     *
     * @param message
     * @param folder
     * @return
     */
    public static String getMsgUID(Message message, Folder folder) throws MessagingException {
        String msgUID = null;
        if (folder instanceof POP3Folder) {
            POP3Folder pop3Folder = (POP3Folder) folder;
            msgUID = pop3Folder.getUID(message);
        } else if (folder instanceof IMAPFolder) {
            IMAPFolder imapFolder = (IMAPFolder) folder;
            msgUID = Long.toString(imapFolder.getUID(message));
        }
        return msgUID;
    }

    /**
     * 利用{@link MessageSource}来创建一个Message实例
     *
     * @param session
     * @param messageSource
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static Message createMessage(Session session, MessageSource messageSource) throws MessagingException, UnsupportedEncodingException {
        /*创建邮件对象*/
        MimeMessage mimeMessage = new MimeMessage(session);

        MailEntityVO mailEntityVO = messageSource.getMailEntityVO();
        List<AttachmentVO> attachmentVOList = messageSource.getAttachmentVOList();

        ////////////////////////////////////
        // 设置邮件的基本信息
        ///////////////////////////////////
        /*发送者*/
        mimeMessage.setFrom(mailEntityVO.getFrom());

        /*正式发送对象*/
        String to = mailEntityVO.getTo();
        if (to != null && !"".equals(to)) {
            String[] tos = to.split(",");
            for (String t : tos) {
                mimeMessage.addRecipients(Message.RecipientType.TO, t);
            }
        }

        /*抄送人*/
        String cc = mailEntityVO.getCc();
        if (cc != null && !"".equals(cc)) {
            String[] ccs = cc.split(",");
            for (String c : ccs) {
                mimeMessage.addRecipients(Message.RecipientType.CC, c);
            }
        }

        /*暗送人*/
        String bcc = mailEntityVO.getBcc();
        if (bcc != null && !"".equals(bcc)) {
            String[] bccs = bcc.split(",");
            for (String b : bccs) {
                mimeMessage.addRecipients(Message.RecipientType.BCC, b);
            }
        }

        /*设置主题*/
        mimeMessage.setSubject(mailEntityVO.getSubject());

        ///////////////////////////////////
        // 设置邮件内容
        ///////////////////////////////////
        MimeBodyPart content = new MimeBodyPart();
        /*EG: text/html; charset=UTF-8*/
        /*检测是否含有contentType*/
        if (mailEntityVO.getContentType() == null || "".equals(mailEntityVO.getContentType()))
            mailEntityVO.setContentType(MailFlag.TEXT_HTML_UTF8);
        content.setContent(mailEntityVO.getContent(), mailEntityVO.getContentType());

        List<MimeBodyPart> inlineList = new ArrayList<MimeBodyPart>();
        List<MimeBodyPart> attachmentList = new ArrayList<MimeBodyPart>();
        for (AttachmentVO attachmentVO : attachmentVOList) {
            if (attachmentVO.getContentID() != null && !"".equals(attachmentVO.getContentID())) {
                MimeBodyPart inline = new MimeBodyPart();
                /*设置content-ID*/
                inline.setContentID(attachmentVO.getContentID());
                /*设置在线显示inline标记*/
                inline.setDisposition("inline");
                String filenameWithEncode = MimeUtility.encodeText(attachmentVO.getFilename(), "UTF-8", "B");
                inline.setFileName(filenameWithEncode);
                DataHandler dataHandler = new DataHandler(new FileDataSource(
                        FileSavingManager.FILE_SAVING_DIRECTORY + "/" + attachmentVO.getRelativePath()));
                inline.setDataHandler(dataHandler);
                inlineList.add(inline);
            }  else {
                MimeBodyPart attachment = new MimeBodyPart();
                /*设置用于下载的附件标记*/
                attachment.setDisposition("attachment");
                String filenameWithEncode = MimeUtility.encodeText(attachmentVO.getFilename(), "UTF-8", "B");
                attachment.setFileName(filenameWithEncode);
                DataHandler dataHandler = new DataHandler(new FileDataSource(
                        FileSavingManager.FILE_SAVING_DIRECTORY + "/" + attachmentVO.getRelativePath()));
                attachment.setDataHandler(dataHandler);
                attachmentList.add(attachment);
            }
        }

        ///////////////////////////////////
        // 开始组装
        //////////////////////////////////

        MimeBodyPart contentBodyPart = new MimeBodyPart();
        /*将related的组装起来*/
        if (inlineList.size() > 0) {
            MimeMultipart related = new MimeMultipart();
            related.setSubType("related");
            related.addBodyPart(content);
            for (MimeBodyPart mimeBodyPart : inlineList) {
                related.addBodyPart(mimeBodyPart);
            }
            contentBodyPart.setContent(related);
        } else {
            contentBodyPart = content;
        }


        /*将mixed组装起来*/
        MimeMultipart mixed = new MimeMultipart();
        mixed.setSubType("mixed");
        mixed.addBodyPart(contentBodyPart);
        for (MimeBodyPart mimeBodyPart : attachmentList) {
            mixed.addBodyPart(mimeBodyPart);
        }

        mimeMessage.setContent(mixed);

        return mimeMessage;
    }
}
