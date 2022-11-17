package com.ddx.util.es.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EsField {
    //默认属性名
    String name()default "" ;
    //数据类型
    EsDataType type();
    String analyzer()default ""; //分词
    String searchAnalyzer()default ""; //搜索分词
}