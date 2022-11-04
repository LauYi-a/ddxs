package com.ddx.file;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: DDXTestRpcApplication
 * @Description: 数据服务
 * @Author: YI.LAU
 * @Date: 2022年11月01日
 * @Version: 1.0
 */


@SpringCloudApplication
@EnableAutoConfiguration(exclude= DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages={"com.ddx"})
@ComponentScan(basePackages = {"com.ddx.file.*","com.ddx.common.*"})
public class DDXFileApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DDXFileApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
