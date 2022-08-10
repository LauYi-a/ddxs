package com.ddx.common.entity;

import lombok.Data;

/**
 * @ClassName: JwtInformation
 * @Description: JWT令牌相关的信息实体类
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Data
public class JwtInformation {
    /**
     * JWT令牌唯一ID
     */
    private String jti;

    /**
     * 过期时间，单位秒，距离过期时间还有多少秒
     */
    private Long expireIn;
}
