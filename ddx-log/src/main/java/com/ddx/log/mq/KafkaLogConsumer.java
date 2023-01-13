package com.ddx.log.mq;

import com.ddx.log.service.IKafkaConsumerLogToEsService;
import com.ddx.util.kafka.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
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
 * @Description: kafka 日志消费端
 * @Author: YI.LAU
 * @Date: 2022年04月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class KafkaLogConsumer {

    @Autowired
    private IKafkaConsumerLogToEsService iKafkaConsumerLogToEsService;

    /**
     * 系统服务日志消费
     * 单条消息消费
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(id= KafkaConstant.DDX_SYSTEM_MANAGE_TOPIC,topics ={KafkaConstant.DDX_SYSTEM_MANAGE_TOPIC})
    public void consumerSystemManageLogAspect(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            try {
                iKafkaConsumerLogToEsService.kafkaConsumerSystemManageLogToEs(message);
                ack.acknowledge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 认证服务日志消费
     * 单条消息消费
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(id= KafkaConstant.DDX_AUTH_TOPIC,topics ={KafkaConstant.DDX_AUTH_TOPIC})
    public void consumerAuthLogAspect(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            try {
                iKafkaConsumerLogToEsService.kafkaConsumerSystemManageLogToEs(message);
                ack.acknowledge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}