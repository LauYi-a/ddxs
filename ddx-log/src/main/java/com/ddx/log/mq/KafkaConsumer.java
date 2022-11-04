package com.ddx.log.mq;


import com.ddx.basis.constant.ConstantUtils;
import com.ddx.log.service.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @ClassName: KafkaConsumer
 * @Description: kafka消费端
 * @Author: YI.LAU
 * @Date: 2022年04月28日
 * @Version: 1.0
 */
@Component
public class KafkaConsumer {

    @Autowired
    private IKafkaService iKafkaService;

    /**
     * 系统服务日志监控消费
     * 单条消息消费
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(id=ConstantUtils.DDX_SYSTEM_MANAGE_TOPIC,topics ={ConstantUtils.DDX_SYSTEM_MANAGE_TOPIC})
    public void consumerSystemManage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header (KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        iKafkaService.writerLogFile(ack,message);
    }

    /**
     * 认证服务日志监控消费
     * 单条消息消费
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(id=ConstantUtils.DDX_AUTH_TOPIC,topics ={ConstantUtils.DDX_AUTH_TOPIC})
    public void consumerAuth(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header (KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        iKafkaService.writerLogFile(ack,message);
    }
}