package com.ddx.util.es.annotation;

import com.ddx.util.es.common.EsEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: EsDateF
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年12月02日 11:02
 * @Version: 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EsField {
    //默认属性名
    String name()default "" ;
    //数据类型
    EsEnum.DataType type();
    String analyzer()default ""; //分词
    String searchAnalyzer()default ""; //搜索分词
}