package com.ddx.util.es.annotation;

import com.ddx.util.es.common.EsEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DocId {

    EsEnum.DataType type();
}