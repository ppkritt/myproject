package util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    //时间格式
    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";


    //单位
    public static String parseSize(long size) {
        String[] danwei = {"B","KB","MB","GB","PB","TB"};
        int index = 0;
        while(size > 1024 && index < danwei.length - 1){
            size /= 1024;
            index++;
        }
        return  size + danwei[index];
    }

    /**
     * 解析为中文时间
     * @param lastModified
     * @return
     */
    public static String parseDate(Date lastModified) {
        return new SimpleDateFormat(DATA_PATTERN).format(lastModified);
    }
    public static void main(String[] args) {

        System.out.println(parseSize(100_000_000_000_000L));
        System.out.println(parseDate(new Date()));
    }

}
