package com.ddx.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddx.gateway.common.GatewayCommon;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.BusinessException;
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
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String serialNumber = SerialNumber.newInstance(BasisConstant.SERIAL_LOG, BasisConstant.DATE_FORMAT_12).toString();
        MDC.put(BasisConstant.REQUEST_SERIAL_NUMBER,serialNumber);
        String requestUrl = exchange.getRequest().getPath().value();
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
        Mono<Void> mono = validatedRequest(exchange,chain,serialNumber,requestUrl,sysParamConfigVo);
        if (!mono.equals(Mono.empty())){
            return mono;
        }
        try {
            //检查请求token
            Map<String,String> tokenMap = GatewayCommon.getToken(exchange);
            JSONObject jsonObject = analysisRequestToken(tokenMap);
            if (Objects.isNull(jsonObject)){
                return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
            }
            //查看黑名单中是否存在这个jti，如果存在则这个令牌不能用
            if (redisTemplateUtils.hasKey(RedisConstant.JTI_KEY_PREFIX + jsonObject.get(BasisConstant.JTI))) {
                return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
            }
            //将解析后的token加密放入请求头中，方便下游微服务解析获取用户信息
            return chain.filter(settingRequestHeader(exchange,jsonObject,serialNumber,sysParamConfigVo,false));
        } catch (InvalidTokenException e) {
            //解析token异常，直接返回token无效
            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
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
    private Mono<Void> validatedRequest(ServerWebExchange exchange,GatewayFilterChain chain,String serialNumber,String requestUrl,SysParamConfigVo sysParamConfigVo){
        String ip = exchange.getRequest().getRemoteAddress().getAddress().toString();
        //判断请求是否频繁发起
        if (redisTemplateUtils.isLock(RedisConstant.SYSTEM_REQUEST+ip+requestUrl)){
            return Mono.error(new BusinessException(CommonEnumConstant.PromptMessage.FREQUENT_RESPONSE_ERROR));
        }
        //不可频繁发起请求的路由进行上锁并设置锁过期时间
        List<String> requestTimeWhitelist =  ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.REQUEST_TIME_WHITELIST).toString()),String.class);
        ExceptionUtils.businessException(requestTimeWhitelist.size() == 0, CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        if (!StringUtil.checkUrls(requestTimeWhitelist, requestUrl)) {
            redisTemplateUtils.lock(RedisConstant.SYSTEM_REQUEST + ip + requestUrl, sysParamConfigVo.getSysRequestTime());
        }
        //白名单放行 如请求携带token则解析信息放入请求
        List<String> whitelistUrls = ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.WHITELIST_REQUEST).toString()),String.class);
        whitelistUrls.addAll(ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(RedisConstant.WHITELIST_RESOURCES).toString()),String.class));
        if (StringUtil.checkUrls(whitelistUrls, requestUrl)) {
            return chain.filter(settingRequestHeader(exchange,analysisRequestToken(GatewayCommon.getToken(exchange)),serialNumber,sysParamConfigVo,true));
        }
        return Mono.empty();
    }

    /**
     * 根据token认证方式进行解析token请求
     * 目前只有两种解析方式
     * @param tokenMap
     * @return
     */
    private JSONObject analysisRequestToken(Map<String,String> tokenMap){
        try {
            if (Objects.equals(tokenMap.get(BasisConstant.TOKEN_PREFIX_KEY),BasisConstant.AUTHORIZATION_TYPE_BASIC)){
                //basic token方式解析
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
                //bearer oAuth token方式解析
                JSONObject jsonObject=new JSONObject();
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenMap.get(BasisConstant.TOKEN_KEY));
                Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
                String jti = additionalInformation.get(BasisConstant.JTI).toString();
                jsonObject.put(BasisConstant.PRINCIPAL_NAME, additionalInformation.get("user_name").toString());
                List<String> authorities = (List<String>) additionalInformation.get(BasisConstant.AUTHORITIES_NAME);
                jsonObject.put(BasisConstant.AUTHORITIES_NAME, authorities);
                jsonObject.put(BasisConstant.EXPR, oAuth2AccessToken.getExpiresIn());
                jsonObject.put(BasisConstant.JTI, jti);
                jsonObject.put(BasisConstant.USER_ID, additionalInformation.get(BasisConstant.USER_ID).toString());
                jsonObject.put(BasisConstant.NICKNAME, additionalInformation.get(BasisConstant.NICKNAME).toString());
                return jsonObject;
            }
        }catch (Exception e){
            return null;
        }
    }

    public ServerWebExchange settingRequestHeader(ServerWebExchange exchange, JSONObject tokenJson, String serialNumber, SysParamConfigVo sysParamConfigVo,Boolean isWhitelist){
        String gatewayTokenId = StringUtil.getUUID();
        ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                .header(BasisConstant.TOKEN_NAME, Objects.isNull(tokenJson)?"": SM4Utils.encryptBase64(tokenJson.toJSONString()))
                .header(BasisConstant.GATEWAY_REQUEST, gatewayTokenId)
                .header(BasisConstant.REQUEST_SERIAL_NUMBER, serialNumber).build();
        ServerWebExchange build = exchange.mutate().request(httpRequest).build();
        redisTemplateUtils.set(RedisConstant.SYSTEM_REQUEST_TOKEN+gatewayTokenId,SM4Utils.encryptBase64(String.valueOf(isWhitelist)),sysParamConfigVo.getGatewayTokenExpireTime());
        return build;
    }
}
