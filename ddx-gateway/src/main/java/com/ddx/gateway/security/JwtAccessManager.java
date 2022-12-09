package com.ddx.gateway.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.utils.ConversionUtils;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: JwtAccessManager
 * @Description: 鉴权管理器
 * 对JwtAuthenticationManager认证成功的根据token类型进行鉴权
 * 白名单的，直接通过交给 GlobalAuthenticationFilter 过滤器token校验
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class JwtAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplateUtil redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethodValue();
        String restFulPath = method + BasisConstant.METHOD_SUFFIX + path;
        //在白名单的请求不用鉴权
        List<String> whitelistUrls = ConversionUtils.castList(JSONObject.parseArray(redisTemplate.get(RedisConstant.WHITELIST_REQUEST).toString()),String.class);
        if (StringUtil.checkUrls(whitelistUrls, path)) {
            return  Mono.just(new AuthorizationDecision(true));
        }
        //根据不同token类型采用不同方式进行鉴权
        String authorization = exchange.getRequest().getHeaders().getFirst(BasisConstant.AUTHORIZATION);
        if (!StringUtils.startsWithIgnoreCase(authorization, BasisConstant.AUTHORIZATION_TYPE_BASIC+" ")) {
            Map<Object, Object> entries = redisTemplate.hmget(RedisConstant.OAUTH_URLS);  //获取所有的uri->角色对应关系
            ExceptionUtils.businessException(entries.size() == 0, CommonEnumConstant.PromptMessage.REDIS_NOT_RESOURCES_ERROR);
            List<String> authorities = (List<String>) entries.get(restFulPath);
            //认证通过且角色匹配的用户可访问当前路径
            return mono.filter(Authentication::isAuthenticated)
                    //判断是否认证成功
                    .flatMapIterable(Authentication::getAuthorities)
                    //获取认证后的全部权限
                    .map(GrantedAuthority::getAuthority)
                    //如果权限包含则判断为true
                    .any(authority->{
                        if (StrUtil.equals(BasisConstant.ROLE_ROOT_CODE,authority)) {
                            return true;
                        }
                        //其他必须要判断角色是否存在交集
                        return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority);
                    }).map(AuthorizationDecision::new).defaultIfEmpty(new AuthorizationDecision(false));
        }else{
            //目前basic不需要额外的鉴权
            return  Mono.just(new AuthorizationDecision(true));
        }
    }
}
