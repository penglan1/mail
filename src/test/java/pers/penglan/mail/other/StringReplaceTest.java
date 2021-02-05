package pers.penglan.mail.other;

import org.junit.Test;

/**
 * @Author PENGL
 * 2020-04-07
 */
public class StringReplaceTest {
    @Test
    public void test01() {
        String content = "你好                        " +
                "var url = 'hhttps://dss1.bdstatic.com/70cFvXSh_Q1" +
                "YnxGkpoWK1HF6hhy/it/u=3173584241,353329086" +
                "0&fm=26&gp=0.jpg';\n1https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "2https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "3https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "4" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "5" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "6" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "6" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "7" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "7" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "8" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg" +
                "9" +
                "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3173584241,3533290860&fm=26&gp=0.jpg";
        String change1 = content.replace("https://dss1.bd" +
                "static.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=317358424" +
                "1,3533290860&fm=26&gp=0.jpg", "，summernote");
        System.out.println(change1);
    }
}
