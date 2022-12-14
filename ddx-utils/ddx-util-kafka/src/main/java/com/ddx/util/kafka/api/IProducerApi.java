package com.ddx.util.kafka.api;


import com.ddx.util.kafka.handle.ReturnHandle;

/**
 * @ClassName: IProducer
 * @Description: Kafka生产端接口
 * @Author: YI.LAU
 * @Date: 2022年04月28日
 * @Version: 1.0
 */
public interface IProducerApi{

    /**
     * 按key的hash计算topic的发送分区
     * 注意 保证key的变化，否则，数据就会全部去往一个分区里面
     * @param entity
     * @param key
     * @param topic
     * @return
     */
    <T> Boolean send(T entity, String key, String topic);

    /**
     * 按key的hash计算topic的发送分区，定制业务处理
     * 注意 保证key的变化，否则，数据就会全部去往一个分区里面
     * @param topic
     * @param handle 处理定制业务
     * @param key
     * @return
     */
    <T> Boolean send(String topic, String key, ReturnHandle<T> handle);

    /**
     * 向指定分区进行发送信息
     * @param entity
     * @param partition
     * @param topic
     * @return
     */
    <T> Boolean send(T entity, Integer partition, String topic);

    /**
     * 向指定分区发送消息，定制业务处理
     * @param topic
     * @param handle 处理定制业务
     * @param partition
     * @return
     */
    <T> Boolean send(String topic, Integer partition, ReturnHandle<T> handle);
}
