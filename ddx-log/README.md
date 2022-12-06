<h1 align="center">ddx-log</h1>

## 简介
中央日志处理服务，负责处理微服务生产的日志，将日志存储到ES中
## 介绍
中央日志处理服务，主要对集成了 ddx-util-log-collector 工具包的服务生产的日志进行统一处理，将采集到的日志进行整合后存储入ES中，
后提供接口，对日志进行查询 删除 分析等功能 
## 集成
在web服务集成 ddx-util-log-collector 之后需要在 中央日志处理系统添加 KafkaConsumer 消费端添加集成服务的消费 topic
```java
public class KafkaConsumer {
   /**
    * xxx服务日志消费
    * 单条消息消费
    * @param record
    * @param ack
    * @param topic
    */
    @KafkaListener(id= KafkaConstant.XXXXXX_TOPIC,topics ={KafkaConstant.XXXXXX_TOPIC})
    public void consumerXXXLogAspect(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
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
```