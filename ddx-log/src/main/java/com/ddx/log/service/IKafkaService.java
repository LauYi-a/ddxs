package com.ddx.log.service;

import org.springframework.kafka.support.Acknowledgment;

import java.util.Optional;

/**
 * @ClassName: IKafkaService
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月04日 10:51
 * @Version: 1.0
 */
public interface IKafkaService {

    /**
     * 监控日志内容写入文件
     * @param ack
     * @param message
     */
    void writerLogFile(Acknowledgment ack, Optional message);
}
