package com.ddx.log.mc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MyCommandLineRunner
 * @Description:  初始化json配置文件
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Slf4j
@Component
@Order(value = 1) //指定其执行顺序,值越小优先级越高
public class ConfigJsonInit implements CommandLineRunner {

    @Override
    public void run(String... args){

    }
}