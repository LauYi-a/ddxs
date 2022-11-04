package com.ddx.common.service.impl;

import com.ddx.common.monitor.FileMonitor;
import com.ddx.common.monitor.LogFileListener;
import com.ddx.common.service.FileMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FileMonitorServiceImpl
 * @Description: 文件监控实现
 * @Author: YI.LAU
 * @Date: 2022年10月27日 11:30
 * @Version: 1.0
 */
@Slf4j
@Service
public class FileMonitorServiceImpl implements FileMonitorService {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void startLogFileMonitor(String path, long interval){
        try {
            FileMonitor fileMonitor = new FileMonitor(interval);
            fileMonitor.monitor(path, new LogFileListener(serviceName));
            fileMonitor.start();
            log.info("已启动日志文件监控...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
