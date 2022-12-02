package com.ddx.util.es.common;

import java.util.Date;

/**
 * @ClassName: EsEnum
 * @Description: ES 使用枚举
 * @Author: YI.LAU
 * @Date: 2022年11月16日 09:53
 * @Version: 1.0
 */
public class EsEnum {

    EsEnum (){};

    /**
     * 索引策略
     */
    public enum IndexStrategy{

        STRATEGY_DATE_YYYY(DateUtil.date2Str(new Date(),DateUtil.DATE_FORMAT_1)),
        STRATEGY_DATE_YYYY_MMM(DateUtil.date2Str(new Date(),DateUtil.DATE_FORMAT_2)),
        STRATEGY_DATE_YYYY_MMM_DD(DateUtil.date2Str(new Date(),DateUtil.DATE_FORMAT_3));

        private Object strategy;

        IndexStrategy(String strategy) {
            this.strategy = strategy;
        }

        public Object getStrategy() {
            return strategy;
        }
    }

    public enum DataType {
        TEXT("text"),
        KEYWORD("keyword"),
        FLOAT("float"),
        LONG("long"),
        INTEGER("integer"),
        SHORT("short"),
        DOUBLE("double"),
        HALF_FLOAT("half_float"),
        SCALED_FLOAT("scaled_float"),
        BYTE("byte"),
        DATE("date"),
        DATE_NANOS("date_nanos"),
        BOOLEAN("boolean"),
        RANGE("rang"),
        BINARY("binary"),
        ARRAY("array"),
        OBJECT("object"),
        NESTED("nested"),
        GEO_POINT("geo_point"),
        GEO_SHAPE("geo_shape"),
        IP("ip"),
        COMPLETION("completion"),
        TOKEN_COUNT("token_count"),
        ATTACHMENT("attachment"),
        PERCOLATOR("percolator");

        private String type;

        DataType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
