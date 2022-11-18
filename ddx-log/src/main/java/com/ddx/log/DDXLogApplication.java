package com.ddx.log;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: DDXLogApplication
 * @Description: 系统日志监控服务
 * @Author: YI.LAU
 * @Date: 2022年03月21日  0021
 * @Version: 1.0
 */


@SpringCloudApplication
@EnableAutoConfiguration(exclude= DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages={"com.ddx"})
@ComponentScan(basePackages = {"com.ddx.log.*","com.ddx.util.redis.*","com.ddx.util.es.*","com.ddx.util.kafka.*","com.ddx.web.*","com.ddx.util.basis.*"})
public class DDXLogApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DDXLogApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
