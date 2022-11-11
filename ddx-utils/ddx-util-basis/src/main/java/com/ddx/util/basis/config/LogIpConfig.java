package com.ddx.util.basis.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.ddx.util.basis.utils.IPUtils;

/**
 * @ClassName: LogIpConfig
 * @Description: 打印日志获取服务ip
 * @Author: YI.LAU
 * @Date: 2022年10月25日
 * @Version: 1.0
 */
public class LogIpConfig extends ClassicConverter {
    private static String webIP;
    static {
        webIP = IPUtils.getIp();
    }
  
    @Override
    public String convert(ILoggingEvent event) {
        return webIP;
    }
}