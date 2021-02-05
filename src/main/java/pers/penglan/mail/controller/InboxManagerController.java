package pers.penglan.mail.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.model.InboxMailBaseInfoVO;
import pers.penglan.mail.model.request.RequestOperation;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.inbox.InboxManagerWrap;
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
@RequestMapping(path = "/inbox", method = RequestMethod.POST)
public class InboxManagerController {
    public final static String MAIL_ACCOUNT_INFO = "mailAccountCenter";
    @Autowired
    private InboxManagerWrap inboxManagerWrap;

    @Autowired
    private Logger logger;


    @RequestMapping("/receiveMail")
    @ResponseBody
    public Object receiveMail(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                              ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        Set<String> msgUIDs = UserSessionOperator.getMsgUIDForInbox(request.getSession());
        if (msgUIDs == null) {
            msgUIDs = new HashSet<>();
            UserSessionOperator.addMsgUIDForInbox(request.getSession(), msgUIDs);
        }

        try {
            List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = inboxManagerWrap.receiveMail(mailAccountCenter, msgUIDs);
            map.put("status", "success");
            map.put("message", "邮件更新完成");
            map.put("mailList", inboxMailBaseInfoVOList);
            /*将新接收的邮件的msgUID加入到session中*/
            for (InboxMailBaseInfoVO mailBaseInfo : inboxMailBaseInfoVOList)
                msgUIDs.add(mailBaseInfo.getMsgUID());

        } catch (MessagingException e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "获取邮件失败");
        }



        return map;
    }

    @RequestMapping("/get/inbox")
    @ResponseBody
    public Object getInbox(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model,
                           HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList = inboxManagerWrap.getInbox(mailAccountCenter);
            map.put("status", "success");
            map.put("message", "收件箱数据获取成功");
            map.put("mailList", inboxMailBaseInfoVOList);

            /*将数据的msgUID放到session缓存中*/
            Set<String> msgUIDs = UserSessionOperator.getMsgUIDForInbox(request.getSession());
            if (msgUIDs == null || msgUIDs.size() == 0) {
                if (msgUIDs == null) {
                    msgUIDs = new HashSet<>();
                    UserSessionOperator.addMsgUIDForInbox(request.getSession(), msgUIDs);
                }
                for (InboxMailBaseInfoVO mailBaseInfo : inboxMailBaseInfoVOList)
                    msgUIDs.add(mailBaseInfo.getMsgUID());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "收件箱数据获取出现异常");
        }


        return map;
    }


    @RequestMapping("/delete/mailBaseInfo")
    @ResponseBody
    public Object setDeleteFlag(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                                @ModelAttribute("inboxMailBaseInfoVOList") List<InboxMailBaseInfoVO> listToDelete, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        boolean exceptionFlag = false;

        try {
            inboxManagerWrap.setDeleteFlag(listToDelete, mailAccountCenter);
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "邮件删除失败，请重新刷新数据");
            exceptionFlag = true;
        }

        if (!exceptionFlag) {
            map.put("status", "success");
            map.put("message", "邮件删除成功");
        }

       return map;
    }

    /**
     * 更新收件箱中的邮件基本信息的统一入口
     * @param updateList
     * @param mailAccountCenter
     * @param model
     * @return
     */
    @RequestMapping("/update/mailBaseInfo")
    @ResponseBody
    public Object updateMailBaseInfo(@ModelAttribute("inboxMailBaseInfoVOList") List<InboxMailBaseInfoVO> updateList,
                                     @ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        boolean exceptionFlag = false;

        try {
            inboxManagerWrap.updateMailBaseInfo(updateList, mailAccountCenter);
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "修改同步失败，请重新刷新收件箱中的数据");
            exceptionFlag = true;
        }

        if (!exceptionFlag) {
            map.put("status", "success");
            map.put("message", "修改同步成功");
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

            InboxMailBaseInfoVO[] inboxMailBaseInfoVOS = operation.getInboxMailBaseInfoVOs();
            model.put("inboxMailBaseInfoVOList", Arrays.asList(inboxMailBaseInfoVOS));
        } else if ("getInbox".equalsIgnoreCase(action)) {

        }
    }
}
