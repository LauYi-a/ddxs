package com.ddx.auth.exception;

import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: RequestAccessDeniedHandler
 * @Description: 当认证后的用户访问受保护的资源时，权限不够，则会进入这个处理器
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class RequestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtils.result(response, ResponseData.out(CommonEnumConstant.PromptMessage.NO_PERMISSION));
    }
}