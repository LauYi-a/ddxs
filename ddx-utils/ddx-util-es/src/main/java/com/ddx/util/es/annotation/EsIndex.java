package com.ddx.util.es.annotation;

import com.ddx.util.es.common.EsEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EsIndex {

    /**
     * 索引: 前缀默认 index-
     * @return
     */
    String indexPrefix() default "index-";

    /**
     * 别名: 前缀默认 alias-
     * @return
     */
    String aliasPrefix() default "alias-" ;

    /**
     * 索引名,为空取类名
     * @return
     */
    String indexName() default "" ;

    /**
     * 索引策略
     * @return
     */
    EsEnum.IndexStrategy strategy();

    /*
     *  分片
     */
    int shards() default 1;

    /**
     *  分片副本
     * @return
     */
    int replicas()default 1;
}