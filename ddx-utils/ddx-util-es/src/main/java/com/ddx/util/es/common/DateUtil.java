package com.ddx.util.es.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: DateUtil
 * @Description: 日期工具类
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public class DateUtil {

    /**
     * 日期格式化
     */
    public final static String DATE_FORMAT_1 = "yyyy";
    public final static String DATE_FORMAT_2 = "yyyy-MM";
    public final static String DATE_FORMAT_3 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_4 = "HH:mm:ss";
    public final static String DATE_FORMAT_5 = "yyyy年";
    public final static String DATE_FORMAT_6 = "yyyy年MM月";
    public final static String DATE_FORMAT_7 = "yyyy年MM月dd日";
    public final static String DATE_FORMAT_8 = "HH时mm分ss秒";
    public final static String DATE_FORMAT_9 = "yyyyMM";
    public final static String DATE_FORMAT_10 = "yyyyMMdd";


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

    /**
     * 根据毫秒数获取格式化数据
     * @param millis 毫秒数
     * @param format 格式
     * @return 格式化后的数据
     */
    public static String millis2Str(String millis, String format){
        return date2Str(new Date(Long.valueOf(millis)), format);
    }

    /**
     * 根据Calendar获取格式化数据
     * @param calendar Calendar对象
     * @param format 格式
     * @return 格式化后的数据
     */
    public static String calendar2Str(Calendar calendar, String format){
        return date2Str(new Date(calendar.getTimeInMillis()), format);
    }

    /**
     * 根据格式化数据获取Date对象
     * @param str 格式化数据
     * @param format 格式
     * @param defaultValue 在转换Date对象失败时返回默认Date对象
     * @return 在转换Date对象失败时返回默认Date对象
     */
    public static Date str2Date(String str, String format, Date defaultValue){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

}