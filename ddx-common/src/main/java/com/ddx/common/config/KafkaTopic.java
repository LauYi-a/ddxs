package com.ddx.common.config;

import com.ddx.basis.constant.ConstantUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName: KafkaTopic
 * @Description: Kafka Topic 创建
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Configuration
public class KafkaTopic {

    /**
     * 请求切面日志 topic 分区数量
     */
    @Value("${spring.kafka.init-producer-count.send_log_Aspect_topic}")
    private Integer sendLogAspectTopicPartitionCount;

    /**
     * topic的名称；
     * 分区数量，新主题的复制因子；
     * 如果指定了副本分配，则为-1
     * @return
     */
    @Bean
    public NewTopic initLogAspectTopic() {
        System.out.println(sendLogAspectTopicPartitionCount);
        return new NewTopic(ConstantUtils.SEND_LOG_ASPECT_TOPIC, sendLogAspectTopicPartitionCount, (short) 1);
    }
}