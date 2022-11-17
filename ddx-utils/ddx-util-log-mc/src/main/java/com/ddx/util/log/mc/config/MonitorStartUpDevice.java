package com.ddx.util.log.mc.config;

import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.es.service.IElasticsearchServiceApi;
import com.ddx.util.log.mc.model.dto.EsLogCollectorDTO;
import com.ddx.util.log.mc.monitor.FileMonitorUnit;
import com.ddx.util.log.mc.monitor.LogFileListener;
import com.ddx.util.log.mc.utils.ValidatedConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MonitorStartUpDevice
 * @Description: 监控启动装置
 * @Author: YI.LAU
 * @Date: 2022年10月27日 11:30
 * @Version: 1.0
 */
@Slf4j
@Component
@Order(value = 1) //指定其执行顺序,值越小优先级越高
public class MonitorStartUpDevice implements CommandLineRunner {

    @Autowired
    private LogmcConfig logmcConfig;
    @Autowired
    private IElasticsearchServiceApi elasticsearchServiceApi;

    @Override
    public void run(String... args) {
        Thread startServiceLogFileMonitor = new Thread(()-> {
            startServiceLogFileMonitor(logmcConfig);
        });
        startServiceLogFileMonitor.start();
    }

    /**
     * 启动服务日志文件监控
     * @param logmcConfig
     */
    public void startServiceLogFileMonitor(LogmcConfig logmcConfig){
        try {
            if (logmcConfig.isOpen()) {
                //启动时创建 按服务创建日志索引
                ResponseData responseData = elasticsearchServiceApi.createIndexSettingsMappings(EsLogCollectorDTO.class);
                if (ResponseData.isSuccess(responseData)) {
                    ValidatedConfig.validatedYmlConfig(logmcConfig);
                    String path = logmcConfig.getMonitorFilePath() + "/" + logmcConfig.getServiceName();
                    FileMonitorUnit fileMonitor = new FileMonitorUnit(Long.valueOf(logmcConfig.getMonitorInterval()));
                    fileMonitor.monitor(path, new LogFileListener(logmcConfig));
                    fileMonitor.start();
                    log.info("已启动日志监控采集程序...");
                }else {
                    ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.SUCCESS,responseData.getMsg());
                }
            }else{
                log.info("未开启日志监控采集程序...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
