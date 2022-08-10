package com.ddx.auth.config;

import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;


/**
 * @ClassName: JwtEnhanceAccessTokenConverter
 * @Description: 令牌转换器，将身份认证信息转换后存储在令牌内
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
public class JwtEnhanceAccessTokenConverter extends DefaultAccessTokenConverter {
    public JwtEnhanceAccessTokenConverter(){
        super.setUserTokenConverter(new JwtEnhanceUserAuthenticationConverter());
    }
}
