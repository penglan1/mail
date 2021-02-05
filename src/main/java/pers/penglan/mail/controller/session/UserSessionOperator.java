package pers.penglan.mail.controller.session;

import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.RegisterTO;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户session统一操作入口
 *
 * <p>该类的主要目的便是统一操作session中的用户相关信息</p>
 *
 * <p>通过此类获取到的session中的信息，是所有用户登入以后都共有的信息</p>
 *
 * @Author PENGL
 * 2020-03-26
 */
public class UserSessionOperator {
    public static final String USER_GLOBAL_INFO_KEY = "USER_GLOBAL_INFO";

    public static final String USER_TEMP_ATTACH_KEY = "USER_TEMP_ATTACH";

    public static final String PK_ATTACHMENT_FOR_DISPLAY_TO_USER_KEY = "PK_ATTACHMENT_FOR_DISPLAY_TO_USER";
    
    public static final String MSG_UID_FOR_INBOX_KEY = "MSG_UID_FOR_INBOX";
    
    public static final String MSG_UID_FOR_SENT_KEY = "MSG_UID_FOR_SENT";
    
    public static final String MSG_UID_FOR_DELETED_KEY = "MSG_UID_FOR_DELETED";
    
    public static final String MSG_UID_FOR_DRAFT_KEY = "MSG_UID_FOR_DRAFT";
    
    public static final String MSG_UID_FOR_JUNK_KEY = "MSG_UID_FOR_JUNK";

    /*注册对象的key*/
    public static final String REGISTERTO_KEY = "REGISTERTO_KEY";

    /**
     * 获取用户的全局信息
     * @param session
     * @return
     */
    public static UserGlobalInfo getUserGlobalInfo(HttpSession session) {
        return (UserGlobalInfo)session.getAttribute(USER_GLOBAL_INFO_KEY);
    }

    /**
     * 设置用户的全局信息
     * @param session
     * @param userGlobalInfo
     */
    public static void setUserGlobalInfo(HttpSession session, UserGlobalInfo userGlobalInfo) {
        session.setAttribute(UserSessionOperator.USER_GLOBAL_INFO_KEY, userGlobalInfo);
    }

    /**
     * 添加临时附件对象
     * @param session
     * @param attachMap
     */
    public static void addUserTempAttachMap(HttpSession session, Map<String, AttachmentVO> attachMap) {
        if (session.getAttribute(USER_TEMP_ATTACH_KEY) == null) {
            session.setAttribute(USER_TEMP_ATTACH_KEY, new HashMap<String, AttachmentVO>());
        }
        Map<String, AttachmentVO> map = (Map<String, AttachmentVO>) session.getAttribute(USER_TEMP_ATTACH_KEY);
        map.putAll(attachMap);
    }

    /**
     * 获取临时的附件对象
     * @param session
     * @return
     */
    public static Map<String, AttachmentVO> getUserTempAttachMap(HttpSession session) {
        return (Map<String, AttachmentVO>) session.getAttribute(USER_TEMP_ATTACH_KEY);
    }

    /**
     * 添加附件{@link AttachmentVO} 的主键，用于接下来用户访问附件时的安全验证
     * @param session
     * @param pk_Attachment
     */
    public static void addPk_AttachmentForDisplayToUser(HttpSession session, String pk_Attachment) {
        if (session.getAttribute(PK_ATTACHMENT_FOR_DISPLAY_TO_USER_KEY) == null) {
            session.setAttribute(PK_ATTACHMENT_FOR_DISPLAY_TO_USER_KEY, new HashSet<String>());
        }

        Set<String> pkSet = (Set<String>)session.getAttribute(PK_ATTACHMENT_FOR_DISPLAY_TO_USER_KEY);
        pkSet.add(pk_Attachment);
    }

    /**
     * 返回用户可访问的附件的PK集合
     * @param session
     * @return
     */
    public static Set<String> getPk_AttachmentForDisplayToUser(HttpSession session) {
        return (Set<String>)session.getAttribute(PK_ATTACHMENT_FOR_DISPLAY_TO_USER_KEY);
    }

