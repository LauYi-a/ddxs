<h1 align="center">ddx-util-es</h1>

## 简介
es 工具模块，对 elasticsearch API 进行封装,使得调用 es 客户端 API 服务更加友好
## 介绍
1. 主要对 es 连接客户端相关工厂进行统一的配置封装，支持单节点与集群模式并同时支持同步异步客户端调用
2. 将 elasticsearch API 进行了封装，实现了对文档索引的基本操作 增、删、查、改、批量删除、创建索引、查询索引 等功能，
并使用自定义注解的方式标记 es 索引以及字段的类型，在ES实体类上标记相关自定义注解在调用接口时可自动识别索引与字段类型，在创建索引时根据注解标识创建。
3. es 工具模块，以泛型模板式的接口形式提供给第三方服务调用，达到接口的统一性，方便后期维护与改造
## 集成
#### 1、添加 maven 
```xml
<!-- es -->
<dependency>
    <groupId>com.ddx</groupId>
    <artifactId>ddx-util-es</artifactId>
    <version>${ddx-util-es.version}</version>
</dependency>
```
#### 2、配置 es yml 配置
```yaml
es:
  nodes: 127.0.0.1:9200
  alias: true
```
- nodes: 多个使用逗号分割
- alias: 是否使用别名的方式访问

#### 3、在 Application 启动类配置扫描包
```java
@ComponentScan(basePackages = {"com.ddx.util.es.*"})
public class Application {
}
```
## 示例代码
1.注解示例
```java
@EsIndex(indexPrefix = "xxx-",aliasPrefix = "xxx-",indexName="xxx",strategy = EsEnum.IndexStrategy.STRATEGY_DATE_YYYY_MMM)
public class ExampleDto{
    
    @DocId(type = EsEnum.DataType.KEYWORD)
    private String id;

    @EsField(type = EsEnum.DataType.KEYWORD)
    private String test;

    @EsField(type = EsEnum.DataType.DATE_NANOS)
    private Date date;

    @EsField(type = EsEnum.DataType.TEXT,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String message;
}
```
 1. @EsIndex 索引注解
    - indexPrefix  索引: 前缀默认 index-
    - aliasPrefix 别名: 前缀默认 alias-
    - indexName 索引名,为空取类名
    - strategy 索引策略
    - shards  分片默认1
    - replicas 分片副本默认1
 2. @DocId 文档 ID 注解 
    - type ID类型
 3. @EsField 文档 字段注解 
    - name 属性名
    - type 数据类型
    - analyzer 分词
    - searchAnalyzer 搜索分词

2.接口示例
> 索引操作
```java
public class Example {
    @Autowired
    private IIndexManageServiceApi iIndexManageServiceApi;
    
    public void createIndex(){
        iIndexManageServiceApi.createIndexSettingsMappings(ExampleDto.getClass());
    }
}
```
- 创建索引会按照 Class类的 indexName 标记的注解创建索引，EsIndex 未设置 indexName 
默认使用类名创建索引名与别名，按照 Class类的 DocId与EsField 标记的注解设置映射

> 数据操作
```java
public class Example {
    @Autowired
    private IElasticsearchServiceApi iElasticsearchServiceApi;
    
    public void createIndex(){
       ElasticsearchServiceApi.addData(t, true);
    }
}
```
- 会按照传入实体类标记的注解添加数据 删除 修改 查询等操作
