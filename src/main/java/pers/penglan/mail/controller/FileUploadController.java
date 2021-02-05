package pers.penglan.mail.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.service.conmmon.FileSavingManager;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.flags.MailFlag;
import pers.penglan.mail.utils.id.UIDCenter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author PENGL
 * 2020-04-08
 */
@Controller
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private Logger logger;

    /**
     * 上传的图片文件会暂时的保存到统一指定目录 {@link FileSavingManager#FILE_SAVING_DIRECTORY} 下。
     *
     * <p>图片文件成功处理以后，会产生对应的{@link AttachmentVO} 对象，该对象会临时的保存到当前用户的
     * session属性中，其对应的键为{@link UserSessionOperator#USER_TEMP_ATTACH_KEY}，保存时的操作应当
     * 统一调用{@link UserSessionOperator} 来完成</p>
     *
     * <p>用户session在失效时，应当检查其中是否有{@link AttachmentVO} 对象，如果有，则应当将对象关联的附件
     * 进行删除，该任务被放置于{@link pers.penglan.mail.controller.session.CommonSessionProcessorListener}
     * 中完成</p>
     *
     * @param item
     * @param model
     * @return
     */
    @RequestMapping("/process/attachment")
    @ResponseBody
    public Object processAttach(@RequestParam("item") MultipartFile item, ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            String attachSubDirectory = "session/attachment";
            String originFilename = item.getOriginalFilename();
            /*获取文件扩展名*/
            int dotIndex = originFilename.lastIndexOf(".");
            String extension = originFilename.substring(dotIndex);

            String relativePath = FileSavingManager.savingFile(item.getInputStream(), attachSubDirectory, extension);

            AttachmentVO attachment = new AttachmentVO();
            attachment.setFilename(originFilename);
            attachment.setRelativePath(relativePath);
            attachment.setContentDisposition(MailFlag.ATTACHMENT);
            attachment.setPk_attachment(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            /*创建map*/
            Map<String, AttachmentVO> attachmentVOMap = new HashMap<>();
            attachmentVOMap.put(attachment.getPk_attachment(), attachment);
            /*放入到session中*/
            UserSessionOperator.addUserTempAttachMap(request.getSession(false), attachmentVOMap);

            /*将数据返回给客户*/
            map.put("status", "success");
            map.put("message", "附件上传成功");
            map.put("attachment", attachment);
            String owner = UserSessionOperator.getUserGlobalInfo(request.getSession()).getCurrentMailAccountCenter().getOwner();
            String url = MailUtilityController.createAttachURL(attachment.getRelativePath(), owner,
                    attachment.getPk_attachment(), attachment.getContentDisposition());
            map.put("url", url);

            /*将附件pk加入到可访问集合中*/
            UserSessionOperator.addPk_AttachmentForDisplayToUser(request.getSession(), attachment.getPk_attachment());
            /*将附件加到session中进行临时管理，以便最后统一删除*/
            Map<String, AttachmentVO> attachMap = new HashMap<String, AttachmentVO>();
            attachMap.put(attachment.getPk_attachment(), attachment);
            UserSessionOperator.addUserTempAttachMap(request.getSession(), attachMap);
        } catch (IOException e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "附件处理失败");
        }

        return map;
    }

    @RequestMapping("/process/inline")
    @ResponseBody
    public Object processInline(@RequestParam("item") MultipartFile item, ModelMap model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            String attachSubDirectory = "session/inline";
            String originFilename = item.getOriginalFilename();
            /*获取文件扩展名*/
            int dotIndex = originFilename.lastIndexOf(".");
            String extension = originFilename.substring(dotIndex);

            String relativePath = FileSavingManager.savingFile(item.getInputStream(), attachSubDirectory, extension);

            AttachmentVO attachment = new AttachmentVO();
            attachment.setFilename(originFilename);
            attachment.setRelativePath(relativePath);
            attachment.setContentDisposition(MailFlag.INLINE);
            attachment.setContentID("<" + Long.toString(UIDCenter.UID_GENERATOR.nextId()) + ">");
            attachment.setPk_attachment(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
            /*创建map*/
            Map<String, AttachmentVO> attachmentVOMap = new HashMap<>();
            attachmentVOMap.put(attachment.getPk_attachment(), attachment);
            /*放入到session中*/
            UserSessionOperator.addUserTempAttachMap(request.getSession(false), attachmentVOMap);

            /*将数据返回给客户*/
            map.put("status", "success");
            map.put("message", "附件上传成功");
            map.put("attachment", attachment);
            String owner = UserSessionOperator.getUserGlobalInfo(request.getSession()).getCurrentMailAccountCenter().getOwner();
            String url = MailUtilityController.createAttachURL(attachment.getRelativePath(), owner,
                    attachment.getPk_attachment(), attachment.getContentDisposition());
            map.put("url", url);

            /*将附件pk加入到可访问集合中*/
            UserSessionOperator.addPk_AttachmentForDisplayToUser(request.getSession(), attachment.getPk_attachment());
            /*将附件加到session中进行临时管理，以便最后统一删除*/
            Map<String, AttachmentVO> attachMap = new HashMap<String, AttachmentVO>();
            attachMap.put(attachment.getPk_attachment(), attachment);
            UserSessionOperator.addUserTempAttachMap(request.getSession(), attachMap);
        } catch (IOException e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "附件处理失败");
        }

        return map;
    }
}
