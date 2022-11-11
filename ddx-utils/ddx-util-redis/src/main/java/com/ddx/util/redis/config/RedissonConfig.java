package com.ddx.util.redis.config;

import com.ddx.util.basis.constant.ConstantUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RedissonConfig
 * @Description: redis 分布式锁的配置
 * @Author: YI.LAU
 * @Date: 2022年04月10日
 * @Version: 1.0
 */
@Configuration
public class RedissonConfig {

    @Value("${redisson.common.redis-nodes:}")
    private String address;
    @Value("${redisson.common.redis-pattern:}")
    private String redisPattern;
    @Value("${redisson.common.password:}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        String pasw = StringUtils.isBlank(password)?null:password;
        String redissonAddress = "";
        redissonAddress = redisPattern.equals(ConstantUtils.REDIS_PATTERN_MULTIPLE)? ConstantUtils.CLUSTER_SERVER + address:
                redisPattern.equals(ConstantUtils.REDIS_PATTERN_STAND_ALONE)?ConstantUtils.SINGLE_SERVER + address:"";
        return RedissonFactory.createRedisson(redissonAddress,pasw);
    }

}