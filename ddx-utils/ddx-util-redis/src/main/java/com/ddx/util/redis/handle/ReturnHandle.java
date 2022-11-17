package com.ddx.util.redis.handle;


/**
 * @ClassName: ReturnHandle<T>
 * @Description: 函数试接口 泛型返回类型
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public interface ReturnHandle<T> {
 
    /**
     * 业务处理
     * @return
     */
    T execute() throws Exception;
 
}