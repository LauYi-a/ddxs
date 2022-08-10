package com.ddx.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RequestRateLimiterConfig {

    /**
     * 按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
     * 按URL限流
     */
    @Bean
    @Primary
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }

    /**
     *通过exchange对象可以获取到请求信息，这边用了HostName
     *按IP来限流
     */
    @Bean
    KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

}