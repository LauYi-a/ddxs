<h1 align="center">ddx-web</h1>

## 简介
 web管理器，集成了各web服务需要的pom依赖，对web服务做权限拦截等工作。
## 介绍
实现了web服务的权限拦截并对网关传递的请求传递用户信息，rpc远程调用令牌中续等功能
##集成
#### 添加 maven 
```xml
 <!-- ddx-web -->
<dependency>
    <groupId>com.ddx</groupId>
    <artifactId>ddx-web</artifactId>
    <version>${ddx-web.version}</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```