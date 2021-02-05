package pers.penglan.mail.service.draft; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.config.AccountChoose;

import java.util.HashSet;
import java.util.Map;

/**
* DraftManager Tester.
*
* @author PENGL
* @since 03/21/2020
* @version 1.0
*/
public class DraftManagerTest {
    private MailAccountCenter mailAccountCenter;

    private DraftManager draftManager;
    
    @Before
    public void before() throws Exception {
        mailAccountCenter = new MailAccountCenter(AccountChoose.get163Properties());
        draftManager = new DraftManager();
    } 
    
    @After
    public void after() throws Exception { 
    
    } 
    
        /** 
    * Method: saveDraft(MessageSource messageSource) 
    */ 
    @Test
    public void testSaveDraft() throws Exception { 
    
    } 
    
    /** 
    * Method: updateDraft(MessageSource messageSource) 
    */ 
    @Test
    public void testUpdateDraft() throws Exception { 
    
    } 
    
    /** 
    * Method: getDrafts(Set<String> msgUIDsExcluded) 
    */ 
    @Test
    public void testGetDrafts() throws Exception {
        Map<String, Object> map = draftManager.getDrafts(null, mailAccountCenter);
    
    }

    /**
     * Method: setDeleteFlag(Set<String> msgUIDsToDelete)
     */
    @Test
    public void testSetDeleteFlag() throws Exception {
        draftManager.setDeleteFlag(new HashSet<String>(){
            {
                add("1583911157");
            }
        }, mailAccountCenter);
    }
} 
