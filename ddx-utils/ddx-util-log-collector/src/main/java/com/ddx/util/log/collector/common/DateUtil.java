package com.ddx.util.log.collector.common;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName: DateUtil
 * @Description: 日期工具类
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public class DateUtil {

    public final static String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_2 = "HH:mm:ss";
    public final static String DATE_FORMAT_1_2 = DATE_FORMAT_1+" "+DATE_FORMAT_2;

    /**
     * 根据Date获取格式化数据
     * @param date date对象
     * @param format 格式
     * @return 格式化后的数据
     */
    public static String dateToStrDate(Date date, String format ){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 根据毫秒数获取格式化数据
     * @param millis 毫秒数
     * @param format 格式
     * @return 格式化后的数据
     */
    public static String millisToStrDate(String millis, String format){
        return dateToStrDate(new Date(Long.valueOf(millis)), format);
    }

    /**
     * 根据毫秒获取日期对象
     * @param millis
     * @return
     */
    public static Date millisToDate(long millis) {
        return new Date(millis);
    }
}