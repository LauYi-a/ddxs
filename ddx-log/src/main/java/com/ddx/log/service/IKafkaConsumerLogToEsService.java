package com.ddx.log.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName: ILogToEsService
 * @Description: 处理日志到ES
 * @Author: YI.LAU
 * @Date: 2022年11月18日 14:25
 * @Version: 1.0
 */
public interface IKafkaConsumerLogToEsService {

    /**
     * kafka消费系统管理日志信息异步存储ES
     * @param message
     */
    public void kafkaConsumerSystemManageAsyncLogToEs(Optional message) throws ExecutionException, InterruptedException;
}
