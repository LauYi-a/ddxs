package com.ddx.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    /*@Value("${logFile.logPath}")
    private String logPath;*/
    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void run(String... args){

    }
}