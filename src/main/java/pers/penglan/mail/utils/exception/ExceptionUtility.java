package pers.penglan.mail.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author PENGL
 * 2020-03-19
 */
public class ExceptionUtility {

    /**
     * 将异常的堆栈信息以字符串的形式返回
     *
     * @param e
     * @return
     */
    public static String getTrace(Exception e) {
        StringWriter stringWriter = new StringWriter(1000);
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        printWriter.flush();

        return stringWriter.toString();
    }
}
