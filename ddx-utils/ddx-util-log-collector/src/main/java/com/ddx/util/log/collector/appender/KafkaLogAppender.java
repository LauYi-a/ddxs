package com.ddx.util.log.collector.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.log.collector.common.DateUtil;
import com.ddx.util.log.collector.common.ThreadPoolUtil;
import com.ddx.util.log.collector.constant.CollectorConstant;
import com.ddx.util.log.collector.model.KafkaSendSystemLogBaseDto;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

/**
 * @ClassName: KafkaLogAppender
 * @Description:  Kafka logBack 日志采集器
 * @Author: YI.LAU
 * @Date: 2022年11月22日 09:24
 * @Version: 1.0
 */
@Data
public class KafkaLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public String serviceName;
    public String env;
    public String servers;
    protected Layout<ILoggingEvent> layout;
    protected static KafkaProducer<String, String> producer;
    protected static final String KAFKA_LOGGER_NAME_PREFIX = "org.apache.kafka.";
    protected String keySerializer;
    protected String valueSerializer;
    @Override
    public void start() {
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
        ThreadPoolUtil.execute(CollectorConstant.THREAD_POOL_LOG_C_P,()->{
            if (iLoggingEvent.getLoggerName().startsWith(KAFKA_LOGGER_NAME_PREFIX)) {
                addInfo(iLoggingEvent.getMessage());
                return;
            }
            String eventStr = this.layout.doLayout(iLoggingEvent);
            KafkaSendSystemLogBaseDto kafkaSendSystemLogBaseDto = KafkaSendSystemLogBaseDto.builder()
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
}
