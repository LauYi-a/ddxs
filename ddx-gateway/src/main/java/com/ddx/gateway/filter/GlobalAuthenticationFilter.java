package com.ddx.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.SysParamConfigVo;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.SerialNumber;
import com.ddx.basis.utils.StringUtil;
import com.ddx.basis.utils.sm4.SM4Utils;
import com.ddx.common.utils.RedisTemplateUtils;
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
    private RedisTemplateUtils redisTemplateUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String serialNumber = SerialNumber.newInstance(ConstantUtils.SERIAL_LOG,ConstantUtils.DATE_FORMAT_7).toString();
        MDC.put(ConstantUtils.REQUEST_SERIAL_NUMBER,serialNumber);
        String requestUrl = exchange.getRequest().getPath().value();
        ServerHttpRequest request = exchange.getRequest();
        String ip = request.getRemoteAddress().getAddress().toString();
        //1.判断是否存在短时间请求频繁
        if (redisTemplateUtils.isLock(ConstantUtils.SYSTEM_REQUEST+ip+requestUrl)){
            return frequentResponseError(exchange);
        }

        //2.请求时效白名单
        List<String> requestTimeWhitelist =  StringUtil.castList(JSONObject.parseArray(redisTemplateUtils.get(ConstantUtils.REQUEST_TIME_WHITELIST).toString()),String.class);
        ExceptionUtils.errorBusinessException(requestTimeWhitelist.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (!StringUtil.checkUrls(requestTimeWhitelist, requestUrl)) {
            SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(ConstantUtils.SYS_PARAM_CONFIG);
            redisTemplateUtils.lock(ConstantUtils.SYSTEM_REQUEST + ip + requestUrl, sysParamConfigVo.getSysRequestTime());
        }

        //3.白名单放行
        List<String> ignoreUrls =  StringUtil.castList(JSONObject.parseArray(redisTemplateUtils.get(ConstantUtils.WHITELIST_REQUEST).toString()),String.class);
        ExceptionUtils.errorBusinessException(ignoreUrls.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (StringUtil.checkUrls(ignoreUrls, requestUrl)){
            exchange.getRequest().mutate()
                    .header(ConstantUtils.REQUEST_SERIAL_NUMBER, serialNumber).build();
            return chain.filter(exchange);
        }
        //4.检查token是否存在
        String token = getToken(exchange);
        if (StringUtils.isBlank(token)) {
            return invalidTokenMono(exchange);
        }
        //5.判断是否是有效的token
        OAuth2AccessToken oAuth2AccessToken;
        try {
            //解析token，使用tokenStore
            oAuth2AccessToken = tokenStore.readAccessToken(token);
            Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
            //令牌的唯一ID
            String jti=additionalInformation.get(ConstantUtils.JTI).toString();
            /**查看黑名单中是否存在这个jti，如果存在则这个令牌不能用****/
            Boolean hasKey = stringRedisTemplate.hasKey(ConstantUtils.JTI_KEY_PREFIX + jti);
            if (hasKey)
                return invalidTokenMono(exchange);
            //取出用户身份信息
            String user_name = additionalInformation.get("user_name").toString();
            //获取用户权限
            List<String> authorities = (List<String>) additionalInformation.get(ConstantUtils.AUTHORITIES_NAME);
            //从additionalInformation取出userId
            String userId = additionalInformation.get(ConstantUtils.USER_ID).toString();
            String nickname = additionalInformation.get(ConstantUtils.NICKNAME).toString();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(ConstantUtils.PRINCIPAL_NAME, user_name);
            jsonObject.put(ConstantUtils.AUTHORITIES_NAME,authorities);
            //过期时间，单位秒
            jsonObject.put(ConstantUtils.EXPR,oAuth2AccessToken.getExpiresIn());
            jsonObject.put(ConstantUtils.JTI,jti);
            //封装到JSON数据中
            jsonObject.put(ConstantUtils.USER_ID, userId);
            jsonObject.put(ConstantUtils.NICKNAME, nickname);
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate()
                    .header(ConstantUtils.TOKEN_NAME, SM4Utils.encryptBase64(jsonObject.toJSONString()))
                    .header(ConstantUtils.REQUEST_SERIAL_NUMBER, serialNumber).build();
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
     * 从请求头中获取Token
     */
    private String getToken(ServerWebExchange exchange) {
        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
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
     */
    private Mono<Void> invalidTokenMono(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN);
        return buildReturnMono(baseResponse,exchange,HttpStatus.OK);
    }

    /**
     * 频繁响应请求
     */
    private Mono<Void> frequentResponseError(ServerWebExchange exchange) {
        BaseResponse baseResponse = ResponseData.out(CommonEnumConstant.PromptMessage.FREQUENT_RESPONSE_ERROR);
        return buildReturnMono(baseResponse,exchange,HttpStatus.OK);
    }


    private Mono<Void> buildReturnMono(BaseResponse baseResponse, ServerWebExchange exchange,HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSON.toJSONString(baseResponse).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json;charset:utf-8");
        return response.writeWith(Mono.just(buffer));
    }
}
