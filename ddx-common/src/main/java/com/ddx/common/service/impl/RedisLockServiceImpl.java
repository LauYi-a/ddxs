package com.ddx.common.service.impl;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.common.service.ReturnHandle;
import com.ddx.common.service.VoidHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName: RedisLock
 * @Description: redis 分布式锁
 * 可根据 RedissonClient 接口扩展不实现
 * 可补充 阻塞锁实现 红锁实现  可重入锁实现
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Slf4j
@Service
public class RedisLockServiceImpl {
 
    @Autowired
    private RedissonClient redissonClient;


    /**
     * 分布式获取锁实现 获取失败无返回值
     * @param lockName 锁名称
     * @param handle 业务处理
     */
    public void tryLock(String lockName,  VoidHandle handle) {
        RLock rLock = getLock(lockName);
        if (!rLock.tryLock()) {
            log.error("锁名：{}，获取锁失败，返回",lockName);
            return;
        }
        try {
            log.info("锁名：{}，获取锁成功",lockName);
            handle.execute();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 带返回值 分布式获取锁实现 获取锁失败返回 null
     * @param lockName 锁名称
     * @param handle 业务处理
     * @param <T> 返回值
     * @return
     */
    public <T> T tryLock(String lockName,  ReturnHandle<T> handle) {
        RLock rLock = getLock(lockName);
        if (!rLock.tryLock()) {
            log.error("锁名：{}，获取锁失败，返回null",lockName);
            return null;
        }
        try {
            log.info("锁名：{}，获取锁成功",lockName);
            return handle.execute();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 分布式获取锁实现 获锁失败时抛出异常
     * @param lockName 锁名称
     * @param handle 业务处理
     */
    public void tryLockException(String lockName,  VoidHandle handle) {
        RLock rLock = getLock(lockName);
        if (!rLock.tryLock()) {
            log.error("锁名：{}，获取锁失败，抛异常处理",lockName);
            ExceptionUtils.errorBusinessException(CommonEnumConstant.PromptMessage.IN_BUSINESS_PROCESS_ERROR);
        }
        try {
            log.info("锁名：{}，获取锁成功",lockName);
            handle.execute();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 带返回值 分布式获取锁实现 获锁失败时抛出异常
     * @param lockName 锁名称
     * @param handle 业务处理
     * @param <T> 返回值
     * @return
     */
    public <T> T tryLockException(String lockName, ReturnHandle<T> handle) {
        RLock rLock = getLock(lockName);
        if (!rLock.tryLock()) {
            log.error("锁名：{}，获取锁失败，抛异常处理",lockName);
            ExceptionUtils.errorBusinessException(CommonEnumConstant.PromptMessage.IN_BUSINESS_PROCESS_ERROR);
        }
        try {
            log.info("锁名：{}，获取锁成功",lockName);
            return handle.execute();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取锁 基础方法
     * @param lockName
     * @return
     */
    private RLock getLock(String lockName ) {
        log.info("获取分布式锁lockName:{}", lockName);
        if (StringUtils.isEmpty(lockName)) {
            ExceptionUtils.errorBusinessException(CommonEnumConstant.PromptMessage.REDIS_LOCK_KEY_ISNULL_ERROR);
        }
        String lockKey = ConstantUtils.SYSTEM_REQUEST+lockName;
        return redissonClient.getLock(lockKey);
    }
 
}