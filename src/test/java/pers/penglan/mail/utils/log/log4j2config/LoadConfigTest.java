package pers.penglan.mail.utils.log.log4j2config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author PENGL
 * 2020-03-18
 */
public class LoadConfigTest {
    private Logger logger;

    @Before
    public void init() {
        LoadConfig.init("target/classes/log/config/Log4j2.xml");
        logger = LogManager.getLogger("mail");
    }

    @Test
    public void testLog() {
        logger.trace("这个是trace级别的输出");
        logger.debug("这个是debug级别的输出");
        logger.info("这个是info级别的输出");
        logger.warn("这个是warn级别的输出");
        logger.error("这个是error级别的输出");
        logger.fatal("这个是fatal级别的输出");
    }
}
