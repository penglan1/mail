package pers.penglan.mail.utils.id;

import pers.penglan.mail.utils.id.common.SnowflakeIdWorker;

/**
 * 统一公共使用的ID生成器
 *
 * <p>在模块中，如果没有必要使用专门的ID生成器，均可使用UIDCenter来生成一个全局唯一的ID</p>
 *
 * @Author PENGL
 * 2020-03-15
 */
public class UIDCenter {
    /*此处不可变，每个项目每个模块或者是使用方向的dataCenterID和workerId都是唯一的，由项目管理者负责ID的分配*/
    public static final SnowflakeIdWorker UID_GENERATOR = new SnowflakeIdWorker(0, 0);
}
