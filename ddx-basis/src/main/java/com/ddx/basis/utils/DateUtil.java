package com.ddx.basis.utils;

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


    /**
     * 两个时间之间相差距离多少天，2018-01-30 16:24:00和2018-01-31 15:24:00相差0天
     * @param date1 时间参数 1：
     * @param date2 时间参数 2：
     * @return 相差天数
     */
    public static Long getDistanceDays(Date date1, Date date2){
        long days;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long diff ;
        if(time1<time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        days = diff / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 将整数天转为秒
     * @param d
     * @return
     */
    public static Long fromatDayMm(long d){
        return 60 * 60 * 24 * d;
    }

    /**
     * 将整数小时转为秒
     * @param d
     * @return
     */
    public static Long fromatHHMm(long d){
        return 60 * 60 * d;
    }

    /**
     * 秒转换 天 小时 秒
     * @param m 要转换的秒数
     * @param isAddSuffix 是否添加后续后缀，如：false,则不添加第一级以后的单位
     * @return 该毫秒数转换为 * 天 * 小时 * 分钟 * 秒 后的格式
     */
    public static String formatMsDuring(long m,boolean isAddSuffix){
        return formatDuring(m * 1000,isAddSuffix);
    }

    /**
     * 毫秒转换 天 小时 秒
     * @param mss 要转换的毫秒数
     * @param isAddSuffix 是否添加后续后缀，如：false,则不添加第一级以后的单位
     * @return 该毫秒数转换为 * 天 * 小时 * 分钟 * 秒 后的格式
     */
    public static String formatDuring(long mss, boolean isAddSuffix) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        String result = "";
        if (days > 0){
            if (!isAddSuffix){
                return result += days;
            }
            result += days+"天";
        }
        if (hours > 0 ){
            if (!isAddSuffix){
                return result += hours;
            }
            result += hours+"小时";
        }
        if (minutes > 0){
            if (!isAddSuffix){
                return result += minutes;
            }
            result += minutes + "分钟";
        }
        if (seconds > 0){
            if (!isAddSuffix){
                return seconds+"";
            }
            result += seconds + "秒";
        }
        return result;
    }

    /**
     * 秒转换小时
     * @param mss 要转换的秒数
     * @param isAddSuffix 是否添加后续后缀，如：false,则不添加第一级以后的单位
     * @return 该秒数转换为 * 小时 后的格式
     */
    public static String formatDuringHH(long mss, boolean isAddSuffix) {
        long hours = (mss % (1000 * 60 * 60 * 24))/3600;
        String result = "";
        if (hours > 0 ){
            if (!isAddSuffix){
                return result += hours;
            }
            result += hours+"小时";
        }
        return result;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
       /* System.out.println(calendar2Str(calendar, ConstantUtils.DATE_FORMAT_1));
        System.out.println(calendar2Str(calendar, ConstantUtils.DATE_FORMAT_2));
        System.out.println(calendar2Str(calendar, ConstantUtils.DATE_FORMAT_3+" "+ConstantUtils.DATE_FORMAT_4));

        System.out.println(millis2Str(System.currentTimeMillis()+"", ConstantUtils.DATE_FORMAT_1));
        System.out.println(millis2Str(System.currentTimeMillis()+"", ConstantUtils.DATE_FORMAT_2));
        System.out.println(millis2Str("3600", ConstantUtils.DATE_FORMAT_1+" "+ConstantUtils.DATE_FORMAT_2));*/
        System.out.println(formatDuringHH(3600, true));

    }
}