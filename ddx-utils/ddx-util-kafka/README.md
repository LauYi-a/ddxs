<h1 align="center">ddx-util-kafka</h1>

## 简介
kafka 工具模块，主要对 kafka 进行封装，将 kafka 生产端代码逻辑进行了封装

## 介绍
我们将 kafka `kafkaTemplate` 模板类的消息生产端逻辑进行了统一的业务包装，采用函数式接口与泛型设计的方法对`kafkaTemplate`进行了简单的封装提供全新的
API 调用方法提供给第三方集成服务，使其更加方便快捷的使用 Kafka 消息队列进行业务开发

## 集成
#### 1、添加 maven 
```xml
<!--kafka 公共服务-->
<dependency>
    <groupId>com.ddx</groupId>
    <artifactId>ddx-util-kafka</artifactId>
    <version>${ddx-util-kafka.version}</version>
</dependency>
```
#### 2、配置 kafka yml 配置
```yaml
#kafka 配置
spring:
  kafka:
    bootstrap-servers: 127.0.0.1:9092
    producer:
      retries: 2
      batch-size: 16384
      buffer-memory: 33554432
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: 1
    consumer:
      auto-commit-interval: 1S
      auto-offset-reset: earliest
      enable-auto-commit: false
      max-poll-records: 1
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    listener:
      concurrency: 1
      ack-mode: manual_immediate
      missing-topics-fatal: false
```
- retries: 发生错误后，消息重发的次数。
- batch-size: 当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小，按照字节数计算。
- buffer-memory: 设置生产者内存缓冲区的大小。
- key-serializer: 键的序列化方式
- value-serializer: 值的序列化方式
- acks: 0 
`acks=0:  生产者在成功写入消息之前不会等待任何来自服务器的响应。
acks=1:  只要集群的首领节点收到消息，生产者就会收到一个来自服务器成功响应。
acks=all: 只有当所有参与复制的节点全部收到消息时，生产者才会收到一个来自服务器的成功响应。`
- auto-commit-interval: 自动提交的时间间隔 在spring boot 2.X 版本中这里采用的是值的类型为Duration 需要符合特定的格式，如1S,1M,2H,5D
- auto-offset-reset: 该属性指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下该作何处理
`latest（默认值）在偏移量无效的情况下，消费者将从最新的记录开始读取数据（在消费者启动之后生成的记录）
earliest ：在偏移量无效的情况下，消费者将从起始位置读取分区的记录`
- enable-auto-commit: 是否自动提交偏移量，默认值是true,为了避免出现重复数据和数据丢失，可以把它设置为false,然后手动提交偏移量
- max-poll-records: 每次拉取多少条数据
- key-deserializer: 键的反序列化方式
- value-deserializer:值的反序列化方式
- concurrency:在侦听器容器中运行的线程数,为了保证一致性，只启用一个线程
- ack-mode: listner负责ack，每调用一次，就立即commit

#### 3、在 Application 启动类配置扫描包
```java
@ComponentScan(basePackages = {"com.ddx.util.kafka.*"})
public class Application {
}
```
## 示例代码
1.泛型式接口
```java
public class Example {
    @Autowired
    private IProducerApi iProducerApi; 
    
    public void test(){
        iProducerApi.send(t,key,topic);
    }
}
```
1.函数式泛型接口
```java
public class Example {
    @Autowired
    private IProducerApi iProducerApi; 
    
    public void test(){
        iProducerApi.send(topic,key,()->{
            //业务逻辑代码...
        });
    }
}
```