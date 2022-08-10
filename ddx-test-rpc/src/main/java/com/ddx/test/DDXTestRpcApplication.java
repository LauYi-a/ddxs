package com.ddx.test;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: DDXTestRpcApplication
 * @Description: 测试rpc服务
 * @Author: YI.LAU
 * @Date: 2022年03月21日  0021
 * @Version: 1.0
 */


@SpringCloudApplication
@EnableAutoConfiguration(exclude= DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages={"com.ddx"})
@ComponentScan(basePackages = {"com.ddx.test.*","com.ddx.common.*"})
public class DDXTestRpcApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DDXTestRpcApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
