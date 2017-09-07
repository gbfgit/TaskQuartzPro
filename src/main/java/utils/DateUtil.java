package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateString(){
        return sdf.format(new Date());
    }

    public static String getLocalDateString(String jobName){
        return "当前时间是：" + sdf.format(new Date()) + " ★★★ 任务【" + jobName + "】在执行...";
    }
}
