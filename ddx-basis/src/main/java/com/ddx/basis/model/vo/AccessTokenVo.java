package com.ddx.basis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: AccessTokenVo
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年05月07日  0007
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenVo {

    /**
     * 返回token
     */
    private String access_token;

    /**
     * token类型
     */
    private String token_type;

    /**
     * 过期时间
     */
    private Integer expires_in;

    /**
     *范围
     */
    private String scope;

    /**
     * 用户ID
     */
    private String user_id;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 登入服务
     */
    private String loginService;

    /**
     * 令牌
     */
    private String jti;
}
