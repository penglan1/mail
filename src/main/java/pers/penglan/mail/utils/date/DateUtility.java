package pers.penglan.mail.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author PENGL
 * 2020-03-26
 */
public class DateUtility {

    public static String getCurrentDate() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String currentDate = new SimpleDateFormat(pattern).format(new Date());
        return currentDate;
    }
}
