package com.ddx.gateway.exception;

import cn.hutool.json.JSONUtil;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.response.ResponseData;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @ClassName: RequestAccessDeniedHandler
 * @Description: 自定义返回结果：没有权限访问时
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Component
public class RequestAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String body= JSONUtil.toJsonStr(ResponseData.out(CommonEnumConstant.PromptMessage.NO_PERMISSION));
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}