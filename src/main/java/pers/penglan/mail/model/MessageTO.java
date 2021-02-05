package pers.penglan.mail.model;

import javax.mail.Message;

/**
 * 数据传输对象，用于{@link pers.penglan.mail.service.conmmon.itf.MailReceiver} 的
 * 相关方法的返回值
 *
 * <p>该输出对象只是增加了messageUID</p>
 *
 * @Author PENGL
 * 2020-03-14
 */
public class MessageTO{
    private Message message;
    private String msgUID;

    public MessageTO(Message message, String msgUID) {
        this.message = message;
        this.msgUID = msgUID;
    }

    public MessageTO() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getMsgUID() {
        return msgUID;
    }

    public void setMsgUID(String msgUID) {
        this.msgUID = msgUID;
    }
}
