package com.ddx.util.es.service;

import java.util.List;

/**
 * @ClassName: IElasticsearchIndexManageService
 * @Description: es 索引管理
 * @Author: YI.LAU
 * @Date: 2022年11月23日 13:51
 * @Version: 1.0
 */
public interface IIndexManageServiceApi {

    /**
     * 按照 tClass类的 indexName 标记的注解创建索引，
     * EsClass未设置 indexName 默认使用类名创建索引名与别名
     * 按照 tClass类的 DocId与EsField 标记的注解设置映射
     * @param tClass 索引类
     * @param <T>
     * @return EsResultData 可通过 EsResultData.isSuccess() 检查请求失败与否
     */
    public <T> boolean createIndexSettingsMappings(Class<T> tClass);

    /**
     * 查询全部索引名
     * @return 可通过 EsResultData.isSuccess() 检查请求是否失败
     */
    public <T> List<String> selectIndexAll();

    /**
     * 查询全部索引别名
     * @return 可通过 EsResultData.isSuccess() 检查请求是否失败
     */
    public <T> List<String> selectAliasAll();

}
