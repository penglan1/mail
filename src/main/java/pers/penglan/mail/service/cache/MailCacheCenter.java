package pers.penglan.mail.service.cache;

import pers.penglan.mail.model.InboxMailBaseInfoVO;

import java.util.List;
import java.util.Set;

/**
 * 邮件缓存中心
 *
 * <p>统一管理所有的缓存，每个邮件账户单独拥有一个缓存中心</p>
 *
 * @Author PENGL
 * 2020-03-14
 */
public class MailCacheCenter {

    public Set<String> getAllMsgUID() {
        return null;
    }

    public List<InboxMailBaseInfoVO> getMailBaseInfoVOList(Set<String> msgUIDExcluded) {
        return null;
    }
}
