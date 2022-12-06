package com.ddx.util.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import com.ddx.util.kafka.api.IProducerApi;
import com.ddx.util.kafka.handle.ReturnHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: KafkaProducer
 * @Description: Kafka生产端实现
 * @Author: YI.LAU
 * @Date: 2022年04月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class KafkaProducer<T> implements IProducerApi {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public <T> Boolean send(T entity,String key,String topic) {
        String entityStr = JSONObject.toJSONString(entity);
        AtomicBoolean isOk = new AtomicBoolean(true);
        kafkaTemplate.send(topic,key,entityStr).addCallback(onSuccess ->{ },onFailure->{
            isOk.set(false);
            log.error("kafka 发送消息失败: %s",onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return isOk.get();
    }

    @Override
    public <T> Boolean send(T entity, Integer partition, String topic) {
        String entityStr = JSONObject.toJSONString(entity);
        AtomicBoolean isOk = new AtomicBoolean(true);
        kafkaTemplate.send(topic,partition,topic,entityStr).addCallback(onSuccess ->{ },onFailure->{
            isOk.set(false);
            log.error("kafka 发送消息失败: %s",onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return isOk.get();
    }

    @Override
    public <T> Boolean send(String topic,Integer partition, ReturnHandle<T> handle){
        T t;
        try {
            t = handle.execute();
        }catch (Exception e){
            log.error("处理业务失败: %s",e);
            return false;
        }
        String entityStr = JSONObject.toJSONString(t);
        AtomicBoolean isOk = new AtomicBoolean(true);
        kafkaTemplate.send(topic, partition,topic,entityStr).addCallback(onSuccess ->{},onFailure->{
            isOk.set(false);
            log.error("kafka 发送消息失败: %s",onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return isOk.get();
    }

    @Override
    public <T> Boolean send(String topic,String key, ReturnHandle<T> handle){
        T t;
        try {
            t = handle.execute();
        }catch (Exception e){
            log.error("处理业务失败: %s",e);
            return false;
        }
        String entityStr = JSONObject.toJSONString(t);
        AtomicBoolean isOk = new AtomicBoolean(true);
        kafkaTemplate.send(topic, key,entityStr).addCallback(onSuccess ->{},onFailure->{
            isOk.set(false);
            log.error("kafka 发送消息失败: %s",onFailure.getMessage());
            onFailure.printStackTrace();
        });
        return isOk.get();
    }
}
