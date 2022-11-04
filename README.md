<h1 align="center">ddx</h1>

## 简介
- ddx 创建于 2022 年 4 月 作者 YI.LAU 
- 目的作为模板化系统架构，开箱即用，满足个人学习以及企业快速迭代
- 此开源项目为个人开发，不限制任何商业使用和个人研究，使用之前请先点个Star对我进行鼓励
- 利用此开源项目参与的一切违法、色情相关的活动均与本源码无关，请勿以身示法

## 介绍
- 此项目作为分布式微服务架构，前后端分离，前端使用vue3+vite+element-plus构建具体点击查看[前端GitHub源码详情](https://github.com/LauYi-a/ddx-web) 
- 后端分布式微服务主要分为 【auth 认证服务】【 common 通用公共服务】【basis 通用基础服务】【eureka 服务注册中心】 【gateway 服务网关】 【system-manage 系统管理服务】
1. auth 主要作用为 服务验证、用户鉴权，主要采用oauth2来实现服务验证
2. eureka 主要作用为 服务注册、服务发现
3. gateway 主要作用为 路由转发、服务限流、接口黑白名单验证、跨域设置、请求校验、接口重放限制、权限认证、token校验、全局异常处理
4. system-manage 主要用来管理实现系统的基础功能 
5. common 通用公共服务模块 主要集成一些公共基础工具实现，可引用于各各服务模块之中，但需要注意jar包冲突等问题
6. basis 通用基础服务模块 为了减少 common 引入时jar包冲突问题 web服务可直接引入common服务common中包含了basis服务，jar包服务时可直接引入basis服务，应为common中maven引入了web强依赖性的jar包，在不是web项目时引入了common是需要排除相关jar包的。所以将公共服务分为两个模块在不同需求时分开引入
7. file 通用性文件处理服务模块处理 上传文件 下载文件 等文件处理
8. log 分布式日志监控服务，收集各服务日志进行归档，提供api 进行查询日志
## 技术点
> 以下技术栈为框架主要使用技术点不包含全部使用技术：

|  技术点   |   版本    | 描述 |
| :-----: | :-------: | :-----: |
|   spring-boot   | 2.0.8.RELEASE | 微服务基础
|  spring-cloud  |   Finchley.SR2   | 分布式服务组件基础
| redis  |  2.4.0 | 缓存
|  redisson  |   3.17.0   | redis分布式锁
|  mybatis-plus  |   3.0.2    | mybatis-plus
|  mysql  |  8.0.22    | 数据库
|  spring-security-oauth2  |  5.2.2.RELEASE    | 认证鉴权
|  kafka  |  2.13+    | 消息队列
|  openfeign  |  2.0+    | rpc远程调用
|  swagger-bootstrap-ui  |  1.9.6    | swagger api ui
|  swagger  |  2.0+    | swagger API
|  bcprov-jdk15on  |  1.6.5   | sm4国密加密算法
|  velocity  |  1.7   | 模板引擎
|  lombok  |  1.18.16  | lombok工具
|  hutool  |  4.6.5  | hutool工具
|  fastjson  |  1.2.31  | fastjson工具

## 项目结构
```
ddx -- 父项目
|-ddx-auth -- 认证中心
|-ddx-common -- 通用公共服务
|-ddx-basis -- 通用基础服务
|-ddx-config -- 配置管理中心
|-ddx-eureka -- 服务注册中心
|-ddx-gateway -- 服务网关
|-ddx-system-manage -- 系统管理服务
|-ddx-file -- 文件处理服务
|-ddx-log -- 日志监控服务
| |-ddx-log-api 日志API服务
```

## 功能
- 该框架`system-manage`服务已完成基础系统管理的一些基本功能
    >如：用户管理、角色管理、权限管理、菜单管理、系统参数管理、白名单管理
- 该框架`auth` 服务已完成用户登入，用户退出，用户鉴权,代码生成器等功能
- 该框架`config`服务已实现服务配置管理，可按环境进行管理yml文件
    >切换环境只需在 `config` resource/config目录下配置对应服务名称yml文件并加环境后缀 如`ddx-auth-xxx.yml`
之后在对应服务的 `bootstrap.yml`文件中配置`profiles: active: xxx`属性。
如打包时需要打包指定环境的配置文件需要修改`pom.xml`文件的`<profiles.active>xxx</profiles.active>`
- 该框架`eureka`服务已实服务注册服务发现等功能，如需将服务注册到`eureka`服务中进行管理需要在服务的`bootstrap.yml`文件中配置注册中心`eureka`的配置
- 该框架`common`服务实现了公用功能如：
    >通用kafka生产端接口、rpc令牌信息中继、微服务请求过滤器、请求解析信息传递、redis分布式锁实现、reids缓存模板工具、OAuth2.0工具，redis 配置、redis分布式锁配置、请求拦截日志、mybatis自动填充配置
- 该框架 `basis`服务主要实现了基础公共功能如：
    >通用业务系统异常处理、通用数据响应体处理、加解密实现（RSA/SM3/SM4）、通用线程池工具、通用分页工具、通用全局流水号获取工具、通用常量池、通用枚举、通用业务实体类
## 编译环境

> 运行编译需安装环境，以下环境版本实测编译成功：

|  环境   |   版本    |
| :-----: | :-------: |
|   jdk   | 1.8 |
|  maven  |   3.6.0   |
| lombok  |  1.18.16  |
|  mysql  |   5.7+    |
|  kafka  |   2.13+    |
|  redis  |   3.0.0+    |

## 部署
* Windows
    > 后续补充 
* Linux
    > 后续补充

## 版本
- 1.0

## 相关文档
- 后续补充