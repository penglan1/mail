package pers.penglan.mail.service.conmmon;

import pers.penglan.mail.model.user.mail.MailAccountCenter;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * 获取不同的Folder
 *
 * @Author PENGL
 * 2020-03-21
 */
public class FolderHelper {

    /**
     * 获取已删除文件夹
     *
     * @param accountInfo
     * @return
     * @throws MessagingException
     */
    public static Folder getDeletedMessagesFolderWithOpen(MailAccountCenter accountInfo) throws MessagingException {
        Store store = accountInfo.getStoreWithConnected();
        Folder folder = null;
        //////////////////////////////////////
        //尝试性解决不同邮箱文件夹不一致问题
        //////////////////////////////////////
        try {
            /*尝试使用"已删除"来获取*/
            folder = store.getFolder("已删除");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            try {
                /*尝试使用"Deleted Messages"来获取*/
                folder = store.getFolder("Deleted Messages");
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException e1) {
                /*尝试使用"已删除邮件"来获取*/
                folder = store.getFolder("已删除邮件");
                folder.open(Folder.READ_WRITE);
            }
        }
        return folder;
    }

    /**
     * 获取草稿箱文件夹
     *
     * @param accountInfo
     * @return
     * @throws MessagingException
     */
    public static Folder getDraftsFolderWithOpen(MailAccountCenter accountInfo) throws MessagingException {
        Store store = accountInfo.getStoreWithConnected();
        Folder folder = null;
        //////////////////////////////////////
        //尝试性解决不同邮箱文件夹不一致问题
        //////////////////////////////////////
        try {
            /*尝试使用"草稿箱"来获取*/
            folder = store.getFolder("草稿箱");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            try {
                /*尝试使用"Drafts"来获取*/
                folder = store.getFolder("Drafts");
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException e1) {
                /*尝试使用"草稿夹"来获取*/
                folder = store.getFolder("草稿夹");
                folder.open(Folder.READ_WRITE);
            }
        }

        return folder;
    }


    /**
     * 获取收件箱文件夹
     *
     * @param accountInfo
     * @return
     * @throws MessagingException
     */
    public static Folder getInboxFolderWithOpen(MailAccountCenter accountInfo) throws MessagingException {
        Store store = accountInfo.getStoreWithConnected();
        Folder folder = null;
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);

        return folder;
    }


    /**
     * 获取垃圾邮件文件夹
     *
     * @param accountInfo
     * @return
     * @throws MessagingException
     */
    public static Folder getJunkFolderWithOpen(MailAccountCenter accountInfo) throws MessagingException {
        Store store = accountInfo.getStoreWithConnected();
        Folder folder = null;
        //////////////////////////////////////
        //尝试性解决不同邮箱文件夹不一致问题
        //////////////////////////////////////
        try {
            /*尝试使用"垃圾邮件"来获取*/
            folder = store.getFolder("垃圾邮件");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            /*尝试使用"Junk"来获取*/
            folder = store.getFolder("Junk");
            folder.open(Folder.READ_WRITE);
        }
        return folder;
    }


    /**
     * 获取已发送文件夹
     *
     * @param accountInfo
     * @return
     * @throws MessagingException
     */
    public static Folder getSentMessagesFolderWithOpen(MailAccountCenter accountInfo) throws MessagingException {
        Store store = accountInfo.getStoreWithConnected();
        Folder folder = null;
        //////////////////////////////////////
        //尝试性解决不同邮箱文件夹不一致问题
        //////////////////////////////////////
        try {
            /*尝试使用"已发送"来获取*/
            folder = store.getFolder("已发送");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            try {
                /*尝试使用"Sent Messages"来获取*/
                folder = store.getFolder("Sent Messages");
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException e1) {
                /*尝试使用"已发邮件"来获取*/
                folder = store.getFolder("已发邮件");
                folder.open(Folder.READ_WRITE);
            }
        }

        return folder;
    }

}
