package pers.penglan.mail.service.outbox;

import com.sun.mail.imap.IMAPFolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.config.AccountChoose;
import pers.penglan.mail.service.conmmon.FolderHelper;
import pers.penglan.mail.utils.message.MessageUtility;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/** 
* OutboxManager Tester. 
* 
* @author PENGL 
* @since 03/19/2020
* @version 1.0 
*/ 
public class OutboxManagerTest {
    private MailAccountCenter mailAccountCenter;

    private OutboxManager outboxManager;

    @Before
    public void before() throws Exception {
        mailAccountCenter = new MailAccountCenter(AccountChoose.getQQProperties());
        outboxManager = new OutboxManager();

    }

    @After
    public void after() throws Exception { 
    
    }
    
        /** 
    * Method: sendMail(MessageSource messageSource) 
    */ 
    @Test
    public void testSendMail() throws Exception {
        MessageSource messageSource = getMessageSource2();

        //outboxManager.sendMail(messageSource, mailAccountCenter);
        /*尝试保存草稿邮件*/
        Message message = MessageUtility.createMessage(mailAccountCenter.getSession(), messageSource);
        message.setFlag(Flags.Flag.DRAFT, true);
        message.saveChanges();

        Folder draftFolder = FolderHelper.getDraftsFolderWithOpen(mailAccountCenter);
        /*使用这个方法添加到草稿箱，追加，追加，追加*/
        draftFolder.appendMessages(new Message[]{message});
        draftFolder.close(true);

    
    }

    @Test
    public void testSendMail2() throws UnsupportedEncodingException, MessagingException {
        MessageSource messageSource = getMessageSource2();
        Message message = MessageUtility.createMessage(mailAccountCenter.getSession(), messageSource);

        IMAPFolder folder = (IMAPFolder) FolderHelper.getSentMessagesFolderWithOpen(mailAccountCenter);
        message.saveChanges();
        folder.appendMessages(new Message[]{message});

        //String uid = Long.toString(folder.getUID(message));
        folder.close(true);

    }

    @Test
    public void testSentMail3() throws UnsupportedEncodingException, MessagingException {
        MessageSource messageSource = getMessageSource2();
        outboxManager.sendMail(messageSource, mailAccountCenter);
    }
    
    /** 
    * Method: createMessage(Session session, MessageSource messageSource) 
    */ 
    @Test
    public void testCreateMessage() throws Exception {
        // 1583911284  1583911282
    }

    @Test
    public void testGetSentMessages() throws MessagingException {
        Map<String, Object> map = outboxManager.getSentMessages(null, mailAccountCenter);
    }

    @Test
    public void testSetDeleteFalg() throws MessagingException {
        outboxManager.setDeleteFlag(new HashSet<String>(){
            {
                add("1583911282");
            }
        }, mailAccountCenter);
    }
    

    /** 
    * Method: getSession() 
    */ 
    @Test
    public void testGetSession() throws Exception { 
    
        /* 
        try { 
           Method method = OutboxManager.getClass().getMethod("getSession"); 
           method.setAccessible(true); 
           method.invoke(<Object>, <Parameters>); 
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) { 
        } 
        */ 
    }
        
    /** 
    * Method: getTransport() 
    */ 
    @Test
    public void testGetTransport() throws Exception { 
    
        /* 
        try { 
           Method method = OutboxManager.getClass().getMethod("getTransport"); 
           method.setAccessible(true); 
           method.invoke(<Object>, <Parameters>); 
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) { 
        } 
        */ 
    }

    /**
     * 既包含inline附件，也包含attachment附件
     *
     * @return
     */
    private MessageSource getMessageSource() {
        MailEntityVO mailEntityVO = new MailEntityVO();
        mailEntityVO.setPk_mailEntity("1234567");
        mailEntityVO.setOwner("penglanm@qq.com");
        mailEntityVO.setAttachFlag(1);
//        mailEntityVO.setBcc("");
        mailEntityVO.setCc("penglanm@163.com");
        mailEntityVO.setTo("penglan5@qq.com");
        mailEntityVO.setFrom("penglanm@qq.com");
        mailEntityVO.setSubject("邮件发送测试任务");
        mailEntityVO.setContentType("text/html; charset=UTF-8");
        mailEntityVO.setContent("<h1>你好，邮件接收者</h1>" +
                "<img src='cid:001'/>" +
                "上面的是不是图片？？？");

        List<AttachmentVO> attachmentVOList = new ArrayList<>();
        AttachmentVO attachmentVO1 = new AttachmentVO();
        attachmentVO1.setContentID("001");
//        attachmentVO1.setContentDisposition("inline");
//        attachmentVO1.setContentType("image/png");
        attachmentVO1.setRelativePath("2020/03/2808356641181696.png");
        attachmentVO1.setFilename("长风破浪.png");

        attachmentVOList.add(attachmentVO1);

        attachmentVO1 = new AttachmentVO();
//        attachmentVO1.setContentID("002");
//        attachmentVO1.setContentDisposition("attachment");
//        attachmentVO1.setContentType("image/png");
        attachmentVO1.setRelativePath("2020/03/2808352392351744.docx");
        attachmentVO1.setFilename("毕业初稿.docx");

        attachmentVOList.add(attachmentVO1);

        return new MessageSource(mailEntityVO, attachmentVOList);
    }

    /**
     * 简单邮件
     *
     * @return
     */
    private MessageSource getMessageSource2() {
        MailEntityVO mailEntityVO = new MailEntityVO();
        mailEntityVO.setPk_mailEntity("1234567");
        mailEntityVO.setOwner(mailAccountCenter.getAccount());
        mailEntityVO.setAttachFlag(1);
//        mailEntityVO.setBcc("");
        mailEntityVO.setTo("penglanm@163.com");
//        mailEntityVO.setTo("penglan5@qq.com");
        /*草稿邮件尝试发送，不设置收件人*/
        mailEntityVO.setFrom(mailAccountCenter.getAccount());
        mailEntityVO.setSubject("去处理smtp勾选之后发送的第二封邮件");
        mailEntityVO.setContentType("text/html; charset=UTF-8");
        mailEntityVO.setContent("<h1>第二封邮件</h1>");

        List<AttachmentVO> attachmentVOList = new ArrayList<>();

        return new MessageSource(mailEntityVO, attachmentVOList);
    }
} 
