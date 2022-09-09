package com.ddx.auth.exception;

import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.ResponseData;
import com.ddx.common.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: OAuthServerAuthenticationEntryPoint
 * @Description: 用于处理客户端想认证出错，包括客户端id、密码错误
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
@Slf4j
public class OAuthServerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 认证失败处理器会调用这个方法返回提示信息
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtils.result(response, ResponseData.out(CommonEnumConstant.PromptMessage.CLIENT_AUTHENTICATION_FAILED));
    }
}