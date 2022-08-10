
package com.ddx.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringCloudApplication
public class DDXConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(DDXConfigApplication.class, args);
	}
}
