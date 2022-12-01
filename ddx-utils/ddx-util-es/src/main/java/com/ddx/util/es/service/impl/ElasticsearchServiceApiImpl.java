package com.ddx.util.es.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.ObjectBuilder;
import com.ddx.util.es.annotation.EsField;
import com.ddx.util.es.common.EsUtils;
import com.ddx.util.es.config.EsClient;
import com.ddx.util.es.service.IElasticsearchServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

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
    public <T> List<T> queryAll(Class<T> tClass) {
        String indexOrAliasName = esClient.getAliasOrIndexName(tClass);
        try {
            List<T> list = new ArrayList<>();
            SearchResponse<T> search = esClient.getClient().search(s -> s.index(indexOrAliasName).query(q -> q.matchAll(m -> m)), tClass);
            for (Hit<T> hit : search.hits().hits()) {
                list.add(hit.source());
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> List<T> complexQuery(Query query, Class<T> clazz) {
       try {
           List<T> list = new ArrayList<>();
           SearchResponse<T> response = esClient.getClient().search(s -> s.index(esClient.getAliasOrIndexName(clazz)).query(query), clazz);
           List<Hit<T>> hits = response.hits().hits();
           for (Hit<T> hit : hits) {
               list.add(hit.source());
           }
           return list;
       }catch (Exception e){
           e.printStackTrace();
           throw new RuntimeException("查询数据失败："+e.getMessage());
       }
    }

    @Override
    public <T> Aggregate complexQueryAggregations(Query query, Function<Aggregation.Builder, ObjectBuilder<Aggregation>> fn, Class<T> clazz) {
        try {
            SearchResponse<T> response = esClient.getClient().search(s ->s.index(esClient.getAliasOrIndexName(clazz))
                    .size(0)// 不需要显示数据 ,只想要聚合结果
                    .query(query)
                    .aggregations("aggregations", fn), clazz);
            return response.aggregations().get("aggregations");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> List<Map<String, Object>> complexQueryHighlight(Query query, Class<T> clazz, String... fields) {
        try {
            List<Map<String ,Object>> list =  new ArrayList<Map<String ,Object>>();
            String index = esClient.getAliasOrIndexName(clazz);
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
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> Object addData(T o, boolean async) {
        String index = esClient.getAliasOrIndexName(o.getClass());
        try {
            Object id = EsUtils.getId(o);
            id = Objects.nonNull(id)?id:UUID.randomUUID().toString();
            IndexRequest.Builder<T> indexReqBuilder = new IndexRequest.Builder<>();
            indexReqBuilder.index(index);
            indexReqBuilder.id(String.valueOf(id));
            indexReqBuilder.document(o);
            if (async) {
                esClient.getAsyncClient().index(indexReqBuilder.build());
                return true;
            }
            IndexResponse response = esClient.getClient().index(indexReqBuilder.build());
            assert response != null;
            return new StringBuffer(response.id());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("新增 "+index+" 索引数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> Object addBatchData(List<T> list, boolean async) {
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();
            for (T o : list) {
                Object id = EsUtils.getId(o);
                id = Objects.nonNull(id)?id:UUID.randomUUID().toString();
                Object finalId = id;
                br.operations(op -> op.index(idx -> idx
                        .index(esClient.getAliasOrIndexName(o.getClass()))
                        .id(String.valueOf(finalId))
                        .document(o)));
            }
            if (async) {
                esClient.getAsyncClient().bulk(br.build());
                return true;
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
            return isOK?true:sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("批量新增索引数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> boolean upQuery(Query query, T o, boolean async) {
        String index = esClient.getAliasOrIndexName(o.getClass());
        try {
            //获取全部字段和字段的名称以及需要修改的值
            StringBuilder stringBuilder=new StringBuilder();
            for (Field declaredField : o.getClass().getDeclaredFields()) {
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
                String str = EsUtils.renderString("ctx._source['${name}'] = '${value}';",
                        new HashMap() {{
                            put("name", finalName);
                            put("value",o1) ;
                        }});
                stringBuilder.append(str);
            }
            UpdateByQueryRequest of = UpdateByQueryRequest.of(u -> u.index(index).query(query).script(s -> s.inline(i -> i.source(stringBuilder.toString()))));
            if (async) {
                esClient.getAsyncClient().updateByQuery(of);
                return true;
            }
            esClient.getClient().updateByQuery(of);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"根据条件修改数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> boolean upDocId(String docId, T o, boolean async) {
        String index = esClient.getAliasOrIndexName(o.getClass());
        try {
            //先查询出来
            Object docId1 = getDocId(docId, o.getClass());
            if (docId1==null) {
                throw new RuntimeException("没有docId:"+docId+"这条数据");
            }
            //进行对象拷贝
            BeanUtils.copyProperties(o,docId1);
            if (async) {
                esClient.getAsyncClient().update(UpdateRequest.of(d -> d.index(index).id(docId).doc(o)), o.getClass());
                return true;
            }
            esClient.getClient().update(UpdateRequest.of(d -> d.index(index).id(docId).doc(o)), o.getClass());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"根据文档ID"+docId+"修改数据失败："+e.getMessage());
        }
    }

    public <T> T getDocId(String docId, Class<T> clazz) {
        String index = esClient.getAliasOrIndexName(clazz);
        try {
            GetResponse<T> response = esClient.getClient().get(g -> g.index(index).id(docId), clazz);
            assert response != null;
            return response.source();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"获取文档ID失败："+e.getMessage());
        }
    }

    @Override
    public <T> boolean docIdExists(Class<T> tClass, String docId) {
        String index = esClient.getAliasOrIndexName(tClass);
        try {
            return esClient.getClient().exists(s -> s.index(index).id(docId)).value();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"判断文档ID"+docId+"是否存在："+e.getMessage());
        }
    }

    @Override
    public <T> boolean delDocId(String docId, Class<T> clazz) {
        String index = esClient.getAliasOrIndexName(clazz);
        try {
            DeleteRequest de = DeleteRequest.of(d -> d.index(index).id(docId));
            esClient.getClient().delete(de);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"根据文档ID"+docId+"删除数据失败："+e.getMessage());
        }
    }

    @Override
    public <T> boolean delQuery(Query query, Class<T> clazz) {
        String index = esClient.getAliasOrIndexName(clazz);
        try {
            DeleteByQueryRequest de = DeleteByQueryRequest.of(d -> d.index(index).query(query));
            esClient.getClient().deleteByQuery(de);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("索引"+index+"根据条件删除数据失败："+e.getMessage());
        }
    }
}
