package com.ddx.sys.mq;

import com.alibaba.fastjson.JSONObject;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.SysLogAspectVo;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.sys.service.ISysAspectLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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
@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private ISysAspectLogService iSysAspectLogService;

    /**
     * 请求切面日志消息处理
     * 单条消息消费
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(containerGroup= ConstantUtils.SEND_LOG_ASPECT_TOPIC,topicPartitions = {
            @TopicPartition(topic = ConstantUtils.SEND_LOG_ASPECT_TOPIC,partitions = "${app.kafka.consumer.partition.send_log_Aspect_topic}")
    })
    public void consumerLogAspect(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header (KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            SysLogAspectVo msg = JSONObject.parseObject(message.get().toString(),SysLogAspectVo.class);
            try {
                //异步存储请求日志
                iSysAspectLogService.addAspectLog(msg);
                //手动提交偏移量，配置配置文件enable-auto-commit属性使用
                ack.acknowledge();
            } catch (Exception e) {
                ExceptionUtils.businessException(true,CommonEnumConstant.PromptMessage.KAFKA_CONSUMER_ERROR,String.format(CommonEnumConstant.PromptMessage.KAFKA_CONSUMER_ERROR.getMsg(),e.getMessage()));
                e.printStackTrace();
            }
        }
    }
}