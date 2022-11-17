package com.ddx.util.redis.redission;

import com.ddx.util.redis.constant.LockConstant;
import com.ddx.util.redis.constant.LockEnum;
import com.ddx.util.redis.handle.ReturnHandle;
import com.ddx.util.redis.handle.VoidHandle;
import com.ddx.util.redis.result.LockResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
    public <T> LockResultData getLock(String lockName, VoidHandle handle){
        LockResultData resultLockInfo = getLock(lockName);
        if (!resultLockInfo.isOk()){
            return resultLockInfo;
        }
        try {
            handle.execute();
            return LockResultData.result(LockEnum.STATUS_TRUE);
        }catch (Exception e){
            return LockResultData.result(LockEnum.STATUS_FALSE,String.format("锁名：%s，业务处理失败：%s",lockName,e.getMessage()));
        }finally {
            RLock rLock = (RLock) resultLockInfo.getData();
            rLock.unlock();
        }
    }

    /**
     * 带返回值 分布式获取锁实现 获取锁失败返回 null
     * @param lockName 锁名称
     * @param handle 业务处理
     * @param <T> 返回泛型
     * @return 返回业务处理值
     */
    public <T> LockResultData getLock(String lockName, ReturnHandle<T> handle){
        LockResultData resultLockInfo = getLock(lockName);
        if (!resultLockInfo.isOk()){
            return resultLockInfo;
        }
        try {
            return LockResultData.result(LockEnum.STATUS_TRUE,handle.execute());
        }catch (Exception e){
            return LockResultData.result(LockEnum.STATUS_FALSE,String.format("锁名：%s，业务处理失败：%s",lockName,e.getMessage()));
        }finally {
            RLock rLock = (RLock) resultLockInfo.getData();
            rLock.unlock();
        }
    }

    /**
     * 获取锁 基础方法
     * @param lockName
     * @return
     */
    private <T> LockResultData getLock(String lockName ) {
        if (StringUtils.isEmpty(lockName)) {
            return LockResultData.result(LockEnum.STATUS_FALSE,String.format("锁名：%s，分布式锁KEY为空！",lockName));
        }
        try {
            String lockKey = LockConstant.SYSTEM_REQUEST+lockName;
            RLock rLock = redissonClient.getLock(lockKey);
            if (!rLock.tryLock()) {
                return LockResultData.result(LockEnum.STATUS_FALSE,String.format("锁名：%s,业务处理中！",lockName));
            }
            return LockResultData.result(LockEnum.STATUS_TRUE,rLock);
        }catch (Exception e){
            return LockResultData.result(LockEnum.STATUS_FALSE,String.format("锁名：%s Message: ",e.getMessage()));
        }
    }
 
}