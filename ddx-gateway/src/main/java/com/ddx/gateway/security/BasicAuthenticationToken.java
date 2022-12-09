package com.ddx.gateway.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;

/**
 * @ClassName: BasicAuthenticationToken
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年12月09日 10:54
 * @Version: 1.0
 */
public class BasicAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 520L;
    private final String token;

    public BasicAuthenticationToken(String token) {
        super((Collection)null);
        this.token = token;
        this.setAuthenticated(false);
    }

    @Deprecated
    public Object getCredentials() {
        return "";
    }

    @Deprecated
    public Object getPrincipal() {
        return "";
    }

    public Object getToken() {
        return this.token ;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }
}
