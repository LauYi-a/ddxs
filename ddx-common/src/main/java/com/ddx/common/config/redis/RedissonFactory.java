package com.ddx.common.config.redis;

import com.ddx.basis.constant.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

import java.util.Set;

/**
 * @ClassName: RedissonFactory
 * @Description:  redis 分布式锁的构造工厂
 * @Author: YI.LAU
 * @Date: 2022年04月10日
 * @Version: 1.0
 */
@Slf4j
public class RedissonFactory {

    private static final int PING_INTERVAL = 1000;

    public static RedissonClient createRedisson(String address, String password) {
        if (StringUtils.startsWith(address, ConstantUtils.SINGLE_SERVER)) {
            String serverAddress = StringUtils.substring(address, ConstantUtils.SINGLE_SERVER.length());
            Config config = new Config().setCodec(JsonJacksonCodec.INSTANCE);
            config.useSingleServer().setAddress(getRedissonAddress(serverAddress));
            return Redisson.create(config);
        } else if (StringUtils.startsWith(address, ConstantUtils.CLUSTER_SERVER)) {
            String serverAddress = StringUtils.substring(address, ConstantUtils.CLUSTER_SERVER.length());
            Set<String> nodes = org.springframework.util.StringUtils.commaDelimitedListToSet(serverAddress);
            Config config = new Config().setCodec(JsonJacksonCodec.INSTANCE);
            ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .setPassword(password)
                    .setMasterConnectionPoolSize(10) // default 64
                    .setMasterConnectionMinimumIdleSize(1) // default 24
                    .setSlaveConnectionPoolSize(10) // default 64
                    .setSlaveConnectionMinimumIdleSize(1) // default 24
                    .setSubscriptionConnectionPoolSize(10) // default 50
                    .setSubscriptionConnectionMinimumIdleSize(1) // default 1
                    .setScanInterval(ConstantUtils.CLUSTER_SCAN_TIME)
                    .setReadMode(ReadMode.MASTER_SLAVE)
                    .setPingConnectionInterval(PING_INTERVAL);
            for (String node : nodes) {
                clusterServersConfig.addNodeAddress(getRedissonAddress(node));
            }
            return Redisson.create(config);
        }
        return null;
    }
    
    private static String getRedissonAddress(String address) {
        return String.format(ConstantUtils.REDISSON_SINGLE_SERVER_FORMAT, address);
    }
}