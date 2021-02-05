package pers.penglan.mail.utils.flags;

/**
 * 邮件标记类
 *
 * @Author PENGL
 * 2020-03-27
 */
public class MailFlag {
    /*未读，已读标记*/
    public static final String READ = "Y";
    public static final String UNREAD = "N";

    /*邮件等级标记*/
    public static final Integer ORDINARY = 0;
    public static final Integer IMPORTANT_1 = 1;
    //...

    /*content-disposition 标记*/
    public static final String ATTACHMENT = "attachment";
    public static final String INLINE = "inline";

    /*邮件发送状态*/
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    /*邮件的contentType*/
    public static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";

}
