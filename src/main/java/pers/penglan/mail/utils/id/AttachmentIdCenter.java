package pers.penglan.mail.utils.id;

import pers.penglan.mail.utils.id.common.SnowflakeIdWorker;

/**
 * 专门用于附件名称生成的ID生成器
 *
 * @Author PENGL
 * 2020-03-15
 */
public class AttachmentIdCenter {
    /*此处不可变，每个项目每个模块或者是使用方向的dataCenterID和workerId都是唯一的，由项目管理者负责ID的分配*/
    public static final SnowflakeIdWorker FILENAME_UID_GENERATOR = new SnowflakeIdWorker(1, 0);
}
