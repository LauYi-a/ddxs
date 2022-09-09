package com.ddx.basis.utils;

import com.ddx.basis.constant.ConstantUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: SerialNumber
 * @Description: 生成流水号
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
public class SerialNumber {
     
    private static final int MAX_VALUE=999999;
    private String FILLA="%06d";
    private static Format DF= null;
    private static final byte[] lock = new byte[0];
    private String prefix = null;
    private Date date = null;
    private int number=1;
    private static Map<String, SerialNumber> map = new HashMap<String, SerialNumber>();
     
    private SerialNumber(String prefix,Date date){
        this.prefix = prefix;
        this.date = date;
    }

    /**
     * 根据前缀与时间格式生成
     * @param prefix 流水前缀
     * @param format 时间格式
     * @return 返回指定格式前缀流水号
     */
    public static SerialNumber newInstance(String prefix,String format){
        Date date = new Date();
        DF = new SimpleDateFormat(format);
        return newInstance(prefix,date);
    }

    /**
     * 根据时间格式生成 无前缀 流水号
     * @param format 时间格式
     * @return 返回指定格式流水号
     */
    public static SerialNumber newInstance(String format){
        Date date = new Date();
        DF = new SimpleDateFormat(format);
        return newInstance("",date);
    }

    private static SerialNumber newInstance(String prefix,Date date){
        SerialNumber o = null;
        synchronized (lock) {
            String key = getKey(prefix, date);
            if(map.containsKey(key)){
                o = map.get(key);
                int number = o.getNumber();
                if(number<MAX_VALUE){
                    o.setNumber(number+1);
                }else {
                    o.setNumber(1);
                }
                 
            } else {
                 o = new SerialNumber(prefix,date);
                 map.put(key, o);
            }
        }
        return o;
    }

    private static String getKey(String prefix,Date date){
        return prefix+format(date);
    }
 
    private static String format(Date date){
        return DF.format(date);
    }
     
    public String toString(){
        return  prefix+ format(date) + String.format(FILLA, number);
    }
 
    public void setNumber(int number) {
        this.number = number;
    }
 
    public int getNumber() {
        return number;
    }

    private static void main(String[] args) {
        for (int j = 0;j<100;j++){
            System.out.println(SerialNumber.newInstance("ID",ConstantUtils.DATE_FORMAT_5));
        }
    }
}