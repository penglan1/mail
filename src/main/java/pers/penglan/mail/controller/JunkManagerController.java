package pers.penglan.mail.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.model.JunkMailBaseInfoVO;
import pers.penglan.mail.model.request.RequestOperation;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.junk.JunkManagerWrap;
import pers.penglan.mail.utils.exception.ExceptionUtility;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author PENGL
 * 2020-03-27
 */
@Controller
@RequestMapping(path = "/junk", method = RequestMethod.POST)
public class JunkManagerController {
    public final static String MAIL_ACCOUNT_INFO = "mailAccountCenter";
    public final static String MSG_UIDS_TO_DELETE = "msgUIDsToDelete";
    public final static String MESSAGE_SOURCE = "messageSource";

    @Autowired
    private JunkManagerWrap junkManagerWrap;

    @Autowired
    private Logger logger;

    @RequestMapping("/get/junks")
    @ResponseBody
    public Object getJunk(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                          ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        List<JunkMailBaseInfoVO> junkMailBaseInfoVOList = junkManagerWrap.getJunk(mailAccountCenter);
        map.put("status", "success");
        map.put("message", "垃圾邮件数据获取成功");
        map.put("mailList", junkMailBaseInfoVOList);

        /*将数据的msgUID放到session缓存中*/
        Set<String> msgUIDs = UserSessionOperator.getMsgUIDForJunk(request.getSession());
        if (msgUIDs == null || msgUIDs.size() == 0) {
            if (msgUIDs == null) {
                msgUIDs = new HashSet<>();
                UserSessionOperator.addMsgUIDForJunk(request.getSession(), msgUIDs);
            }
            for (JunkMailBaseInfoVO mailBaseInfo : junkMailBaseInfoVOList)
                msgUIDs.add(mailBaseInfo.getMsgUID());
        }

        return map;
    }

    @RequestMapping("/receiveJunk")
    @ResponseBody
    public Object receiveJunk(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                              ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        Set<String> msgUIDs = UserSessionOperator.getMsgUIDForJunk(request.getSession());
        if (msgUIDs == null) {
            msgUIDs = new HashSet<>();
            UserSessionOperator.addMsgUIDForJunk(request.getSession(), msgUIDs);
        }

        try {
            List<JunkMailBaseInfoVO> junkMailBaseInfoVOList = junkManagerWrap.receiveJunk(mailAccountCenter, msgUIDs);
            map.put("status", "success");
            map.put("message", "邮件更新完成");
            map.put("mailList", junkMailBaseInfoVOList);

            /*将新接收的邮件的msgUID加入到session中*/
            for (JunkMailBaseInfoVO mailBaseInfo : junkMailBaseInfoVOList)
                msgUIDs.add(mailBaseInfo.getMsgUID());

        } catch (MessagingException e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "接收垃圾邮件过程中出现错误");
        }

        return map;
    }

    @RequestMapping("/delete/mail")
    @ResponseBody
    public Object setDeleteFlag(@ModelAttribute("junkMailBaseInfoVOList") List<JunkMailBaseInfoVO> listToDelete,
                                @ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            junkManagerWrap.setDeleteFlag(listToDelete, mailAccountCenter);
            map.put("status", "success");
            map.put("message", "邮件删除成功");
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "邮件删除失败，请重新刷新数据");
        }

        return map;
    }

    @RequestMapping("/update/mailBaseInfo")
    @ResponseBody
    public Object updateMailBaseInfo(@ModelAttribute("junkMailBaseInfoVOList") List<JunkMailBaseInfoVO> updateList,
                                     @ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            junkManagerWrap.updateMailBaseInfo(updateList, mailAccountCenter);
            map.put("status", "success");
            map.put("message", "修改同步成功");
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "修改同步失败，请重新刷新收件箱中的数据");
        }

        return map;
    }

    @ModelAttribute
    public void beforeOperation(@RequestBody RequestOperation operation,
                                HttpServletRequest request, ModelMap model) {
        HttpSession session = request.getSession(false);
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(session);
        /*获取账号操作对象*/
        String owner = operation.getOwner();
        /*检验该账号是否存在*/
        if (!userGlobalInfo.checkAccount(owner)) {
            model.addAttribute("status", "failure");
            if (owner == null || "".equals(owner))
                model.put("message", "请选择操作的邮箱账号，或者新增邮箱");
            else
                model.put("message", "当前用户不存在该邮箱账号"  + owner);

            return;
        } else {
            model.addAttribute("status", "success");
        }

        MailAccountCenter mailAccountCenter = userGlobalInfo.getMailAccountInfo(owner);
        model.addAttribute(MAIL_ACCOUNT_INFO, mailAccountCenter);

        String action = operation.getAction();

        if ("receiveMail".equalsIgnoreCase(action)) {

        } else if ("deleteMailBaseInfo".equalsIgnoreCase(action)
                || "updateMailBaseInfo".equalsIgnoreCase(action)) {

            JunkMailBaseInfoVO[] junkMailBaseInfoVOS = operation.getJunkMailBaseInfoVOs();
            model.put("junkMailBaseInfoVOList", Arrays.asList(junkMailBaseInfoVOS));
        } else if ("getJunk".equalsIgnoreCase(action)) {

        }

    }
}
