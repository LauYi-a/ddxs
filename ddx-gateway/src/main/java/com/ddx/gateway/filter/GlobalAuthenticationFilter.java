package com.ddx.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddx.gateway.common.GatewayCommon;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.utils.ConversionUtils;
import com.ddx.util.basis.utils.SerialNumber;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Mono<Void> mono = validatedRequest(exchange,chain,serialNumber,requestUrl);
        if (!mono.equals(Mono.empty())){
            return mono;
        }
        try {
            //检查请求token
            Map<String,String> tokenMap = GatewayCommon.getToken(exchange);
            JSONObject jsonObject = analysisRequestToken(tokenMap);
            if (Objects.isNull(jsonObject)){
                return GatewayCommon.invalidTokenMono(exchange);
            }
            //查看黑名单中是否存在这个jti，如果存在则这个令牌不能用
            if (stringRedisTemplate.hasKey(RedisConstant.JTI_KEY_PREFIX + jsonObject.get(BasisConstant.JTI))) {
                return GatewayCommon.invalidTokenMono(exchange);
            }
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            ServerHttpRequest tokenRequest = exchange.getRequest().mutate()
                    .header(BasisConstant.TOKEN_NAME, SM4Utils.encryptBase64(jsonObject.toJSONString()))
                    .header(BasisConstant.GATEWAY_REQUEST, SM4Utils.encryptBase64(String.valueOf(false)))
                    .header(BasisConstant.REQUEST_SERIAL_NUMBER, serialNumber).build();
            ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
            return chain.filter(build);
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return GatewayCommon.invalidTokenMono(exchange);
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
        String ip = exchange.getRequest().getRemoteAddress().getAddress().toString();
        //判断请求是否频繁发起
        if (redisTemplateUtils.isLock(RedisConstant.SYSTEM_REQUEST+ip+requestUrl)){
            return GatewayCommon.frequentResponseError(exchange);
        }
        //不可频繁发起请求的路由进行上锁并设置锁过期时间
        List<String> requestTimeWhitelist =  ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.REQUEST_TIME_WHITELIST).toString()),String.class);
        ExceptionUtils.businessException(requestTimeWhitelist.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (!StringUtil.checkUrls(requestTimeWhitelist, requestUrl)) {
            SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
            redisTemplateUtils.lock(RedisConstant.SYSTEM_REQUEST + ip + requestUrl, sysParamConfigVo.getSysRequestTime());
        }
        //白名单放行 如请求携带token则解析信息放入请求
        List<String> whitelistUrls = ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.WHITELIST_REQUEST).toString()),String.class);
        whitelistUrls.addAll(ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.WHITELIST_RESOURCES).toString()),String.class));
        if (StringUtil.checkUrls(whitelistUrls, requestUrl)) {
            Map<String,String> tokenMap = GatewayCommon.getToken(exchange);
            JSONObject jsonObject = analysisRequestToken(tokenMap);
            ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                    .header(BasisConstant.TOKEN_NAME, Objects.isNull(jsonObject)?"":SM4Utils.encryptBase64(jsonObject.toJSONString()))
                    .header(BasisConstant.GATEWAY_REQUEST, SM4Utils.encryptBase64(String.valueOf(true)))
                    .header(BasisConstant.REQUEST_SERIAL_NUMBER, serialNumber).build();
            ServerWebExchange build = exchange.mutate().request(httpRequest).build();
            return chain.filter(build);
        }

        return Mono.empty();
    }

    /**
     * 解析请求 token
     * @param tokenMap
     * @return
     */
    private JSONObject analysisRequestToken(Map<String,String> tokenMap){
        try {
            if (Objects.isNull(tokenMap)) {
                return null;
            }
            if (Objects.equals(tokenMap.get(BasisConstant.TOKEN_PREFIX_KEY),BasisConstant.AUTHORIZATION_TYPE_BASIC)){
                String token = String.valueOf(tokenMap.get(BasisConstant.TOKEN_KEY));
                String json =  SM4Utils.decryptBase64(token);
                JSONObject tokenJson = JSON.parseObject(json);
                JSONObject securityUser = JSON.parseObject(SM4Utils.decryptBase64(redisTemplateUtils.get(RedisConstant.BASIC_TOKEN+tokenJson.getString(BasisConstant.JWT)).toString()));
                JSONObject tokenConfig = JSON.parseObject(SM4Utils.decryptBase64(tokenJson.getString(BasisConstant.JWT_CONFIG)));
                JSONObject jsonObject=new JSONObject();
                jsonObject.put(BasisConstant.EXPR, tokenConfig.get(BasisConstant.EXPR));
                jsonObject.put(BasisConstant.JTI, tokenConfig.get(BasisConstant.JTI));
                jsonObject.put(BasisConstant.PRINCIPAL_NAME, securityUser.get(BasisConstant.PRINCIPAL_NAME));
                jsonObject.put(BasisConstant.AUTHORITIES_NAME, securityUser.get(BasisConstant.AUTHORITIES_NAME));
                jsonObject.put(BasisConstant.USER_ID, securityUser.get(BasisConstant.USER_ID));
                jsonObject.put(BasisConstant.NICKNAME, securityUser.get(BasisConstant.NICKNAME));
                return jsonObject;
            }else {
                JSONObject jsonObject=new JSONObject();
                //OAuth2判断是否是有效的token
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenMap.get(BasisConstant.TOKEN_KEY));
                //解析token，使用tokenStore
                Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
                //令牌的唯一ID
                String jti = additionalInformation.get(BasisConstant.JTI).toString();
                jsonObject.put(BasisConstant.PRINCIPAL_NAME, additionalInformation.get("user_name").toString());
                //获取用户权限
                List<String> authorities = (List<String>) additionalInformation.get(BasisConstant.AUTHORITIES_NAME);
                jsonObject.put(BasisConstant.AUTHORITIES_NAME, authorities);
                //过期时间，单位秒
                jsonObject.put(BasisConstant.EXPR, oAuth2AccessToken.getExpiresIn());
                jsonObject.put(BasisConstant.JTI, jti);
                //封装到JSON数据中
                jsonObject.put(BasisConstant.USER_ID, additionalInformation.get(BasisConstant.USER_ID).toString());
                jsonObject.put(BasisConstant.NICKNAME, additionalInformation.get(BasisConstant.NICKNAME).toString());
                return jsonObject;
            }
        }catch (Exception e){
            return null;
        }
    }
}
