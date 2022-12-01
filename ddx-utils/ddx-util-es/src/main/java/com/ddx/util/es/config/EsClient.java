package com.ddx.util.es.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.ddx.util.es.common.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component("esClient")
public class EsClient implements InitializingBean {

    @Value("${es.nodes}")
    private String nodes;

    @Value("${es.alias}")
    private boolean alias;

    private ElasticsearchClient client;
    private ElasticsearchAsyncClient asyncClient;

    // 同步客户端
    public ElasticsearchClient getClient() {
        return client;
    }

    // 异步客户端
    public ElasticsearchAsyncClient getAsyncClient() {
        return asyncClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] hosts = this.nodes.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for(int i=0;i<hosts.length;i++) {
            String host = hosts[i].split(":")[0];
            int port = Integer.parseInt(hosts[i].split(":")[1]);
            httpHosts[i] = new HttpHost(host, port, "http");
        }
        RestClient restClient = RestClient.builder(httpHosts).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // es 客户端
        this.client = new ElasticsearchClient(transport);
        this.asyncClient = new ElasticsearchAsyncClient(transport);
    }

    /**
     * 使用索引还是使用别名
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> String getAliasOrIndexName(Class<T> clazz){
        return EsUtils.getIndexOrAlias(clazz, alias);
    }
}