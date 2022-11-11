package com.ddx.util.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.handle.ReturnHandle;
import com.ddx.util.kafka.api.IProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName: KafkaProducer
 * @Description: Kafka生产端实现
 * @Author: YI.LAU
 * @Date: 2022年04月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class KafkaProducer<T> implements IProducer<T> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Boolean send(T entity,String key,String topic) {
        String entityStr = JSONObject.toJSONString(entity);
        kafkaTemplate.send(topic,key,entityStr).addCallback(onSuccess ->{

        },onFailure->{
            //生产者 发送消息失败 处理操作
            ExceptionUtils.businessException(true, CommonEnumConstant.PromptMessage.KAFKA_SEND_ERROR,onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return true;
    }

    @Override
    public Boolean send(T entity, Integer partition, String topic) {
        String entityStr = JSONObject.toJSONString(entity);
        kafkaTemplate.send(topic,partition,topic,entityStr).addCallback(onSuccess ->{

        },onFailure->{
            //生产者 发送消息失败 处理操作
            ExceptionUtils.businessException(true,CommonEnumConstant.PromptMessage.KAFKA_SEND_ERROR,onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return true;
    }

    @Override
    public Boolean send(String topic,Integer partition, ReturnHandle<T> handle)throws Exception {
        T t = handle.execute();
        String entityStr = JSONObject.toJSONString(t);
        kafkaTemplate.send(topic, partition,topic,entityStr).addCallback(onSuccess ->{},onFailure->{
            //生产者 发送消息失败 处理操作
            ExceptionUtils.businessException(true,CommonEnumConstant.PromptMessage.KAFKA_SEND_ERROR,onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return true;
    }

    @Override
    public Boolean send(String topic,String key, ReturnHandle<T> handle)throws Exception {
        T t = handle.execute();
        String entityStr = JSONObject.toJSONString(t);
        kafkaTemplate.send(topic, key,entityStr).addCallback(onSuccess ->{},onFailure->{
            //生产者 发送消息失败 处理操作
            ExceptionUtils.businessException(true,CommonEnumConstant.PromptMessage.KAFKA_SEND_ERROR,onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return true;
    }
}