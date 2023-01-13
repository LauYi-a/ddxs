package com.ddx.log.model;

import com.ddx.util.es.annotation.DocId;
import com.ddx.util.es.annotation.EsField;
import com.ddx.util.es.annotation.EsIndex;
import com.ddx.util.es.common.EsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@EsIndex(indexName = "log",strategy = EsEnum.IndexStrategy.STRATEGY_DATE_YYYY_MMM)
public class LogCollectorDto {

    @DocId(type = EsEnum.DataType.KEYWORD)
    private String id;

    @EsField(type = EsEnum.DataType.KEYWORD)
    private String env;

    @EsField(type = EsEnum.DataType.KEYWORD)
    private String serviceName;

    @EsField(type = EsEnum.DataType.DATE_NANOS)
    private Date date;

    @EsField(type = EsEnum.DataType.TEXT,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String message;
}
