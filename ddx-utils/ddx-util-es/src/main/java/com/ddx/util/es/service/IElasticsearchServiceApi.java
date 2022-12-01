package com.ddx.util.es.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.ObjectBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: IElasticsearchService
 * @Description: ES 接口
 * @Author: YI.LAU
 * @Date: 2022年11月16日 09:43
 * @Version: 1.0
 */
public interface IElasticsearchServiceApi {

    /**
     * 查询所有数据
     * @param tClass 查询类
     * @param <T>
     * @return
     */
    public <T> List<T> queryAll(Class<T> tClass);

    /**
     * 按条件查询数据
     * @param query 查询条件
     * @param clazz 查询类
     * @param <T>
     * @return
     */
    public <T> List<T> complexQuery(Query query, Class<T> clazz);

    /**
     * 聚合查询
     * @param query 查询条件
     * @param fn
     * @param clazz 查询类
     * @param <T>
     * @return
     */
    public <T> Aggregate complexQueryAggregations(Query query, Function<Aggregation.Builder, ObjectBuilder<Aggregation>> fn, Class<T> clazz);

    /**
     * 高亮查询
     * @param query 查询条件
     * @param clazz 查询类
     * @param fields 高亮值
     * @param <T>
     * @return
     */
    public <T> List<Map<String, Object>> complexQueryHighlight(Query query, Class<T> clazz, String... fields);

    /**
     * 添加数据
     * @param o
     * @param async 是否异步 true异步 ,false同步
     * @param <T>
     * @return 同步成功返回 数据ID ,异步返回 true 除非抛出异常
     * 同步或异常抛出异常失败时 返回失败信息
     */
    public <T> Object addData(T o, boolean async);


    /**
     * 批量添加数据
     * @param list 数据集合
     * @param async 是否异步 true异步 ,false同步
     * @param <T>
     * @return 成功返回 true ,异步返回 true 除非抛出异常
     * 同步或异步抛出异常失败时 返回失败信息
     */
    public <T> Object addBatchData(List<T> list, boolean async);

    /**
     * 根据条件修改数据
     * @param query 查询条件
     * @param o 修改数据
     * @param async 是否异步 true异步 ,false同步
     * @param <T>
     * @return 成功返回 true 异步返回true
     * 同步或异步抛出异常时 失败返回失败信息
     */
    public <T> boolean upQuery(Query query,T o,boolean async);

    /**
     * 根据文档ID修改数据
     * @param docId 查询文本ID
     * @param o 修改数据
     * @param async 是否异步 true异步 ,false同步
     * @param <T>
     * @return 成功返回 true 异步返回true
     * 同步或异步抛出异常时 失败返回失败信息
     * 可通过 EsResultData.isSuccess()检查请求是否失败
     */
    public <T> boolean upDocId(String docId,T o,boolean async);

    /**
     * 查询文档ID
     * @param docId 查询文本ID
     * @param clazz 查询类
     * @param <T>
     * @return
     */
    public <T> T getDocId(String docId, Class<T> clazz);

    /**
     * 查询指定文档id数据是否存在
     * @param tClass 查询类
     * @param docId 文档id
     * @param <T>
     * @return
     */
    public <T> boolean docIdExists(Class<T> tClass, String docId);

    /**
     * 根据文档ID 进行删除
     * @param docId 文档ID
     * @param clazz 数据类
     * @param <T>
     * @return
     */
    public <T> boolean delDocId( String docId,Class<T> clazz);

    /**
     * 根据条件进行删除
     * @param query 查询条件
     * @param clazz 数据类
     * @param <T>
     * @return
     */
    public <T> boolean delQuery(Query query,Class<T> clazz);
}
