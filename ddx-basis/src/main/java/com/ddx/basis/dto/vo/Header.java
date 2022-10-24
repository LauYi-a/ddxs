package com.ddx.basis.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Header
 * @Description: 请求响应头
 * @Author: YI.LAU
 * @Date: 2022年09月23日 16:29
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    /**
     * 请求流水号
     */
    @JsonProperty(index = 1)
    private String serialNumber;

    /**
     * 日志类型
     */
    @JsonProperty(index = 2)
    private String type;

    /**
     * 用户名
     */
    @JsonProperty(index = 3)
    private String nickname;

    /**
     * 用户ID
     */
    @JsonProperty(index = 4)
    private String userId;

    /**
     * 请求用户IP
     */
    @JsonProperty(index = 5)
    private String ip;

    /**
     * 请求路由
     */
    @JsonProperty(index = 6)
    private String url;

    /**
     * 请求或响应类型
     */
    @JsonProperty(index = 7)
    private String contentType;

    /**
     * 请求或响应时间
     */
    @JsonProperty(index = 8)
    private String dateTime;
}
