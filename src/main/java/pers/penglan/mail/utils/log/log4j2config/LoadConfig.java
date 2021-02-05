package pers.penglan.mail.utils.log.log4j2config;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 当log4j2的配置文件没有放置在默认初始位置时，可以使用此类来告诉log4j2配置文件的位置，并将配置文件传递给log4j2
 * 注意：此工具类的作用主要是先用配置文件初始化log4j2,初始化之后，后续可以直接使用Logger来打印日志
 * 获取Logger的步骤：
 * <pre>
 *     // 第一步，初始化
 *     // 初始化操作在整个项目运行期间只需要调用一次就可以了，除非想要更改使用另一个配置文件，则需要重新调用一次初始化
 *     LoadConfig.init(path);
 *     // 第二部，在使用出获得Logger
 *     Logger logger = LogManager.getLogger(parameter);
 * </pre>
 *
 * @Author PENGL
 * 2020-03-18
 */
public class LoadConfig {
    /*配置文件的位置*/
    private static File configFile = null;

    public synchronized static void init(String path) {
        LoadConfig.configFile = new File(path);
        init01();
    }

    private synchronized static void init01() {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(configFile));
            ConfigurationSource configurationSource = new ConfigurationSource(bufferedInputStream, configFile);
            Configurator.initialize(null, configurationSource);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("日志加载配置文件初始化时失败", e);
        }
    }
}
