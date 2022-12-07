package com.ddx.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.ConversionUtils;
import com.ddx.util.basis.utils.SerialNumber;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: GlobalAuthenticationFilter
 * @Description: 全局过滤器，对token的拦截，解析token放入header中，便于下游微服务获取用户信息
 * 分为如下几步：
 * 生成全局流水号 防止重放 白名单放行  校验token 读取token中存放的用户信息 重新封装用户信息加密传递
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {
    /**
     * JWT令牌的服务
     */
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String serialNumber = SerialNumber.newInstance(BasisConstant.SERIAL_LOG, BasisConstant.DATE_FORMAT_12).toString();
        MDC.put(BasisConstant.REQUEST_SERIAL_NUMBER,serialNumber);
        String requestUrl = exchange.getRequest().getPath().value();
        Object object = redisTemplateUtils.hget(RedisConstant.PERMISSION_URLS,BasisConstant.POST+requestUrl);
        Mono<Void> mono = validatedRequest(exchange,chain,serialNumber,requestUrl);
        if (!mono.equals(Mono.empty())){
            return mono;
        }
        //检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            return invalidTokenMono(exchange);
        }

        try {
            //OAuth2判断是否是有效的token
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            //解析token，使用tokenStore
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            //令牌的唯一ID
            String jti = additionalInformation.get(BasisConstant.JTI).toString();
            //查看黑名单中是否存在这个jti，如果存在则这个令牌不能用
            if (stringRedisTemplate.hasKey(RedisConstant.JTI_KEY_PREFIX + jti)) {
                return invalidTokenMono(exchange);
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(BasisConstant.PRINCIPAL_NAME, additionalInformation.get("user_name").toString());
            //获取用户权限
            List<String> authorities = (List<String>) additionalInformation.get(BasisConstant.AUTHORITIES_NAME);
            jsonObject.put(BasisConstant.AUTHORITIES_NAME,authorities);
            //过期时间，单位秒
            jsonObject.put(BasisConstant.EXPR,oAuth2AccessToken.getExpiresIn());
            jsonObject.put(BasisConstant.JTI,jti);
            //封装到JSON数据中
            jsonObject.put(BasisConstant.USER_ID, additionalInformation.get(BasisConstant.USER_ID).toString());
            jsonObject.put(BasisConstant.NICKNAME, additionalInformation.get(BasisConstant.NICKNAME).toString());
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate()
                    .header(BasisConstant.TOKEN_NAME, SM4Utils.encryptBase64(jsonObject.toJSONString()))
                    .header(BasisConstant.GATEWAY_REQUEST, SM4Utils.encryptBase64(String.valueOf(false)))
                    .header(BasisConstant.REQUEST_SERIAL_NUMBER, serialNumber).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return invalidTokenMono(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 验证请求
     * @param exchange
     * @param chain
     * @param serialNumber
     * @return
     */
    private Mono<Void> validatedRequest(ServerWebExchange exchange,GatewayFilterChain chain,String serialNumber,String requestUrl){
        ServerHttpRequest request = exchange.getRequest();
        String ip = request.getRemoteAddress().getAddress().toString();
        //判断请求是否频繁发起
        if (redisTemplateUtils.isLock(RedisConstant.SYSTEM_REQUEST+ip+requestUrl)){
            return frequentResponseError(exchange);
        }
        //可频繁发起请求白名单
        List<String> requestTimeWhitelist =  ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.REQUEST_TIME_WHITELIST).toString()),String.class);
        ExceptionUtils.businessException(requestTimeWhitelist.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (!StringUtil.checkUrls(requestTimeWhitelist, requestUrl)) {
            SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
            redisTemplateUtils.lock(RedisConstant.SYSTEM_REQUEST + ip + requestUrl, sysParamConfigVo.getSysRequestTime());
        }
        //白名单请求
        List<String> ignoreUrls =  ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.WHITELIST_REQUEST).toString()),String.class);
        ExceptionUtils.businessException(ignoreUrls.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (StringUtil.checkUrls(ignoreUrls, requestUrl)){
            ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                    .header(BasisConstant.GATEWAY_REQUEST, SM4Utils.encryptBase64(String.valueOf(true)))
                    .header(BasisConstant.REQUEST_SERIAL_NUMBER, serialNumber).build();
            ServerWebExchange build = exchange.mutate().request(httpRequest).build();
            return chain.filter(build);
        }
        return Mono.empty();
    }

    /**
     * 从请求头中获取Token
     * @param exchange
     * @return
     */
    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst(BasisConstant.AUTHORIZATION);
        if (StringUtils.isBlank(tokenStr)) {
            return null;
        }
        String token = tokenStr.split(" ")[1];
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token;
    }

    /**
     * 无效的token
     * @param exchange
     * @return
     */
    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN);
        return buildReturnMono(baseResponse,exchange,HttpStatus.OK);
    }

    /**
     * 频繁发送响应请求
     * @param exchange
     * @return
     */
    private Mono<Void> frequentResponseError(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.FREQUENT_RESPONSE_ERROR);
        return buildReturnMono(baseResponse,exchange,HttpStatus.OK);
    }


    /**
     * 返回请求
     * @param baseResponse
     * @param exchange
     * @param httpStatus
     * @return
     */
    private Mono<Void> buildReturnMono(BaseResponse baseResponse, ServerWebExchange exchange,HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSON.toJSONString(BaseResponse.toMap(baseResponse)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset:utf-8");
        return response.writeWith(Mono.just(buffer));
    }
}
