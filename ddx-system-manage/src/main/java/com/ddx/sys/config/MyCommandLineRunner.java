package com.ddx.sys.config;

import com.ddx.common.service.FileMonitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @ClassName: MyCommandLineRunner
 * @Description: 在项目启动完成 初始方法执行器
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
@Order(value = 1) //指定其执行顺序,值越小优先级越高
public class MyCommandLineRunner implements CommandLineRunner {

    @Resource
    private FileMonitorService fileMonitorService;
    @Value("${logFile.logPath}")
    private String logPath;
    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void run(String... args){
        //启动日志监控
        Thread logMonitor = new Thread(()-> {
            String path = logPath + File.separator + serviceName;
            fileMonitorService.startLogFileMonitor(path, 5000);
        });
        logMonitor.start();
    }
}