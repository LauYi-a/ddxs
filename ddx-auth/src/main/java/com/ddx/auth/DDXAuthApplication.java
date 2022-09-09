package com.ddx.auth;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: DDXAuthApplication
 * @Description: 验证服务
 * @Author: YI.LAU
 * @Date: 2022年03月21日  0021
 * @Version: 1.0
 */

@SpringCloudApplication
@EnableAutoConfiguration(exclude= DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages={"com.ddx"})
@ComponentScan(basePackages = {"com.ddx.*","com.ddx.common.*","com.ddx.basis.*"})
public class DDXAuthApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DDXAuthApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
