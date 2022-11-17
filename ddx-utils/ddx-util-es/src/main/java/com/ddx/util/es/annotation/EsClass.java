package com.ddx.util.es.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EsClass {

    /**
     * 索引: 前缀 index + 默认类名( 自动转小写)
     * 默认类名可以修改为我们自定义
     * @return
     */
    String index() default "";
    /**
     * 别名: 前缀alias + 默认类名( 自动转小写)
     *默认类名可以修改为我们自定义
     * @return
     */
    String alias() default "" ;
    /*
     *  分片
     */
    int shards()default 1;

    /**
     *  分片副本
     * @return
     */
    int replicas()default 1;
}