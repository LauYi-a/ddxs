<h1 align="center">ddx-util-redis</h1>

## 简介
redis 工具模块，主要对redis进行封装，将redis相关工具进行统一服装

## 介绍
1. 主要对 redis 连接池相关工厂进行统一的配置封装支持单节点与集群模式，并对 redis 调用模板的 API 进行简单的工具类封装，使其在各服务调用 redis 时更加方便快捷，并具有一定的统一性方便后期的维护与改造。
2. 对于分布式锁我们采用 redis 的 redission 来实现，并对其调用方法做了简单封装，采用函数式接口的方式进行封装

## 集成
#### 1、添加 maven 
```xml
<!--redis 公共服务-->
<dependency>
    <groupId>com.ddx</groupId>
    <artifactId>ddx-util-redis</artifactId>
    <version>${ddx-util-redis.version}</version>
</dependency>
```
#### 2、配置 redis yml 连接配置
```yaml
#redis 配置
spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    password:     
    timeout: 6000ms 
    lettuce:
      pool:
        max-active: 100  
        max-wait: -1ms    
        max-idle: 10     
        min-idle: 1    
        timeout: 200
```
- password:   密码（默认为空）
- timeout:   连接超时时长（毫秒）
- max-active:  连接池最大连接数（使用负值表示没有限制）
- max-wait: 连接池最大阻塞等待时间（使用负值表示没有限制）
- max-idle:  连接池中的最大空闲连接
- min-idle:  连接池中的最小空闲连接

#### 3、配置 redission 分布式锁 yml 配置文件
```yaml
# redisson 分布式锁
redisson:
  common:
    redis-nodes: 127.0.0.1:6379
    redis-pattern: stand-alone 
```
- redis-pattern: stand-alone 单机模式 multiple 集群模式
- redis-nodes: 集群使用豆号分割

#### 4、在 Application 启动类配置扫描包
```java
@ComponentScan(basePackages = {"com.ddx.util.redis.*"})
public class Application {
}
```
## 示例代码
1. redission 分布式锁代码演示
> 不带返回值的
```java
public class Example {
  public void test(){
      RedisLock.getLock("REDIS_KEY",()->{
          //业务逻辑...
      });
  }   
}
```
 > 返回任意类型的
```java
public class Example {
    public <T> T test(){
        return t = RedisLock.getLock("REDIS_KEY",()->{
            Object object = new Object();
        	//业务逻辑...
            return object;
        });
    }   
}
```
>业务执行完成后将会自动释放锁
2. redis RedisTemplateUtil 工具类代码演示
>调用模板工具示例
```java
public class Example {
    @Autowired
    private RedisTemplateUtil redisTemplateUtils; 
    
    public void test(){
        redisTemplateUtils.set("REDIS_KEY",val);
    }
}
```
