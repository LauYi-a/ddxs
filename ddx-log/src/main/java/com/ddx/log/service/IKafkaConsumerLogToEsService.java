package com.ddx.log.service;

import java.util.Optional;

/**
 * @ClassName: ILogToEsService
 * @Description: 处理日志到ES
 * @Author: YI.LAU
 * @Date: 2022年11月18日 14:25
 * @Version: 1.0
 */
public interface IKafkaConsumerLogToEsService {

    public void kafkaConsumerSystemManageLogToEs(Optional message);
}
