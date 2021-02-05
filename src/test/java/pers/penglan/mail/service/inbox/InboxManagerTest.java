package pers.penglan.mail.service.inbox; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.config.AccountChoose;
import pers.penglan.mail.service.conmmon.MessageAnalyst;
import pers.penglan.mail.utils.message.MessageUtility;

import javax.mail.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/** 
* InboxManager Tester. 
* 
* @author PENGL 
* @since 03/18/2020
* @version 1.0 
*/ 
public class InboxManagerTest {
    private MailAccountCenter mailAccountCenter;

    private InboxManager inboxManager;
    
    @Before
    public void before() throws Exception {
        mailAccountCenter = new MailAccountCenter(AccountChoose.getQQProperties());
        inboxManager = new InboxManager();

    } 
    
    @After
    public void after() throws Exception { 
    
    } 
    
    /**
    * Method: receiveMail() 
    */ 
    @Test
    public void testReceiveMail() throws Exception {
        Map<String, Object> resultMap = inboxManager.receiveMail(new HashSet<String>(), mailAccountCenter);
    }


    /**
     * 测试列出所有文件夹
     */
    @Test
    public void testListFolders() throws MessagingException {
        Session session = mailAccountCenter.getSession();
        Store store = session.getStore();
        store.connect();
        Folder defaultFolder = store.getDefaultFolder();
        //inbox.open(Folder.READ_WRITE);
        Folder[] folders = defaultFolder.list("*");
        for (Folder folder : folders) {
            System.out.println(folder.getName());
        }
        /////////////////网易邮箱列出如下文件夹
        //INBOX
        //草稿箱
        //已发送
        //已删除
        //垃圾邮件
        //病毒文件夹
    }

    @Test
    public void testAllBox() throws MessagingException, IOException {
        Session session = mailAccountCenter.getSession();
        Store store = session.getStore();
        store.connect();
        Folder folder = store.getFolder("垃圾邮件");
        folder.open(Folder.READ_WRITE);

        Message[] messages = folder.getMessages();

        for (Message message : messages) {
            System.out.println("--------------" + MessageUtility.getMsgUID(message, folder) + "--------------");
            System.out.println(message.getSubject());
            MessageAnalyst.AnalysisResult result = MessageAnalyst.analyze(message);
            System.out.println(result.getMailEntityVO().getContent());
            //message.setFlag(Flags.Flag.DELETED, true);
        }

        folder.close(true);

    }
    
    /** 
    * Method: getInbox(Set<String> msgUIDExcluded) 
    */ 
    @Test
    public void testGetInbox() throws Exception { 
    
    } 
    
    /** 
    * Method: getMailEntity(String pk_mailEntity) 
    */ 
    @Test
    public void testGetMailEntity() throws Exception { 
    
    } 

        
} 
