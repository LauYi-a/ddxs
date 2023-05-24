<h1 align="center">ddx</h1>

## 简介
- ddx 创建于 2022 年 4 月 作者 YI.LAU 
- 目的作为模板化系统架构，开箱即用，满足个人学习以及企业快速迭代
- 此开源项目为个人开发，不限制任何商业使用和个人研究，使用之前请先点个Star对我进行鼓励
- 利用此开源项目参与的一切违法、色情相关的活动均与本源码无关，请勿以身示法

## 介绍
- 此项目作为前后端分离项目
    - 前端使用vue3+vite+element-plus构建具体点击查看[前端GitHub源码详情](https://github.com/LauYi-a/ddx-web) 
- 后端采用分布式微服务架构
    - 采用 spring-cloud+spring-boot+mysql+mybatis-plus 为主，并集成了 redis、kafka、elasticsearch、minio 等中间件，
    采用 openfeign 作为远程服务调用并对rpc服务内置调用实现了令牌终续，使用 redisson 实现分布式锁，并使用 oauth2与自定义验证方式实现了系统认证鉴权功能。
    - 通过 gateway 对访问路由做统一处理，所有服务请求都是通过 gateway 来进行转发并对网关请求发放请求令牌保证请求安全，网关对服务实现控制限流 熔断 等功能
## 集成环境
|  技术点   |   版本    |       描述       |
| :-----: | :-------: |:--------------:|
|  spring-boot   | 2.0.8.RELEASE |     微服务基础      |
|  spring-cloud  |   Finchley.SR2   |   分布式服务治理基础    |
|  mybatis-plus  |   3.0.2    |  mybatis-plus  |
|  mysql  |  8.0.22    |      数据库       
|  spring-security-oauth2  |  5.2.2.RELEASE    |      认证鉴权      |
|  redis  |  2.4.0 |       缓存       
|  redisson  |   3.17.0   |   redis分布式锁    |
|  kafka  |  2.13+    |      消息队列      |
|  elasticsearch|8.5.0|       ES       |
|  openfeign  |  2.0+    |    rpc远程调用     |
|  swagger-bootstrap-ui  |  1.9.6    | swagger api ui |
|  swagger  |  2.0+    |  swagger API   |
|  minio  | 8.0.0  |    文件存储服务器     |

## 项目结构
```
ddx -- 父项目
|-ddx-eureka -- 服务注册中心
|-ddx-config -- 配置管理中心
|-ddx-gateway -- 服务网关
|-ddx-web -- web服务管理包
|-ddx-auth -- 认证中心
|-ddx-api -- 内置服务rpc与外置服务 API 提供包
|-ddx-system-manage -- 系统管理服务
|-ddx-log -- 中央日志处理服务
|-ddx-file -- 文件处理服务[占时未实现-需要集成minio]
|-ddx-util -- 公共工具包
|--|-ddx-util-basis 公共基础工具包
|--|-ddx-util-es 公共ES工具包
|--|-ddx-util-kafka 公共kafka工具包
|--|-ddx-util-redis 公共redis工具包
|--|-ddx-util-log-collector logback日志采集工具包
```

## 功能
- 具体功能描述请进入链接查看
  - [ddx-eureka 服务注册中心](https://github.com/LauYi-a/ddxs/tree/master/ddx-eureka)
  - [ddx-config 配置管理中心](https://github.com/LauYi-a/ddxs/tree/master/ddx-config)
  - [ddx-gateway 服务网关](https://github.com/LauYi-a/ddxs/tree/master/ddx-gateway)
  - [ddx-web web服务管理包](https://github.com/LauYi-a/ddxs/tree/master/ddx-web)
  - [ddx-api 服务API提供包](https://github.com/LauYi-a/ddxs/tree/master/ddx-api)
  - [ddx-auth 认证中心](https://github.com/LauYi-a/ddxs/tree/master/ddx-auth)
  - [ddx-system-manage 系统管理服务](https://github.com/LauYi-a/ddxs/tree/master/ddx-system-manage)
  - [ddx-log 中央日志处理服务](https://github.com/LauYi-a/ddxs/tree/master/ddx-log)
  - [ddx-file 文件处理服务](https://github.com/LauYi-a/ddxs/tree/master/ddx-file)
  - [ddx-util 公共工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-util)
  - [ddx-util-basis 公共基础工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-utils/ddx-util-basis)
  - [ddx-util-es 公共ES工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-util/ddx-util-es)
  - [ddx-util-kafka 公共kafka工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-util/ddx-util-kafka)
  - [ddx-util-redis 公共redis工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-util/ddx-util-redis)
  - [ddx-util-log-collector logback日志采集工具包](https://github.com/LauYi-a/ddxs/tree/master/ddx-utils/ddx-util-log-collector)
## 编译环境

> 运行编译需安装环境，以下环境版本实测编译成功：

|  环境   |   版本    |
| :-----: | :-------: |
|  jdk   | 1.8 |
|  maven  |   3.6.0   |
|  lombok  |  1.18.16  |
|  mysql  |   5.7+    |
|  kafka  |   2.13+    |
|  redis  |   3.0.0+    |
|  elasticsearch|8.5.0|
|  minio | 8.0.0 |

## 部署
* Windows
    > 后续补充 
* Linux
    > 后续补充

## 知识库
- [进入飞书查看知识库](https://j0dttt306j.feishu.cn/wiki/wikcnwcfeqzj4ZywAWKiND4X9jd)

## 版本
- 1.0
