package com.ddx.util.log.mc.utils;

import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.log.mc.config.LogmcConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: ValidatedConfig
 * @Description: 校验配置
 * @Author: YI.LAU
 * @Date: 2022年11月11日 23:24
 * @Version: 1.0
 */
public class ValidatedConfig {

    /**
     * 校验 yml 配置文件参数
     * @param logmcConfig
     */
    public static void validatedYmlConfig(LogmcConfig logmcConfig){
        ExceptionUtils.businessException(!StringUtils.isNoneBlank(logmcConfig.getServiceName())
                ,String.format("请在yml文件中配置 string.application.name 监控服务名参数"));
        ExceptionUtils.businessException(!StringUtils.isNoneBlank(logmcConfig.getMonitorFileName())
                ,String.format("请在yml文件中配置 ddxlog.monitor-collector.monitor-file-name 监控文件名参数"));
        ExceptionUtils.businessException(!StringUtils.isNoneBlank(logmcConfig.getMonitorFilePath())
                ,String.format("请在yml文件中配置 ddxlog.monitor-collector.monitor-file-path 监控文件路径参数"));
        ExceptionUtils.businessException(!StringUtils.isNoneBlank(logmcConfig.getLogMcPath())
                ,String.format("请在yml文件中配置 ddxlog.monitor-collector.logmc-path 配置文件路径参数"));
        ExceptionUtils.businessException(!ConfigFileUtil.isConfigFile(logmcConfig.getLogMcPath(),logmcConfig.getLogmcName())
                ,String.format("日志监控启动失败，需要在 %s 此路径下创建 %s 配置文件",logmcConfig.getLogMcPath(),logmcConfig.getLogmcName()));
    };
}
