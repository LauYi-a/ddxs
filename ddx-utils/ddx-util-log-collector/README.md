<h1 align="center">ddx-util-log-collector</h1>

## 简介
日志采集模块，主要用作微服务系统运行日志的采集工作，将服务分部在多个节点运行时产生的日志统一收集，
收集之后发送到 kafka 中，然后通过日志中央系统处理日志进行存储、查询、分析等工作。

## 介绍
- 采用 logBack 自定义 appender 来实现监控系统通过 log4j slf4j 打印的日志，监控到日志产生时
将日志以异步线程池的方式发送到 Kafka 中。
    - 使用服务名称加 `-log`后缀 作为 Kafka 的 topic。
    - 线程池根据cpu数量,计算出合理的线程并发数，
    最长等待线程队列为1000个，线程存活时间为1小时，
    超过一小时没有日志进行传输将会自动关闭线程池，下次调用时在创建线程池

## 集成
> ###1、添加 maven 
```xml
<!--redis 公共服务-->
<dependency>
    <groupId>com.ddx</groupId>
    <artifactId>ddx-util-redis</artifactId>
    <version>${ddx-util-redis.version}</version>
</dependency>
```
> ###2. 添加yml配置文件 
>>####2.1.添加采集服务 bootstrap.yml
```yaml
#日志配置
ddxlog:
  file-path: D:\\logs
  isFormatterHttp: true
  kafka-servers: 127.0.0.1:9092
```
- file-path 日志本地文件存储路径
- isFormatterHttp 是否对HTTP请求信息进行格式化打印
- kafka-servers Kafka服务地址
>> ####2.2config 服务 common.yml 配置
```yaml
#配置 Kafka topic
spring:
  kafka:
    ddx-system-manage-topic: ddx-xxx-log
```
- Kafka topic 以服务名称加 -log 命名
>###3. 添加 logback-spring.xml 配置文件
>>####3.1.获取配置文件上下文
```xml
<springProperty scop="context" name="ddxlog.file-path" source="ddxlog.file-path" defaultValue=""/>
<springProperty scop="context" name="spring.profiles.active" source="spring.profiles.active" defaultValue=""/>
<springProperty scop="context" name="spring.application.name" source="spring.application.name" defaultValue=""/>
<springProperty scope="context" name="serverIp" source="spring.cloud.client.ip-address" defaultValue=""/>
<springProperty scope="context" name="servers" source="ddxlog.kafka-servers" defaultValue=""/>
```
- ddxlog.file-path 日志文件本地存储路径
- spring.profiles.active 服务环境
- spring.application.name 服务名称
- serverIp 服务本机ip
- servers Kafka服务地址
>>####3.2.配置logback 自定义 appender
```xml
<!-- 日志输出 kafka servers多个逗号隔离-->
<appender name="kafkaLog" class="com.ddx.util.log.collector.appender.KafkaLogAppender">
    <serviceName>${spring.application.name}</serviceName>
    <env>${spring.profiles.active}</env>
    <servers>${servers}</servers>
    <layout>
        <pattern>%-5level ${spring.profiles.active} [%date] [%thread] [${serverIp}] [%X{serialNumber}] [%logger-%line] : %msg%n</pattern>
    </layout>
</appender>

<!-- 配置级别 -->
<root level="INFO">
    <appender-ref ref="kafkaLog" />
</root>
```
- class= com.ddx.util.log.collector.appender.KafkaLogAppender 为 appender 具体实现
- serviceName 服务名
- env 环境
- servers Kafka服务地址 多个受用（,）逗号分割
- layout 输出日志格式
>###4. Application 微服务启动器配置扫描包
```markdown
@ComponentScan(basePackages = {"com.ddx.util.log.collector.*"})
```
>至此 日志采集端所有配置已完成 接下来需要在 ddx-log 中央日志处理系统添加 Kafka消费端，消费ddx-xxx-log topic 进行日志处理即可`