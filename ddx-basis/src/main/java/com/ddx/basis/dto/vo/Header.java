package com.ddx.basis.dto.vo;

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
    private String serialNumber;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 请求路由
     */
    private String url;

    /**
     * 请求用户IP
     */
    private String ip;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 请求或响应类型
     */
    private String contentType;

    /**
     * 请求或响应时间
     */
    private String dateTime;
}
