package pers.penglan.mail.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.mapper.SentMailBaseInfoMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.model.SentMailBaseInfoVO;
import pers.penglan.mail.model.request.RequestOperation;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.outbox.OutboxManagerWrap;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.flags.MailFlag;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author PENGL
 * 2020-03-25
 */
@Controller
@RequestMapping(path = "/outbox", method = RequestMethod.POST)
public class OutboxManagerController {
    public final static String MAIL_ACCOUNT_INFO = "mailAccountCenter";
    public final static String MESSAGE_SOURCE = "messageSource";

    @Autowired
    private Logger logger;

    @Autowired
    private OutboxManagerWrap outboxManagerWrap;

    @Autowired
    private SentMailBaseInfoMapper sentMailBaseInfoMapper;

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @RequestMapping("/get/sentFailureMail")
    @ResponseBody
    public List<SentMailBaseInfoVO> getSentFailureMessages(
            @ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model) {
        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = outboxManagerWrap.getSentFailureMessages(mailAccountCenter);
        return sentMailBaseInfoVOList;
    }

    @RequestMapping("/get/receiveMail")
    @ResponseBody
    public Object receiveSentMail(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                                  ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        Set<String> msgUIDs = UserSessionOperator.getMsgUIDForSent(request.getSession());
        if (msgUIDs == null) {
            msgUIDs = new HashSet<>();
            UserSessionOperator.addMsgUIDForSent(request.getSession(), msgUIDs);
        }

        try {
            List<SentMailBaseInfoVO> sentMailBaseInfoVOList = outboxManagerWrap.receiveMail(mailAccountCenter, msgUIDs);
            map.put("status", "success");
            map.put("message", "已发送邮件更新成功");
            map.put("mailList", sentMailBaseInfoVOList);

            /*将新接收的邮件的msgUID加入到session中*/
            for (SentMailBaseInfoVO mailBaseInfo : sentMailBaseInfoVOList)
                msgUIDs.add(mailBaseInfo.getMsgUID());

        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "接收邮件过程中出现错误");
        }

        return map;

    }

    @RequestMapping("/get/sentMail")
    @ResponseBody
    public Object getSentMessages(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                                  ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        List<SentMailBaseInfoVO> sentMailBaseInfoVOList = outboxManagerWrap.getSentMessages(mailAccountCenter);
        map.put("status", "success");
        map.put("message", "已发送数据获取成功");
        map.put("mailList", sentMailBaseInfoVOList);

        /*将数据的msgUID放到session缓存中*/
        Set<String> msgUIDs = UserSessionOperator.getMsgUIDForSent(request.getSession());
        if (msgUIDs == null || msgUIDs.size() == 0) {
            if (msgUIDs == null) {
                msgUIDs = new HashSet<>();
                UserSessionOperator.addMsgUIDForSent(request.getSession(), msgUIDs);
            }
            for (SentMailBaseInfoVO mailBaseInfo : sentMailBaseInfoVOList)
                msgUIDs.add(mailBaseInfo.getMsgUID());
        }

        return map;
    }

    @RequestMapping("/delete/mail")
    @ResponseBody
    public Object setDeleteFlag(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                                @ModelAttribute("sentMailBaseInfoVOList") List<SentMailBaseInfoVO> listToDelete, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            outboxManagerWrap.setDeleteFlag(listToDelete, mailAccountCenter);
            map.put("status", "success");
            map.put("message", "邮件删除成功");
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "邮件删除失败，请重新刷新数据");
        }

        return map;
    }

    @RequestMapping("/send/mail")
    @ResponseBody
    public Object sendMail(@ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter,
                           @ModelAttribute(MESSAGE_SOURCE) MessageSource messageSource, ModelMap model,
                           HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            outboxManagerWrap.sendMail(messageSource, mailAccountCenter);
            map.put("status", "success");
            map.put("message", "邮件发送成功");
        } catch (Exception e) {
            logger.info(ExceptionUtility.getTrace(e));
            /*将发送失败的邮件写如数据库*/
            messageSource.getMailEntityVO().setPk_mailEntity(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            SentMailBaseInfoVO sentMailBaseInfoVO = outboxManagerWrap.createFailedSentMailBaseInfoVO(messageSource.getMailEntityVO());
            sentMailBaseInfoVO.setPk_mailEntity(messageSource.getMailEntityVO().getPk_mailEntity());

            sentMailBaseInfoMapper.insertOne(sentMailBaseInfoVO);

            mailEntityMapper.insertOne(messageSource.getMailEntityVO());
            /*将相对应的附加从session中移除，防止被删除了*/
            Map<String, AttachmentVO> attachMap = UserSessionOperator.getUserTempAttachMap(request.getSession());
            for (AttachmentVO attach : messageSource.getAttachmentVOList())
                attachMap.remove(attach.getPk_attachment());

            map.put("status", "failure");
            map.put("message", "邮件发送异常");
        }

        return map;
    }

    /**
     * 更新邮件基本信息
     * @param updateList
     * @param mailAccountCenter
     * @param model
     * @return
     */
    @RequestMapping("/update/mailBaseInfo")
    @ResponseBody
    public Object updateMailBaseInfo(@ModelAttribute("sentMailBaseInfoVOList") List<SentMailBaseInfoVO> updateList,
                                     @ModelAttribute(MAIL_ACCOUNT_INFO) MailAccountCenter mailAccountCenter, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        try {
            outboxManagerWrap.updateMailBaseInfo(updateList, mailAccountCenter);
            map.put("status", "success");
            map.put("message", "修改同步成功");
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "修改同步失败，请重新刷新数据");
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

            SentMailBaseInfoVO[] sentMailBaseInfoVOS = operation.getSentMailBaseInfoVOs();
            model.put("sentMailBaseInfoVOList", Arrays.asList(sentMailBaseInfoVOS));
        } else if ("getSent".equalsIgnoreCase(action)) {

        } else if ("sendMail".equalsIgnoreCase(action)) {
            MailEntityVO mailEntity = operation.getMailEntityVO();
            List<AttachmentVO> attachments = Arrays.asList(operation.getAttachmentVOs());
            /*过滤掉无用的附件，替换内容中的路径为contentID*/
            String content = mailEntity.getContent();
            List<AttachmentVO> finalAttachList = new ArrayList<>();
            for (AttachmentVO attachment : attachments) {
                if (MailFlag.INLINE.equals(attachment.getContentDisposition())) {
                    String url = MailUtilityController.createAttachURL(attachment.getRelativePath(),
                            mailAccountCenter.getOwner(), attachment.getPk_attachment(),
                            attachment.getContentDisposition());
                    /*由于summernote中的&变为了"&amp;"，座椅许需要将url中的&也变为"&amp;*/
                    url = url.replace("&", "&amp;");

                    if (content.contains(url)) {
                        content = content.replace(url, "cid:" + attachment.getContentIDWithoutAngle());
                        finalAttachList.add(attachment);
                    }
                } else {
                    finalAttachList.add(attachment);
                }

            }
            /*重新设置内容*/
            mailEntity.setContent(content);

            MessageSource messageSource = new MessageSource(mailEntity, finalAttachList);
            model.put(MESSAGE_SOURCE, messageSource);
        }

    }
}
