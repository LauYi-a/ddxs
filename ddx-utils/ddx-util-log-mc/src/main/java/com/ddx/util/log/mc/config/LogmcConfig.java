package com.ddx.util.log.mc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: LogmcConfig
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月11日 23:12
 * @Version: 1.0
 */
@Data
@Configuration
public class LogmcConfig {

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 监控文件名
     */
    @Value("${ddxlog.monitor-collector.monitor-file-name:}")
    private String monitorFileName;

    /**
     * 监控文件路径
     */
    @Value("${ddxlog.monitor-collector.monitor-file-path:}")
    private String monitorFilePath;

    /**
     * 配置文件路径
     */
    @Value("${ddxlog.monitor-collector.logmc-path:}")
    private String logMcPath;

    /**
     * 配置文件名
     */
    @Value("${ddxlog.monitor-collector.logmc-name:logmc.json}")
    private String logMcName;


    /**
     * 文件监控频率
     */
    @Value("${ddxlog.monitor-collector.monitor-interval:5000}")
    private Long monitorInterval;

    /**
     * 是否打开监控
     */
    @Value("${ddxlog.monitor-collector.isOpen:false}")
    private boolean isOpen;

}
