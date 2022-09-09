package com.ddx.basis.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: IPUtils
 * @Description: 获取IP
 * 获取IP地址
 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
 * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public class IPUtils {

    public static String getIpAddr(HttpServletRequest request) {
 
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}