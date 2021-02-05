package pers.penglan.mail.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.mapper.AttachmentMapper;
import pers.penglan.mail.mapper.MailEntityMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.request.RequestOperation;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.MailAccountCenter;
import pers.penglan.mail.service.conmmon.FileSavingManager;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.flags.MailFlag;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 处理邮件的一些常用请求
 *
 * @Author PENGL
 * 2020-04-03
 */
@Controller
@RequestMapping(path = "/common", method = {RequestMethod.POST, RequestMethod.GET})
public class MailUtilityController {
    public final static String MAIL_ACCOUNT_INFO = "mailAccountCenter";

    @Autowired
    private MailEntityMapper mailEntityMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private Logger logger;

    /**
     * 获取邮件实体
     * @param pk_MailEntity
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/get/mailEntity")
    @ResponseBody
    public Object getMailEntity(@ModelAttribute("pk_MailEntity") String pk_MailEntity, HttpServletRequest request, ModelMap model) {
        Map<String, Object> map = new HashMap<>();

        if ("failure".equals(model.get("status"))) {
            map.put("status", model.get("status"));
            map.put("message", model.get("message"));
            return map;
        }

        MailEntityVO mailEntityVO = mailEntityMapper.selectByPk_MailEntity(pk_MailEntity);
        List<AttachmentVO> attachmentVOList = attachmentMapper.selectByPk_MailEntity(pk_MailEntity);

        if (mailEntityVO == null) {
            map.put("status", "failure");
            map.put("message", "ID为" + pk_MailEntity + "的邮件不存在");
            return map;
        }

        /*处理邮件正文中的inline引用*/
        String content = mailEntityVO.getContent();
        List<Map<String, String>> attachUrlList = new ArrayList<>();
        for (AttachmentVO attachment : attachmentVOList) {
            if (MailFlag.INLINE.equals(attachment.getContentDisposition())) {
                String contentID = attachment.getContentID();
                /*去除两边的尖括号*/
                if (contentID.indexOf("<") == 0 && contentID.lastIndexOf(">") == (contentID.length() - 1))
                    contentID = contentID.substring(1, contentID.length() - 1);
                /*img的固定形式：<img src="/mail/common/get/img?relativePath=xxx/xxx/xx.[png/jpg/gif]&flag=img"/>*/
                String url = createAttachURL(attachment.getRelativePath(), mailEntityVO.getOwner(), attachment.getPk_attachment(), MailFlag.INLINE);
                content = content.replace("cid:" + contentID, url);
                UserSessionOperator.addPk_AttachmentForDisplayToUser(request.getSession(), attachment.getPk_attachment());
            } else {
                String url = createAttachURL(attachment.getRelativePath(), mailEntityVO.getOwner(),
                        attachment.getPk_attachment(), attachment.getContentDisposition(), attachment.getFilename());
                Map<String, String> urlMap = new HashMap<>();
                urlMap.put("filename", attachment.getFilename());
                urlMap.put("url", url);
                urlMap.put("pk_attachment", attachment.getPk_attachment());
                /*添加url集合*/
                attachUrlList.add(urlMap);
                UserSessionOperator.addPk_AttachmentForDisplayToUser(request.getSession(), attachment.getPk_attachment());
            }
        }
        mailEntityVO.setContent(content);

        map.put("status", "success");
        map.put("message", "邮件获取成功");
        map.put("mailEntity", mailEntityVO);
        map.put("attachmentList", attachmentVOList);
        map.put("attachUrlList", attachUrlList);

        return map;
    }

    /**
     * 获取附件内容流
     *
     * @param relativePath
     * @param request
     */
    @RequestMapping(value = "/get/attachment")
    public void getAttachment(String relativePath, String pk_attachment, HttpServletRequest request,
                       HttpServletResponse response, String disposition, String filename) {
        //验证是否自己的邮件账号访问，防止访问其用户的邮件账号中的附件
        Set<String> pkSet = UserSessionOperator.getPk_AttachmentForDisplayToUser(request.getSession());
        if (pkSet == null || !pkSet.contains(pk_attachment))
            return;

        String goalPath = FileSavingManager.FILE_SAVING_DIRECTORY + "/" + relativePath;
        File file = new File(goalPath);
        if (!file.exists()) {
            // 返回一个提示的图片
            return;
        }

        /*设置响应头*/
        if (disposition != null && disposition.equals(MailFlag.ATTACHMENT)) {
            try {
                filename = filename == null ? Long.toString(UIDCenter.UID_GENERATOR.nextId()) : filename;
                String encodeFilename = URLEncoder.encode(filename, "UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeFilename);
            } catch (UnsupportedEncodingException e) {
                logger.error(ExceptionUtility.getTrace(e));
            }
        }

        try {

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            response.setBufferSize(1024 * 200);
            OutputStream out = response.getOutputStream();
            byte[] bytes = new byte[1024 * 10];
            int len = -1;
            while ((len = bufferedInputStream.read(bytes, 0, bytes.length)) != -1) {
                out.write(bytes, 0, len);
            }

            out.flush();

        } catch (FileNotFoundException e) {
            logger.error(ExceptionUtility.getTrace(e));
        } catch (IOException e) {
            logger.error(ExceptionUtility.getTrace(e));
        }

    }

    @ModelAttribute
    public void beforeOperation(@RequestBody(required = false) RequestOperation operation,
                                HttpServletRequest request, ModelMap model) {
        /*处理get方法的请求，防止报错*/
        if (operation == null) {
            return;
        }

        /*获取账号操作对象*/
        String owner = operation.getOwner();
        HttpSession session = request.getSession(false);
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(session);
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

        if ("getMailEntity".equalsIgnoreCase(action)) {
            model.put("pk_MailEntity", operation.getMailEntityVO().getPk_mailEntity());
        }

    }

    /**
     * 产生附件的URL
     * @param relativePath 附件的相对子路径
     * @param owner 附件所属邮箱账号
     * @param pk_attachment 附件对应对象{@link AttachmentVO} 的主键
     * @return url
     */
    public static String createAttachURL(String relativePath, String owner, String pk_attachment, String disposition) {
        String url = "/mail/common/get/attachment?relativePath=" + relativePath + "" +
                "&flag=img" +
                "&mailAccount=" + owner + "" +
                "&pk_attachment=" + pk_attachment + "" +
                "&disposition=" + disposition;

        return url;
    }

    public static String createAttachURL(String relativePath, String owner, String pk_attachment, String disposition, String filename) {
        String url = "/mail/common/get/attachment?relativePath=" + relativePath + "" +
                "&flag=img" +
                "&mailAccount=" + owner + "" +
                "&pk_attachment=" + pk_attachment + "" +
                "&disposition=" + disposition + "" +
                "&filename=" + filename;

        return url;
    }
}
