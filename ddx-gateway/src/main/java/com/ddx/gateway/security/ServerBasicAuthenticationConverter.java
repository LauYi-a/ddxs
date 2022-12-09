package com.ddx.gateway.security;

import com.ddx.util.basis.constant.BasisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: ServerBasicAuthenticationConverter
 * @Description: 校验token是否符合
 * @Author: YI.LAU
 * @Date: 2022年12月09日 10:54
 * @Version: 1.0
 */
public class ServerBasicAuthenticationConverter implements ServerAuthenticationConverter {

    public Mono<Authentication> convert(ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst(BasisConstant.AUTHORIZATION);
        if (!StringUtils.startsWithIgnoreCase(authorization, BasisConstant.AUTHORIZATION_TYPE_BASIC+" ")) {
            return Mono.empty();
        } else {
           String[] tokens = authorization.split(" ");
           if (tokens.length != 2){
               return Mono.empty();
           }
           return Mono.just(new BasicAuthenticationToken(tokens[1]));
        }
    }
}