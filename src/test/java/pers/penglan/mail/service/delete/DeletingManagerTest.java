package pers.penglan.mail.service.delete; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.DeletedMailBaseInfoVO;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.service.config.AccountChoose;

import java.util.List;
import java.util.Map;

/** 
* DeletingManager Tester. 
* 
* @author PENGL 
* @since 03/21/2020
* @version 1.0 
*/ 
public class DeletingManagerTest {
    private MailAccountCenter mailAccountCenter;

    private DeletingManager deletingManager;
    
    @Before
    public void before() throws Exception {
        mailAccountCenter = new MailAccountCenter(AccountChoose.get163Properties());
        deletingManager = new DeletingManager();
    } 
    
    @After
    public void after() throws Exception { 
    
    } 
    
    /**
    * Method: getDeletedMessages(Set<String> msgUIDsExcluded) 
    */ 
    @Test
    public void testGetDeletedMessages() throws Exception {
        Map<String, Object> resultMap =  deletingManager.receiveDeletedMessages(null, mailAccountCenter);
        List<DeletedMailBaseInfoVO> deletedMailBaseInfoVOList = (List<DeletedMailBaseInfoVO>) resultMap.get("deletedMailBaseInfoVOList");
        List<MailEntityVO> mailEntityVOList = (List<MailEntityVO>) resultMap.get("mailEntityVOList");
        List<AttachmentVO> attachmentVOList = (List<AttachmentVO>) resultMap.get("attachmentVOList");

    } 

} 
