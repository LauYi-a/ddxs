package com.ddx.util.es.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.aliases.AliasesRecord;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.util.ObjectBuilder;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.es.annotation.*;
import com.ddx.util.es.common.EsUtil;
import com.ddx.util.es.config.EsClient;
import com.ddx.util.es.result.EsResultData;
import com.ddx.util.es.service.IElasticsearchServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: ElasticsearchServiceImpl
 * @Description: ES API 实现
 * @Author: YI.LAU
 * @Date: 2022年11月16日 09:46
 * @Version: 1.0
 */
@Slf4j
@Service
public class ElasticsearchServiceApiImpl implements IElasticsearchServiceApi {

    @Autowired
    private EsClient esClient;

    @Override
    public <T> EsResultData createIndexSettingsMappings(Class<T> tClass) {
        try {
            String index = EsUtil.getIndex(tClass);
            String alias = EsUtil.getAlias(tClass);
            EsResultData validatedIndex = validatedIndex(index,alias);
            if (!EsResultData.isSuccess(validatedIndex)){
                return validatedIndex;
            }
            return createIndexSettingsMappings(index, alias,tClass);
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
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
    private <T> EsResultData createIndexSettingsMappings(String index, String alias, Class<T> tClass){
        try {
            EsClass annotation = tClass.getAnnotation(EsClass.class);
            int shards = annotation.shards();
            int replicas = annotation.replicas();
            StringBuilder stringBuilder = new StringBuilder("{");
            stringBuilder.append(
                    "\"settings\": {\n"
                            + "    \"number_of_shards\": "
                            + shards
                            + ",\n"
                            + "    \"number_of_replicas\": "
                            + replicas
                            + "\n"
                            + "  },");
            stringBuilder.append("\"mappings\": {\n" + "    \"properties\": ");
            JSONObject jsonObject = new JSONObject();
            for (Field declaredField : tClass.getDeclaredFields()) {
                declaredField.setAccessible(true);
                JSONObject jsonObject1 = new JSONObject();
                DocId DocId = declaredField.getAnnotation(DocId.class);
                if (DocId != null) {
                    jsonObject1.put("type", EsDataType.LONG.getType());
                    jsonObject.put(declaredField.getName(), jsonObject1);
                    continue;
                }

                EsField annotation1 = declaredField.getAnnotation(EsField.class);
                if (annotation1 != null) {
                    String name = annotation1.name();
                    name = "".equals(name) ? declaredField.getName() : name;
                    EsDataType type = annotation1.type();
                    String analyzer = annotation1.analyzer();
                    String searchAnalyzer = annotation1.searchAnalyzer();
                    jsonObject1.put("type", type.getType());
                    if (!"".equals(analyzer)) {
                        jsonObject1.put("analyzer", analyzer);
                    }
                    if (!"".equals(searchAnalyzer)) {
                        jsonObject1.put("search_analyzer", searchAnalyzer);
                    }
                    jsonObject.put(name, jsonObject1);
                }
            }
            Assert.isTrue(jsonObject.size() > 0, "请添加es相关注解");
            stringBuilder.append(jsonObject);
            stringBuilder.append("}}");
            Reader queryJson = new StringReader(stringBuilder.toString());
            String finalIndex = index;
            String finalAlias = alias;
            CreateIndexRequest req = CreateIndexRequest.of(b ->b.index(finalIndex).aliases(finalAlias, Alias.of(a -> a.isWriteIndex(true))).withJson(queryJson));
            if (esClient.getClient().indices().create(req).acknowledged()){
                return EsResultData.result(EsEnum.EsResult.STATUS_FALSE);
            }else{
                return EsResultData.result(EsEnum.EsResult.STATUS_FALSE);
            }
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    /**
     * 校验索引
     * @param index
     * @param alias
     * @return
     */
    private EsResultData validatedIndex(String index, String alias){
        EsResultData<List<String>> indexRD = indexAll();
        if (!EsResultData.isSuccess(indexRD)){
            return indexRD;
        }
        List<String> indexs = indexRD.getData();
        EsResultData<List<String>> aliasRD = aliasAll();
        if (!EsResultData.isSuccess(aliasRD)){
            return aliasRD;
        }
        List<String> aliass = aliasRD.getData();
        if(indexs.contains(index)||aliass.contains(alias)){
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,"索引已存在");
        }
        return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,"校验成功");
    }

    @Override
    public EsResultData<List<String>> indexAll() {
        try {
            List<IndicesRecord> indices = esClient.getClient().cat().indices().valueBody();
            assert indices != null;
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,
                    indices.stream().map(IndicesRecord::index).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询索引失败："+e.getMessage());
        }
    }

    @Override
    public EsResultData<List<String>> aliasAll() {
        try {
            List<AliasesRecord> aliasesRecords = esClient.getClient().cat().aliases().valueBody();
            assert aliasesRecords != null;
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,
                    aliasesRecords.stream().map(AliasesRecord::alias).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询索引别名失败："+e.getMessage());
        }
    }

    @Override
    public <T> EsResultData<List<T>> queryAll(Class<T> tClass) {
        String indexOrAliasName = esClient.getClassAliasOrIndex(tClass);
        return queryAll(tClass,indexOrAliasName);
    }

    private <T> EsResultData<List<T>> queryAll(Class<T> tClass,String indexOrAliasName) {
        try {
            List<T> list = new ArrayList<>();
            SearchResponse<T> search = esClient.getClient().search(s -> s.index(indexOrAliasName).query(q -> q.matchAll(m -> m)), tClass);
            for (Hit<T> hit : search.hits().hits()) {
                list.add(hit.source());
            }
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,list);
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> EsResultData<List<T>> complexQuery(Query query, Class<T> clazz) {
       try {
           List<T> list = new ArrayList<>();
           SearchResponse<T> response = esClient.getClient().search(s -> s.index(EsUtil.getAlias(clazz)).query(query), clazz);
           List<Hit<T>> hits = response.hits().hits();
           for (Hit<T> hit : hits) {
               list.add(hit.source());
           }
           return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,list);
       }catch (Exception e){
           return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询数据失败："+e.getMessage());
       }
    }

    @Override
    public <T> EsResultData<Aggregate> complexQueryAggregations(Query query, Function<Aggregation.Builder, ObjectBuilder<Aggregation>> fn, Class<T> clazz) {
        try {
            SearchResponse<T> response = esClient.getClient().search(s ->s.index(EsUtil.getAlias(clazz))
                    .size(0) // 不需要显示数据 ,只想要聚合结果
                    .query(query)
                    .aggregations("aggregations", fn), clazz);
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,response.aggregations().get("aggregations"));
        }catch (Exception e){
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> EsResultData<List<Map<String, Object>>> complexQueryHighlight(Query query, Class<T> clazz, String... fields) {
        try {
            List<Map<String ,Object>> list =  new ArrayList<Map<String ,Object>>();
            String index = esClient.getClassAliasOrIndex(clazz);
            Highlight of = Highlight.of(h -> {
                for (String field : fields) {
                    h.fields(field, h1 -> h1.preTags("<font color='red'>").postTags("</font>"));
                }
                return h;
            });
            SearchResponse<T> response = esClient.getClient().search(s -> s.index(index).query(query).highlight(of), clazz);
            for (Hit<T> hit : response.hits().hits()) {
                Map<String ,Object> map=new HashMap<>();
                map.put("source",hit.source());
                map.put("highlight",hit.highlight());
                list.add(map);
            }
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,list);
        }catch (Exception e){
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,"查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> EsResultData addData(T o, boolean async) {
        try {
            Object id = EsUtil.getId(o);
            if (id == null) {
                id = UUID.randomUUID().toString();
            }
            IndexRequest.Builder<T> indexReqBuilder = new IndexRequest.Builder<>();
            indexReqBuilder.index(EsUtil.getAlias(o.getClass()));
            indexReqBuilder.id(String.valueOf(id));
            indexReqBuilder.document(o);
            if (async) {
                esClient.getAsyncClient().index(indexReqBuilder.build());
                return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
            }
            IndexResponse response = esClient.getClient().index(indexReqBuilder.build());
            assert response != null;
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,new StringBuffer(response.id()));
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData addBatchData(List<T> list, boolean async) {
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();
            for (T o : list) {
                Object id = EsUtil.getId(o);
                if (id == null) {
                    id = UUID.randomUUID().getMostSignificantBits();
                }
                Object finalId = id;
                br.operations(op -> op.index(idx -> idx.index(EsUtil.getAlias(o.getClass())).id(String.valueOf(finalId)).document(o)));
            }
            if (async) {
                esClient.getAsyncClient().bulk(br.build());
                return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
            }
            BulkResponse result = esClient.getClient().bulk(br.build());
            StringBuffer sb = new StringBuffer();
            assert result != null;
            boolean isOK = true;
            if (result.errors()) {
                isOK = false;
                for (BulkResponseItem item : result.items()) {
                    if (item.error() != null) {
                        sb.append(item.error().reason());
                    }
                }
            }
            return EsResultData.result(isOK?EsEnum.EsResult.STATUS_TRUE:EsEnum.EsResult.STATUS_FALSE, isOK?true:sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData upQuery(Query query, T o, boolean async) {
        try {
            Class<?> aClass = o.getClass();
            String index = esClient.getClassAliasOrIndex(aClass);
            //获取全部字段和字段的名称以及需要修改的值
            StringBuilder stringBuilder=new StringBuilder();
            for (Field declaredField : aClass.getDeclaredFields()) {
                declaredField.setAccessible(true);
                Object o1 = declaredField.get(o);
                if (o1==null) {
                    continue;
                }
                declaredField.setAccessible(true);
                EsField field = declaredField.getAnnotation(EsField.class);
                String name = field.name();
                name = "".equals(name) ? declaredField.getName() : name;
                String finalName = name;
                String str = EsUtil.renderString("ctx._source['${name}'] = '${value}';",
                        new HashMap() {{
                            put("name", finalName);
                            put("value",o1) ;
                        }});
                stringBuilder.append(str);
            }
            UpdateByQueryRequest of = UpdateByQueryRequest.of(u -> u.index(index).query(query).script(s -> s.inline(i -> i.source(stringBuilder.toString()))));
            if (async) {
                esClient.getAsyncClient().updateByQuery(of);
                return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
            }
            esClient.getClient().updateByQuery(of);
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData upDocId(String docId, T o, boolean async) {
        try {
            //先查询出来
            Object docId1 = getDocId(docId, o.getClass());
            if (docId1==null) {
                throw new Exception("没有docId:"+docId+"这条数据");
            }
            //进行对象拷贝
            BeanUtils.copyProperties(o,docId1);
            String index = esClient.getClassAliasOrIndex(o.getClass());
            if (async) {
                esClient.getAsyncClient().update(UpdateRequest.of(d -> d.index(index).id(docId).doc(o)), o.getClass());
                return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
            }
            esClient.getClient().update(UpdateRequest.of(d -> d.index(index).id(docId).doc(o)), o.getClass());
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,true);
        }catch (Exception e){
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    public <T> EsResultData<T> getDocId(String docId, Class<T> clazz) {
        try {
            GetResponse<T> response = esClient.getClient().get(g -> g.index(EsUtil.getAlias(clazz)).id(docId), clazz);
            assert response != null;
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,response.source());
        } catch (IOException e) {
            e.printStackTrace();
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData docIdExists(Class<T> tClass, String docId) {
        try {
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE,
                    esClient.getClient().exists(s -> s.index(EsUtil.getAlias(tClass)).id(docId)).value());
        }catch (Exception e){
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData delDocId(String docId, Class<T> clazz) {
        try {
            String index = esClient.getClassAliasOrIndex(clazz);
            DeleteRequest de = DeleteRequest.of(d -> d.index(index).id(docId));
            esClient.getClient().delete(de);
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE);
        }catch (Exception e){
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }

    @Override
    public <T> EsResultData delQuery(Query query, Class<T> clazz) {
        try {
            String index = esClient.getClassAliasOrIndex(clazz);
            DeleteByQueryRequest de = DeleteByQueryRequest.of(d -> d.index(index).query(query));
            esClient.getClient().deleteByQuery(de);
            return EsResultData.result(EsEnum.EsResult.STATUS_TRUE);
        }catch (Exception e){
            return EsResultData.result(EsEnum.EsResult.STATUS_FALSE,e.getMessage());
        }
    }
}
