package com.ddx.gateway.common;

import com.ddx.util.basis.constant.BasisConstant;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * @ClassName: GatewayCommon
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年12月08日 10:54
 * @Version: 1.0
 */
@Component
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
}
