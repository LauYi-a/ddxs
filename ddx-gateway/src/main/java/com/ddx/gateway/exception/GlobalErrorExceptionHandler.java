package com.ddx.gateway.exception;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.response.BaseResponse;
import com.ddx.common.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: GlobalErrorExceptionHandler
 * @Description: 用于网关的全局异常处理
 * @Order(-1)： 优先级一定要比ResponseStatusExceptionHandler低
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        //自定义枚举异常信息提示
        List<CommonEnumConstant.PromptMessage> enumPromptMsg = Arrays.stream(CommonEnumConstant.PromptMessage.values()).filter(e -> e.getMsg().equals(ex.getMessage())).collect(Collectors.toList());
        BaseResponse resultMsg = ResponseData.out(CommonEnumConstant.PromptMessage.SYS_ERROR,String.format(CommonEnumConstant.PromptMessage.SYS_ERROR.getMsg(),ex.getMessage()));
        if ( enumPromptMsg.size()>0 ){
            resultMsg = ResponseData.out(enumPromptMsg.get(0));
        }
        // JOSN格式返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        //处理TOKEN失效的异常
        if (ex instanceof InvalidTokenException){
            resultMsg = ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN);
        }

        BaseResponse finalResultMsg = resultMsg;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                //todo 返回响应结果，根据业务需求，自己定制
                return bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(finalResultMsg));
            }
            catch (Exception e) {
                log.error("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
