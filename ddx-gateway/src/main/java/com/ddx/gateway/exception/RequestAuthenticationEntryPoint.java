package com.ddx.gateway.exception;

import cn.hutool.json.JSONUtil;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.response.ResponseData;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @ClassName: RequestAuthenticationEntryPoint
 * @Description: 用于处理没有登录或token过期时的自定义返回结果
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Component
public class RequestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body= JSONUtil.toJsonStr(ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}