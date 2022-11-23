package com.ddx.util.es.dto;

import com.ddx.util.es.annotation.DocId;
import com.ddx.util.es.annotation.EsClass;
import com.ddx.util.es.annotation.EsDataType;
import com.ddx.util.es.annotation.EsField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: LogCollectorEsDTO
 * @Description: 日志采集
 * @Author: YI.LAU
 * @Date: 2022年11月16日 17:18
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EsClass
public class EsLogCollectorDTO {

    @DocId
    private String id;

    @EsField(type = EsDataType.KEYWORD)
    private String env;

    @EsField(type = EsDataType.KEYWORD)
    private String serviceName;

    @EsField(type = EsDataType.KEYWORD)
    private String date;

    @EsField(type = EsDataType.TEXT,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String message;
}
