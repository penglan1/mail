package pers.penglan.mail.service.conmmon;

import pers.penglan.mail.utils.id.AttachmentIdCenter;
import pers.penglan.mail.utils.id.common.SnowflakeIdWorker;

import java.io.*;
import java.util.Properties;

/**
 * @Author PENGL
 * 2020-03-15
 */
public class FileSavingManager {
    /*此处不可变，每个项目每个模块或者是使用方向的dataCenterID和workerId都是唯一的，由项目管理者负责ID的分配*/
    private static final SnowflakeIdWorker FILENAME_UID_GENERATOR = AttachmentIdCenter.FILENAME_UID_GENERATOR;

    /*文件保存的统一根目录*/
    public static final String FILE_SAVING_DIRECTORY;
    static{
        Properties properties = new Properties();
        try {
            properties.load(FileSavingManager.class.getClassLoader().getResourceAsStream("setting/AttachmentsPath.properties"));
            FILE_SAVING_DIRECTORY = properties.getProperty("mail.attachment.path");
        } catch (IOException e) {
            throw new RuntimeException("加载附件存放位置的配置文件时出现错误");
        }
    }

    private FileSavingManager() {}

    /**
     * 将流中的字节保存到指定的目录下面
     *
     * <p>最终文件的保存目录为：
     * <pre>
     *     String finalDir = FILE_SAVING_DIRECTORY + "/" + subDir;
     * </pre></p>
     *
     * @param inputStream 需要保存的内容的字节流
     * @param subDir 文件保存的子目录
     *               <p>错误格式："/xxx/" 或者 "/xxx" 或者 "xxx/"</p>
     *               <p>正确格式：null 或者 "" 或者 "xxx" 或者 "xxx/xxx/...../xxx"</p>
     * @param fileExtension 文件扩张名 正确格式：".xxx"
     * @return 返回文件的相对路径，其相对的目录是{@link FileSavingManager#FILE_SAVING_DIRECTORY}
     */
    public static String savingFile(InputStream inputStream, String subDir, String fileExtension) throws IOException {
        String finalDire;
        if (subDir == null)
            subDir = "";
        if ("".equals(subDir)) {
            finalDire = FILE_SAVING_DIRECTORY;
        } else {
            finalDire = FILE_SAVING_DIRECTORY + "/" + subDir;
        }

        /*判断是否需要创建目标目录*/
        File goalDir = new File(finalDire);
        if (!goalDir.exists())
            goalDir.mkdirs();

        String filename = Long.toString(FILENAME_UID_GENERATOR.nextId());
        /*判断是否需要指定文件扩展名*/
        if (fileExtension != null)
            filename = filename + fileExtension;

        /*文件最终保存的位置*/
        String location = finalDire + "/" + filename;
        File goalFile = new File(location);
        if (!goalFile.exists())
            goalFile.createNewFile();

        /*保存数据到文件中*/
        BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(goalFile));
        BufferedInputStream bufferedIn = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = bufferedIn.read(bytes, 0, bytes.length)) != -1) {
            bufferedOut.write(bytes, 0, len);
        }
        /*关闭资源*/
        bufferedOut.flush();
        bufferedOut.close();
        bufferedIn.close();

        /*待返回的相对路径*/
        String relativePath = subDir + "/" + filename;

        return relativePath;
    }

}
