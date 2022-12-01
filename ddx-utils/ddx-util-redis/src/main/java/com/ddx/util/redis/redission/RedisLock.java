package com.ddx.util.redis.redission;

import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.handle.ReturnHandle;
import com.ddx.util.redis.handle.VoidHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @ClassName: RedisLock
 * @Description: redis 分布式锁
 * 可根据 RedissonClient 接口扩展不同实现
 * 可补充 阻塞锁实现 红锁实现  可重入锁实现
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedisLock {
 
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 不带返回值 分布式获取锁实现
     * @param lockName 锁名称
     * @param handle 业务处理
     * @param <T> 返回泛型
     * @return 返回业务处理值
     */
    public <T> void getLock(String lockName, VoidHandle handle){
        RLock resultLockInfo = getLock(lockName);
        log.info("锁名：{} 获锁成功",lockName);
        try {
            handle.execute();
            log.info("锁名：{} 处理业务逻辑成功",lockName);
        }catch (Exception e){
            log.error("锁名：{} 业务处理失败 Message:{}",lockName,e);
            throw new RuntimeException(String.format("锁名：%s，业务处理失败：%s",lockName,e.getMessage()));
        }finally {
            if (resultLockInfo!=null) {
                resultLockInfo.unlock();
            }
        }
    }


    /**
     * 带返回值 分布式获取锁实现 获取锁失败返回 null
     * @param lockName 锁名称
     * @param handle 业务处理
     * @param <T> 返回泛型
     * @return 返回业务处理值
     */
    public <T> T getLock(String lockName, ReturnHandle<T> handle){
        RLock resultLockInfo = getLock(lockName);
        log.info("锁名：{} 获锁成功",lockName);
        try {
            log.info("锁名：{} 处理业务成功",lockName);
            return handle.execute();
        }catch (Exception e){
            log.error("锁名：{} 业务处理失败 Message:{}",lockName,e);
            throw new RuntimeException(String.format("锁名：%s，业务处理失败：%s",lockName,e.getMessage()));
        }finally {
            if (resultLockInfo!=null) {
                resultLockInfo.unlock();
            }
        }
    }

    /**
     * 获取锁 基础方法
     * @param lockName
     * @return
     */
    private <T> RLock getLock(String lockName ) {
        log.info("获取分布式锁lockName:{}", lockName);
        if (StringUtils.isEmpty(lockName)) {
            log.error("锁名：{}，分布式锁KEY为空", lockName);
            throw new RuntimeException(String.format("锁名：%s，分布式锁KEY为空！",lockName));
        }
        try {
            String lockKey = RedisConstant.SYSTEM_REQUEST+lockName;
            RLock rLock = redissonClient.getLock(lockKey);
            if (!rLock.tryLock()) {
                log.error("锁名：{}，业务处理中", lockName);
                throw new RuntimeException(String.format("锁名：%s,业务处理中！",lockName));
            }
            return rLock;
        }catch (Exception e){
            log.error("锁名：{}，Message:{}", lockName,e);
            throw new RuntimeException(String.format("锁名：%s Message: ",e.getMessage()));
        }
    }
 
}