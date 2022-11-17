package com.ddx.util.redis.handle;


/**
 * @ClassName: ReturnHandle<T>
 * @Description: 函数试接口 无返回
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
public interface VoidHandle {
 
    /**
     * 业务处理
     */
    void execute() throws Exception;

}