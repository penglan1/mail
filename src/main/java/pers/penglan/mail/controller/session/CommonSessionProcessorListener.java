package pers.penglan.mail.controller.session;

import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.service.conmmon.FileDeletingManager;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * @Author PENGL
 * 2020-04-01
 */
public class CommonSessionProcessorListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Map<String, AttachmentVO> attachMap = UserSessionOperator.getUserTempAttachMap(session);
        if (attachMap != null) {
            for (AttachmentVO attach : attachMap.values()) {
                FileDeletingManager.deleteAttachment(attach);
            }
        }
    }
}
