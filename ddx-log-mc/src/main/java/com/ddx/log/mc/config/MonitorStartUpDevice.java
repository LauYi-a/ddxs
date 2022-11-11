package com.ddx.log.mc.config;


import com.ddx.log.mc.monitor.FileMonitorUnit;
import com.ddx.log.mc.monitor.LogFileListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Order(value = 2) //指定其执行顺序,值越小优先级越高
public class MonitorStartUpDevice implements CommandLineRunner {

    @Value("string.application.name")
    private String serviceName;

    @Override
    public void run(String... args) throws Exception {

    }

    /**
     * 启动服务日志文件监控
     * @param path
     * @param interval
     */
    public void startServiceLogFileMonitor(String path, long interval){
        try {
            FileMonitorUnit fileMonitor = new FileMonitorUnit(interval);
            fileMonitor.monitor(path, new LogFileListener(serviceName));
            fileMonitor.start();
            //log.info("已启动服务日志文件监控...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
