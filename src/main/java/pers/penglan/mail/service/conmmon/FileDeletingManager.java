package pers.penglan.mail.service.conmmon;

import pers.penglan.mail.model.AttachmentVO;

import java.io.File;

/**
 * @Author PENGL
 * 2020-03-27
 */
public class FileDeletingManager {
    public static final String FILE_DIRECTORY = FileSavingManager.FILE_SAVING_DIRECTORY;

    public static void deleteAttachment(AttachmentVO attachmentVO) {
        String filePath = FILE_DIRECTORY + "/" + attachmentVO.getRelativePath();
        File file = new File(filePath);
        if (file.exists())
            file.delete();
    }
}
