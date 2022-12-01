package com.ddx.util.es.service.impl;

import co.elastic.clients.elasticsearch.cat.aliases.AliasesRecord;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.es.annotation.DocId;
import com.ddx.util.es.annotation.EsField;
import com.ddx.util.es.annotation.EsIndex;
import com.ddx.util.es.common.EsEnum;
import com.ddx.util.es.common.EsUtils;
import com.ddx.util.es.config.EsClient;
import com.ddx.util.es.service.IIndexManageServiceApi;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ElasticsearchIndexManageServiceImpl
 * @Description: es 索引管理 实现
 * @Author: YI.LAU
 * @Date: 2022年11月23日 14:03
 * @Version: 1.0
 */
@Slf4j
@Service
public class IndexManageServiceApiImpl implements IIndexManageServiceApi {

    @Autowired
    private EsClient esClient;

    @Override
    public <T> boolean createIndexSettingsMappings(Class<T> tClass) {
        try {
            String index = EsUtils.getIndexOrAlias(tClass,false);
            String alias = EsUtils.getIndexOrAlias(tClass,true);
            if (existsIndex(index)){
                return true;
            }
            return createIndexSettingsMappings(index, alias,tClass);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建索引设置映射
     * @param index
     * @param alias
     * @param tClass
     * @param <T>
     * @return
     */
    private <T> boolean createIndexSettingsMappings(String index, String alias, Class<T> tClass){
        try {
            EsIndex annotation = tClass.getAnnotation(EsIndex.class);
            JSONObject indexConfigJson = new JSONObject();
            //索引设置
            Map<String,Object> settingsMap = Maps.newHashMap();
            settingsMap.put("number_of_shards",annotation.shards());
            settingsMap.put("number_of_replicas",annotation.replicas());
            indexConfigJson.put("settings", settingsMap);

            //Mapping 设置
            Map<String,Object> propertiesMap = Maps.newHashMap();
            Map<String,Object> fieIds = Maps.newHashMap();
            for (Field declaredField : tClass.getDeclaredFields()) {
                declaredField.setAccessible(true);
                JSONObject jsonObject = new JSONObject();
                DocId docId = declaredField.getAnnotation(DocId.class);
                if (docId != null) {
                    EsEnum.DataType type = docId.type();
                    jsonObject.put("type", type.getType());
                    fieIds.put(declaredField.getName(), jsonObject);
                    continue;
                }
                EsField annotationFieId = declaredField.getAnnotation(EsField.class);
                if (annotationFieId != null) {
                    String name = annotationFieId.name();
                    name = "".equals(name) ? declaredField.getName() : name;
                    EsEnum.DataType type = annotationFieId.type();
                    jsonObject.put("type", type.getType());
                    fieIds.put(name, jsonObject);
                }
            }
            propertiesMap.put("properties",fieIds);
            indexConfigJson.put("mappings",propertiesMap);
            Reader indexJson = new StringReader(new StringBuffer().append(indexConfigJson).toString());
            String finalIndex = index;
            String finalAlias = alias;
            CreateIndexRequest req = CreateIndexRequest.of(b ->b.index(finalIndex).aliases(finalAlias, Alias.of(a -> a.isWriteIndex(true)))
                    .withJson(indexJson));
            if (!esClient.getClient().indices().create(req).acknowledged()){
                throw new RuntimeException("创建索引失败");
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("创建索引失败:"+e.getMessage());
        }
    }

    /**
     * 校验索引是否存在
     * @param index
     * @return 存在 true 不存在 false
     */
    private boolean existsIndex(String index) throws IOException {
        boolean isIndex = esClient.getClient().indices().exists(req -> req.index(index)).value();
        if (isIndex){
            return true;
        }
        return false;
    }

    @Override
    public <T> List<String> selectIndexAll() {
        try {
            List<IndicesRecord> indices = esClient.getClient().cat().indices().valueBody();
            assert indices != null;
            return indices.stream().map(IndicesRecord::index).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询索引失败："+e.getMessage());
        }
    }

    @Override
    public List<String> selectAliasAll() {
        try {
            List<AliasesRecord> aliasesRecords = esClient.getClient().cat().aliases().valueBody();
            assert aliasesRecords != null;
            return aliasesRecords.stream().map(AliasesRecord::alias).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询索引别名失败："+e.getMessage());
        }
    }

}
