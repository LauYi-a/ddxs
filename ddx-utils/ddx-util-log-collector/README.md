<h1 align="center">ddx-util-log-collector</h1>

## 简介
日志采集模块，主要用作微服务运行日志的采集工作，将服务分部在多个节点运行时产生的日志统一收集，
收集之后传输到 kafka 中然后通过日志中央系统统一处理日志进行存储、查询、分析

## 介绍
- 采用 logBack 自定义 appender 来实现监控系统通过 log4j slf4j 打印的日志，监控到日志产生时
将日志以异步线程池的方式发送到 Kafka 中。
    - 使用服务名称加 `-log`后缀 作为 Kafka 的 topic。
    - 线程池根据cpu数量,计算出合理的线程并发数，
    最长等待线程队列为1000个，线程存活时间为1小时，
    超过一小时没有日志进行传输将会自动关闭线程池，下次调用时在创建线程池

## 使用方法
ddx-util-log-collector 分布式微服务日志采集使用教程 [点击跳转飞书文档查看](https://j0dttt306j.feishu.cn/docx/WLHVdaLxGoZRO2xkSpyc3K6SnHe) 
