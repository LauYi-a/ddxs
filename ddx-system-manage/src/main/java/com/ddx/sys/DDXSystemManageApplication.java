package com.ddx.sys;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: DDXSystemManageApplication
 * @Description: 系统管理服务
 * @Author: YI.LAU
 * @Date: 2022年03月21日  0021
 * @Version: 1.0
 */

@EnableAsync
@SpringCloudApplication
@EnableAutoConfiguration(exclude= DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages={"com.ddx"})
@ComponentScan(basePackages = {"com.ddx.sys.*","com.ddx.util.log.mc.*","com.ddx.util.kafka.*","com.ddx.util.redis.*","com.ddx.util.basis.*", "com.ddx.web.*"})
public class DDXSystemManageApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DDXSystemManageApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
