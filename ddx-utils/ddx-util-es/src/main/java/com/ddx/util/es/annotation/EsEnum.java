package com.ddx.util.es.annotation;

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
     * 索引前缀枚举
     */
    public enum EsIndexPrefix{

        INDEX_PREFIX("index-"),
        ALIAS_PREFIX("alias-");

        private String prefix;

        EsIndexPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public enum EsResult{
        STATUS_TRUE(true,"ES操作成功"),
        STATUS_FALSE(false,"ES操作失败");

        private boolean isOk;
        private String msg;

        EsResult(boolean isOk,String msg){
            this.isOk = isOk;
            this.msg = msg;
        }

        public boolean isOk() {
            return isOk;
        }

        public String getMsg() {
            return msg;
        }
    }
}
