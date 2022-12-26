package com.ddx.util.log.collector.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.log.collector.common.DateUtil;
import com.ddx.util.log.collector.constant.CollectorConstant;
import com.ddx.util.log.collector.model.SendLogToKafkaBaseDto;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: KafkaLogAppender
 * @Description:  Kafka logBack 日志采集器
 * @Author: YI.LAU
 * @Date: 2022年11月22日 09:24
 * @Version: 1.0
 */

@Data
@Slf4j
public class KafkaLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public String serviceName;
    public String env;
    public String servers;
    public String isCollector;
    protected Layout<ILoggingEvent> layout;
    protected static KafkaProducer<String, String> producer;
    protected static final String KAFKA_LOGGER_NAME_PREFIX = "org.apache.kafka.";
    protected String keySerializer;
    protected String valueSerializer;
    private static ThreadPoolExecutor threadPool= null;
    private static final int BLOCKING_QUEUE_LENGTH= 10000;// 等待队列长度
    private static final int KEEP_ALIVE_TIME= 60 * 1000;// 闲置线程存活时间


    @Override
    public void start() {
        if (!Boolean.valueOf(isCollector)){
            return;
        }
        if (isStarted()) {
            return;
        }
        if (this.layout == null) {
            addError("No layout set for the appender named [" + name + "].");
            return;
        }
        if (this.servers == null || servers.length() == 0) {
            addError("servers could not be blank.");
            return;
        }
        if (!initKafkaProducer()) {
            return;
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        getThreadPool(CollectorConstant.THREAD_POOL_LOG_C_P).execute(()->{
            if (iLoggingEvent.getLoggerName().startsWith(KAFKA_LOGGER_NAME_PREFIX)) {
                addInfo(iLoggingEvent.getMessage());
                return;
            }
            String eventStr = this.layout.doLayout(iLoggingEvent);
            SendLogToKafkaBaseDto kafkaSendSystemLogBaseDto = SendLogToKafkaBaseDto.builder()
                    .id(UUID.randomUUID().toString())
                    .serviceName(serviceName)
                    .env(env)
                    .date(DateUtil.millisToDate(iLoggingEvent.getTimeStamp()))
                    .message(eventStr)
                    .build();
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(serviceName+"-log", UUID.randomUUID().toString(),JSONObject.toJSONString(kafkaSendSystemLogBaseDto));
            producer.send(producerRecord);
        });
    }

    protected boolean initKafkaProducer() {
        try {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer == null ? "org.apache.kafka.common.serialization.StringSerializer" : keySerializer);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer == null ? "org.apache.kafka.common.serialization.StringSerializer" : valueSerializer);
            properties.put(ProducerConfig.ACKS_CONFIG, "0");
            properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "1000");
            producer = new KafkaProducer<>(properties);
            return true;
        } catch (Exception e) {
            addError("Failed to construct kafka producer", e);
            return false;
        }
    }

    private static synchronized ThreadPoolExecutor getThreadPool(String poolName) {
        if (threadPool == null) {
            // 获取处理器数量
            int cpuNum = Runtime.getRuntime().availableProcessors();
            // 根据cpu数量,计算出合理的线程并发数
            int maxPoolSize = cpuNum * 2 + 1;
            threadPool = new ThreadPoolExecutor(maxPoolSize - 1,//核心线程数
                    maxPoolSize,//最大线程数
                    KEEP_ALIVE_TIME,//闲置线程存活时间
                    TimeUnit.MILLISECONDS,//时间单位
                    new LinkedBlockingDeque<>(BLOCKING_QUEUE_LENGTH),//线程队列 未设置使用Integer.MAX_VALUE作为最大队列
                    new ThreadFactoryBuilder().setNameFormat(poolName +"-%d").build(),//线程工厂
                    new ThreadPoolExecutor.AbortPolicy(){//当前线程数已经超过最大线程数时的异常处理策略
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                            //单独起一个线程处理任务
                            r.run();
                        }
                    });
        }
        return threadPool;
    }
}
