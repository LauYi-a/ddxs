package com.ddx.util.log.mc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName: DateUtil
 * @Description: 日期工具类
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public class EsDateUtil {

    public final static String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_2 = "HH:mm:ss";
    public final static String DATE_FORMAT_1_2 = DATE_FORMAT_1+" "+DATE_FORMAT_2;

    /**
     * 根据Date获取格式化数据
     * @param date date对象
     * @param format 格式
     * @return 格式化后的数据
     */
    public static String date2Str(Date date, String format ){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}