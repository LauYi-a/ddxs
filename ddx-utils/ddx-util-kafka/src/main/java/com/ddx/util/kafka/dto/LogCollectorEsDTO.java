package com.ddx.util.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: LogCollectorEsDTO
 * @Description: 日志采集到ES
 * @Author: YI.LAU
 * @Date: 2022年11月16日 17:18
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogCollectorEsDTO {

    /**
     * 文档ID
     */
    private String docId;

    /**
     * 本机IP
     */
    private String ip;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 日志内容
     */
    private String text;
}
