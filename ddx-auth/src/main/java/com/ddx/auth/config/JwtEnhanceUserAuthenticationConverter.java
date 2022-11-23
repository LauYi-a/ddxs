package com.ddx.auth.config;

import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.web.entity.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName: JwtEnhanceUserAuthenticationConverter
 * @Description: 从map中提提取用户信息
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
public class JwtEnhanceUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    /**
     * 重写抽取用户数据方法
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            String username = (String) map.get(USERNAME);
            String nickname = (String) map.get(BasisConstant.NICKNAME);
            String loginService = (String) map.get(BasisConstant.LOGIN_SERVICE);
            Long userId = Long.valueOf(map.get(BasisConstant.USER_ID).toString());
            SecurityUser user =new SecurityUser(userId,username,nickname,loginService,"",authorities);
            return new UsernamePasswordAuthenticationToken(user, "", authorities);
        }
        return null;
    }

    /**
     * 提取权限
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

}