package com.ddx.gateway.common;

import com.alibaba.fastjson.JSON;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @ClassName: GatewayCommon
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年12月08日 10:54
 * @Version: 1.0
 */
public class GatewayCommon {

    /**
     * 从请求头中获取Token
     * @param exchange
     * @return
     */
    public static Map<String,String> getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst(BasisConstant.AUTHORIZATION);
        if (StringUtils.isBlank(tokenStr)) {
            return null;
        }
        String[] token = tokenStr.split(" ");
        if (StringUtils.isBlank(token.length>1?token[1]:"")) {
            return null;
        }
        Map<String,String> tokenMap = Maps.newHashMap();
        tokenMap.put(BasisConstant.TOKEN_PREFIX_KEY,token[0]);
        tokenMap.put(BasisConstant.TOKEN_KEY,token[1]);
        return tokenMap;
    }

    /**
     * 无效的token
     * @param exchange
     * @return
     */
    public static Mono<Void> invalidTokenMono(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN);
        return buildReturnMono(baseResponse,exchange, HttpStatus.OK);
    }


    /**
     * 频繁发送响应请求
     * @param exchange
     * @return
     */
    public static Mono<Void> frequentResponseError(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.FREQUENT_RESPONSE_ERROR);
        return buildReturnMono(baseResponse,exchange, HttpStatus.OK);
    }

    /**
     * 返回请求
     * @param baseResponse
     * @param exchange
     * @param httpStatus
     * @return
     */
    public static Mono<Void> buildReturnMono(BaseResponse baseResponse, ServerWebExchange exchange,HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSON.toJSONString(BaseResponse.toMap(baseResponse)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset:utf-8");
        return response.writeWith(Mono.just(buffer));
    }

}
