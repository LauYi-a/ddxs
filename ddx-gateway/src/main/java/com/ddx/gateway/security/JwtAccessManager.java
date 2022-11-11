package com.ddx.gateway.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.common.utils.RedisTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: JwtAccessManager
 * @Description: 鉴权管理器 用于认证成功之后对用户的权限进行鉴权
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Slf4j
@Component
public class JwtAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplateUtils redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //匹配url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        //请求方法 POST,GET
        String method = authorizationContext.getExchange().getRequest().getMethodValue();
        
        String restFulPath = method + ConstantUtils.METHOD_SUFFIX + uri.getPath();
        //获取所有的uri->角色对应关系
        Map<Object, Object> entries = redisTemplate.hmget(ConstantUtils.OAUTH_URLS);
        ExceptionUtils.businessException(entries.size() == 0, CommonEnumConstant.PromptMessage.REDIS_NOT_RESOURCES_ERROR);
        //角色集合
        List<String> authorities = (List<String>) entries.get(restFulPath);
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果权限包含则判断为true
                .any(authority->{
                    //超级管理员直接放行
                    if (StrUtil.equals(ConstantUtils.ROLE_ROOT_CODE,authority))
                        return true;
                    //其他必须要判断角色是否存在交集
                    return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
