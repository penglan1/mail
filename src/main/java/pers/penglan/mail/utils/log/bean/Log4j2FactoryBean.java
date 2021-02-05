package pers.penglan.mail.utils.log.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 为了配合spring，尽可能将代码解耦，专门设计此类用于生产Logger的bean
 *
 * <p>此类实质上就是一个logger bean工厂</p>
 *
 * <s>使用该bean工厂产生Logger时，则默认使用者已经提前将Log4j2的日志文件初始化好了，因此，使用者
 * 有责任保证此处获取得到的logger是调用者自己初始化好了配置文件的logger，即自己想要的那个logger</s>
 *
 * @Author PENGL
 * 2020-03-25
 */
public class Log4j2FactoryBean {

    private static final String PROJECT_NAME = "mail";

    /**
     * 获取到org.apache.logging.log4j.Logger
     *
     * @param name 目标日志名称
     * @return
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

    public static Logger getLogger() {
        return LogManager.getLogger(PROJECT_NAME);
    }

}