    /**
     * 缓存已经收取的收件箱里面的邮件的msgUID
     * @param session
     * @param msgUID
     */
    public static void addMsgUIDForInbox(HttpSession session, Set<String> msgUID) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_INBOX_KEY) == null) {
            map.put(MSG_UID_FOR_INBOX_KEY, new HashSet<String>());
        }

        Set<String> msgSet = (Set<String>) map.get(MSG_UID_FOR_INBOX_KEY);
        msgSet.addAll(msgSet);
    }
    
    public static Set<String> getMsgUIDForInbox(HttpSession session) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_INBOX_KEY) == null) {
            map.put(MSG_UID_FOR_INBOX_KEY, new HashSet<String>());
        }

        return (Set<String>) map.get(MSG_UID_FOR_INBOX_KEY);
    }

    public static void addMsgUIDForSent(HttpSession session, Set<String> msgUID) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_SENT_KEY) == null) {
            map.put(MSG_UID_FOR_SENT_KEY, new HashSet<String>());
        }

        Set<String> msgSet = (Set<String>)map.get(MSG_UID_FOR_SENT_KEY);
        msgSet.addAll(msgSet);
    }

    public static Set<String> getMsgUIDForSent(HttpSession session) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_SENT_KEY) == null) {
            map.put(MSG_UID_FOR_SENT_KEY, new HashSet<String>());
        }

        return (Set<String>)map.get(MSG_UID_FOR_SENT_KEY);
    }

    public static void addMsgUIDForDeleted(HttpSession session, Set<String> msgUID) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_DELETED_KEY) == null) {
            map.put(MSG_UID_FOR_DELETED_KEY, new HashSet<String>());
        }

        Set<String> msgSet = (Set<String>)map.get(MSG_UID_FOR_DELETED_KEY);
        msgSet.addAll(msgSet);
    }

    public static Set<String> getMsgUIDForDeleted(HttpSession session) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_DELETED_KEY) == null) {
            map.put(MSG_UID_FOR_DELETED_KEY, new HashSet<String>());
        }

        return (Set<String>)map.get(MSG_UID_FOR_DELETED_KEY);
    }

    public static void addMsgUIDForJunk(HttpSession session, Set<String> msgUID) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_JUNK_KEY) == null) {
            map.put(MSG_UID_FOR_JUNK_KEY, new HashSet<String>());
        }

        Set<String> msgSet = (Set<String>)map.get(MSG_UID_FOR_JUNK_KEY);
        msgSet.addAll(msgSet);
    }

    public static Set<String> getMsgUIDForJunk(HttpSession session) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_JUNK_KEY) == null) {
            map.put(MSG_UID_FOR_JUNK_KEY, new HashSet<String>());
        }
        return (Set<String>)map.get(MSG_UID_FOR_JUNK_KEY);
    }

    public static void addMsgUIDForDraft(HttpSession session, Set<String> msgUID) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_DRAFT_KEY) == null) {
            map.put(MSG_UID_FOR_DRAFT_KEY, new HashSet<String>());
        }

        Set<String> msgSet = (Set<String>)map.get(MSG_UID_FOR_DRAFT_KEY);
        msgSet.addAll(msgSet);
    }

    public static Set<String> getMsgUIDForDraft(HttpSession session) {
        String owner = getUserGlobalInfo(session).getCurrentMailAccountCenter().getOwner();
        Map<String, Object> map = null;
        if (session.getAttribute(owner) == null) {
            map = new HashMap<String, Object>();
            session.setAttribute(owner, map);
        } else {
            map = (Map<String, Object>) session.getAttribute(owner);
        }

        if (map.get(MSG_UID_FOR_DRAFT_KEY) == null) {
            map.put(MSG_UID_FOR_DRAFT_KEY, new HashSet<String>());
        }
        return (Set<String>)map.get(MSG_UID_FOR_DRAFT_KEY);
    }


    /**
     * 向session中加入注册对象
     * @param session
     * @param registerTO
     */
    public static void addRegisterTO(HttpSession session, RegisterTO registerTO) {
        session.setAttribute(REGISTERTO_KEY, registerTO);
    }

    /**
     * 获取session中的注册对象
     * @param session
     * @return
     */
    public static RegisterTO getRegisterTO(HttpSession session) {
        return (RegisterTO) session.getAttribute(REGISTERTO_KEY);
    }



}
