package com.ddx.gateway.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.BusinessException;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @ClassName: JwtAuthenticationManager
 * @Description: JWT认证管理器
 * 请求携带了token 对携带过来的token进行校验
 * 一旦token校验通过，则交给鉴权管理器进行鉴权
 * 如未携带token 直接进入鉴权管理
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    /**
     * 使用JWT令牌进行解析令牌
     */
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof BearerTokenAuthenticationToken){
            return Mono.justOrEmpty(authentication)
                    .filter(a -> a instanceof BearerTokenAuthenticationToken)
                    .cast(BearerTokenAuthenticationToken.class)
                    .map(BearerTokenAuthenticationToken::getToken)
                    .flatMap((accessToken -> {
                        OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                        //根据access_token从数据库获取不到OAuth2AccessToken
                        if (oAuth2AccessToken == null) {
                            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
                        } else if (oAuth2AccessToken.isExpired()) {
                            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.EXCEED_TOKEN));
                        }
                        OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(accessToken);
                        if (oAuth2Authentication == null) {
                            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
                        } else {
                            return Mono.just(oAuth2Authentication);
                        }
                    })).cast(Authentication.class);
        }else if(authentication instanceof BasicAuthenticationToken){
            return Mono.justOrEmpty(authentication)
                    .filter(a -> a instanceof BasicAuthenticationToken)
                    .cast(BasicAuthenticationToken.class)
                    .map(BasicAuthenticationToken::getToken)
                    .flatMap((accessToken -> {
                        try {
                            String token = String.valueOf(accessToken);
                            String json =  SM4Utils.decryptBase64(token);
                            JSONObject jsonObject = JSON.parseObject(json);
                            if (redisTemplateUtils.hasKey(RedisConstant.BASIC_TOKEN+jsonObject.get(BasisConstant.JWT))){
                                return Mono.just(new BasicAuthenticationToken(token));
                            }else{
                                return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.EXCEED_TOKEN));
                            }
                        }catch (Exception e){
                            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
                        }
                    })).cast(Authentication.class);
        }
        return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
    }
}